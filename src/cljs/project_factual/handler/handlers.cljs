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
  :new-active-item
  [default-interceptors]
  (fn [{:keys [db]} [id]]
    (let [editor (:editor db)]
      {:db (assoc db :active-item-id id)
       :set-editor-value [editor (get-in db [:items id :item.content])]})))

(r/reg-event-db
  :move-active-item-to-trash
  [default-interceptors]
  (fn [db]
    (let [active-item-id (:active-item-id db)]
      (assoc db :items
                (dissoc (:items db) active-item-id)))))

(r/reg-event-db
  :new-active-group
  [default-interceptors]
  (fn [db [id]]
    (assoc db :active-group-id id)))

(r/reg-event-db
  :set-sidebar-visibility
  [default-interceptors]
  (fn [db [visibility]]
    (assoc db :sidebar-active visibility)))

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
  :new-item
  [default-interceptors]
  (fn [{:keys [db]} [type]]
    (let [id (min-unused-id (keys (:items db)))
          item {:item.id id
                :item.type type
                :item.content ""}]
      {:db (assoc-in db [:items id] item)
       :dispatch [:new-active-item id]})))

(r/reg-event-db
  :new-col-group
  [default-interceptors]
  (fn [db [name]]
    (let [id (min-unused-id (keys (:groups db)))
          group {:group.id id
                 :group.type :group.col
                 :group.name name}]
      (assoc-in db [:group id] group))))

;; ----------
;; REPL conveniences
;; ----------

(r/reg-event-db
  :repl-reset-db
  (fn [_ [_]]
    db/testing-database))

(r/reg-event-db
  :repl-current-db
  (fn [db [_]]
    (cljs.pprint/pprint db)
    db))

(r/reg-event-db
  :repl-change-db
  (fn [db [_ func]]
    (func db)))

(r/reg-event-db
  :repl-change-key
  (fn [db [_ k v]]
    (assoc db k v)))

(r/reg-event-db
  :repl-reset-items
  (fn [db _]
    (assoc db :items (:items db/testing-database))))

(r/reg-event-db
  :repl-reset-groups
  (fn [db _]
    (assoc db :groups (:groups db/testing-database))))