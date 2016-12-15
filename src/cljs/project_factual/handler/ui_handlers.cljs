(ns project-factual.handler.ui-handlers
  (:require [project-factual.handler.handlers :refer [default-interceptors]]
            [re-frame.core :as r]))

(r/reg-event-db
  :ui.pane-mid.show.toggle
  [default-interceptors]
  (fn [db]
    (assoc db
      :ui.pane-mid.show
      (not (:ui.pane-mid.show db)))))

(r/reg-event-db
  :ui.sidebar.show.set
  [default-interceptors]
  (fn [db [visibility]]
    (assoc db :ui.sidebar.show visibility)))

(r/reg-event-db
  :ui.sidebar.show.toggle
  [default-interceptors]
  (fn [db]
    (assoc db
      :ui.sidebar.show
      (not (:ui.sidebar.show db)))))