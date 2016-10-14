(ns project-factual.handlers
  (:require [re-frame.core :as r]
            [project-factual.db :as db]))

(r/reg-event-db
  :init-db
  (fn [_ [_]]
    (db/testing-database)))