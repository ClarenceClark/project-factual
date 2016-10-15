(ns project-factual.handlers
  (:require [re-frame.core :as r]
            [project-factual.db :as db]))

(r/reg-event-db
  :init-db
  (fn [_ [_]]
    (db/testing-database)))

(r/reg-event-db
  :items-list-elem-clicked
  (fn [db [_ id]]
    (assoc db :active-item-id id)))

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