(ns project-factual.handler.handlers
  (:require [cljs.spec :as spec]
            [clojure.set :as set]
            [project-factual.data.db :as db]
            [re-frame.core :as r]))

(defn check-db-against-spec
  "Throws an exception if db does not match spec"
  [spec db]
  (when-not (spec/valid? spec db)
    (throw (ex-info (str "Spec check for db failed; explanation: "
                         (spec/explain-str spec db))
                    {}))))

(def spec-interceptor (r/after (partial check-db-against-spec :project-factual.data.db/db)))

(def default-interceptors
  [spec-interceptor
   r/trim-v])

(r/reg-event-db
  :init-db
  [default-interceptors]
  (fn [db]
    (merge db/testing-database db)))

(r/reg-event-fx
  :item.switch-to
  [default-interceptors]
  (fn [{:keys [db]} [id]]
    (let [editor (:editor db)]
      {:db (assoc db :items.active-id id)
       :set-editor-value [editor (get-in db [:items id :item.content])]})))

(r/reg-event-db
  :item.trash
  [default-interceptors]
  (fn [db]
    (let [active-item-id (:items.active-id db)]
      (assoc db :items
                (dissoc (:items db) active-item-id)))))

(r/reg-event-db
  :new-active-group
  [default-interceptors]
  (fn [db [id]]
    (assoc db :groups.active-id id)))

(defn difference [col1 col2]
  (set/difference (set col1) (set col2)))

(defn min-unused-id [col]
  "Takes a collection of numbers, and finds the
   smallest number not present that's greater than the minimum.
   Very very inefficient, but I don't care. It won't get called that much."
  (apply min (difference (range
                           (apply min col)
                           ; +2 so that the generated list has an elem that is higher than
                           ; (max col), which allows `difference` to pick it as the missing elem
                           (+ 2 (apply max col)))
                         col)))

(r/reg-event-fx
  :item.new
  [default-interceptors]
  (fn [{:keys [db]} [type]]
    (let [id (min-unused-id (keys (:items db)))
          item {:item.id id
                :item.type type
                :item.content ""}]
      {:db (assoc-in db [:items id] item)
       :dispatch [:item.switch-to id]})))

(r/reg-event-db
  :new-col-group
  [default-interceptors]
  (fn [db [name]]
    (let [id (min-unused-id (keys (:groups db)))
          group {:group.id id
                 :group.type :group.col
                 :group.name name}]
      (assoc-in db [:group id] group))))

(r/reg-event-db
  :add-group-to-active-item
  [default-interceptors]
  (fn [db [group]]
    (update-in db
               [:items (:items.active-id db) :item.groups]
               #(conj % (:group.id group)))))

(r/reg-event-db
  :todo
  [default-interceptors]
  (fn todo []
    (println "TODO")))

(defn reg-dbk-set [event db-key]
  "Registers a db handler for `event` that sets the db's `db-key` to the value given"
  (r/reg-event-db
    event
    [default-interceptors]
    (fn [db [new-val]]
      (assoc db db-key new-val))))

(reg-dbk-set :pref.theme.set :pref.theme)
(reg-dbk-set :pref.ui.font.set :pref.ui.font)

;; ----------
;; REPL conveniences
;; ----------

(r/reg-event-db
  :repl-reset-db
  (fn [_ [_]]
    db/testing-database))