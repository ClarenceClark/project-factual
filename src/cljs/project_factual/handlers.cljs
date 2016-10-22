(ns project-factual.handlers
  (:require [re-frame.core :as r]
            [project-factual.data.db :as db]
            [project-factual.editor.cm-wrapper :as editor]
            [cljs.spec :as s]))

(defn check-db-against-spec
  "Throws an exception if db does not match spec"
  [spec db]
  (when-not (s/valid? spec db)
    (throw (ex-info (str "Spec check for db failed; explanation: "
                         (s/explain-str spec db))
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

(r/reg-event-db
  :init-textarea
  [default-interceptors]
  (fn [db [dom opts]]
    (assoc db :editor (editor/new-editor dom opts))))

(defn save-editor-value
  [db editor]
  (assoc-in db
            [:items (:active-item-id db) :item.content]
            (editor/get-value editor)))

(r/reg-event-db
  :save-editor-value
  [default-interceptors]
  (fn [db]
    (let [editor (:editor db)]
      (save-editor-value db editor))))

(r/reg-event-fx
  :new-active-item
  [default-interceptors]
  (fn [{:keys [db]} [id]]
    (let [editor (:editor db)]
      {:db (-> db
             (save-editor-value editor)
             (assoc :active-item-id id))
       :set-editor-value [editor (get-in db [:items id :item.content])]})))

(r/reg-event-db
  :new-active-group
  [default-interceptors]
  (fn [db [id]]
    (assoc db :active-group-id id)))

(r/reg-event-db
  :set-sidebar-visibility
  [default-interceptors]
  (fn [db [visibility]]
    (-> db
        (assoc :sidebar-active visibility)
        (assoc :screen-dim visibility))))

(r/reg-event-fx
  :destroy-editor
  [default-interceptors]
  (fn [{:keys [db]}]
    {:editor-destroy (:editor db)
     :db (assoc db :editor nil)}))

;; ------------
;; Side-effects
;; ------------

(r/reg-fx
  :editor-destroy
  (fn [editor]
    (editor/destroy-editor editor)))

(r/reg-fx
  :set-editor-value
  (fn [[cm new-value]]
    (editor/set-value cm new-value)))

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