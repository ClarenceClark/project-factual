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

(defn save-editor-value
  [db editor]
  (assoc-in db
            [:items (:active-item-id db) :content]
            (editor/get-value editor)))

(r/reg-event-db
  :save-editor-value
  (fn [db [_ id]]
    (let [editor (:editor db)]
      (save-editor-value db editor))))

(r/reg-event-fx
  :new-active-item
  (fn [{:keys [db]} [_ id]]
    (let [editor (:editor db)]
      {:db (let [editor-saved (save-editor-value db editor)
                 new-active (assoc editor-saved :active-item-id id)]
             new-active)
       :set-editor-value [editor (get-in db [:items id :content])]})))


;; ------------
;; Side-effects
;; ------------


(r/reg-fx
  :set-editor-value
  (fn [[cm new-value]]
    (editor/set-value cm new-value)))

(r/reg-fx
  :dispatch-sync
  (fn [event]
    (r/dispatch event)))

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
    (assoc db :items (:items (db/testing-database)))))