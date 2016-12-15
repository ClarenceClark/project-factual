(ns project-factual.handler.ui-handlers
  (:require [project-factual.handler.handlers :refer [default-interceptors]]
            [re-frame.core :as r]))

(defn reg-toggle [event db-key]
  "Registers an event `event` that toggles `db-key` in db when fired.
   Assumes that `db-key` is a boolean"
  (r/reg-event-db
    event
    [default-interceptors]
    (fn [db]
      (assoc db
        db-key
        (not (db-key db))))))

(reg-toggle :ui.pane-mid.toggle :ui.pane-mid.show)
(reg-toggle :ui.sidebar.toggle :ui.sidebar.show)
(reg-toggle :ui.preview.toggle :ui.preview.show)
(reg-toggle :ui.pref.toggle :ui.pref.show)