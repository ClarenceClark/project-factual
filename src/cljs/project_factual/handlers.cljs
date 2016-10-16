(ns project-factual.handlers
  (:require [re-frame.core :as r]
            [project-factual.db :as db]
            [project-factual.editor.cm-wrapper :as editor]
            [com.rpl.specter :as s]))

(r/reg-event-db
  :init-db
  (fn [db [_]]
    (merge (db/testing-database) db)))

(r/reg-event-db
  :init-textarea
  (fn [db [_ dom opts]]
    (assoc db :editor (editor/new-editor dom opts))))

(r/reg-event-db
  :write-editor-value-to-database
  (fn [db [_ cm]]
    (let [active-item-id (:active-item-id db)
          new-value (editor/get-value cm)]
      (update-in  db [:items active-item-id :content] new-value))))

(r/reg-event-fx
  :new-active-item
  (fn [{:keys [db]} [_ id]]
    {:dispatch [:write-editor-value-to-database (:editor db)]
     :set-editor-value [(:editor db) (get-in db [:items id :content])]
     :db (assoc db :active-item-id id)}))

;; ------------
;; Side-effects
;; ------------

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
  :repl-magic-editor
  (fn [db [_ editor]]
    (assoc db :editor editor)))