(ns project-factual.handlers
  (:require [re-frame.core :as r]
            [project-factual.db :as db]
            [project-factual.editor :as editor]))

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
    (println "hello world")
    (assoc db :editor (editor/new-editor dom opts))))

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