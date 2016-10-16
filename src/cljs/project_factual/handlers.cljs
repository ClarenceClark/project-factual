(ns project-factual.handlers
  (:require [re-frame.core :as r]
            [project-factual.db :as db]
            [project-factual.editor.cm-wrapper :as editor]
            [com.rpl.specter :as s]))

(r/reg-event-db
  :init-db
  (fn [_ [_]]
    (db/testing-database)))

(r/reg-event-db
  :items-list-elem-clicked
  (fn [db [_ id]]
    (assoc db :active-item-id id)))

(r/reg-event-db
  :init-textarea
  (fn [db [_ dom opts]]
    (assoc db :editor (editor/new-editor dom opts))))

(r/reg-event-db
  :on-editor-changes
  (fn [db [_ cm changes]]
    (let [active-item-id (:active-item-id db)
          new-value (editor/get-value cm)]
      (s/transform* [:items active-item-id] new-value db))))

;; ----------
;; REPL conveniences
;; ----------

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