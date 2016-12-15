(ns project-factual.handler.ui-handlers
  (:require [project-factual.handler.handlers :refer [default-interceptors]]
            [re-frame.core :as r]))

(r/reg-event-db
  :ui.pane-mid.toggle
  [default-interceptors]
  (fn [db]
    (assoc db
      :ui.pane-mid.show
      (not (:ui.pane-mid.show db)))))

(r/reg-event-db
  :ui.sidebar.set
  [default-interceptors]
  (fn [db [visibility]]
    (assoc db :ui.sidebar.show visibility)))

(r/reg-event-db
  :ui.sidebar.toggle
  [default-interceptors]
  (fn [db]
    (assoc db
      :ui.sidebar.show
      (not (:ui.sidebar.show db)))))

(r/reg-event-db
  :ui.mdpreview.toggle
  [default-interceptors]
  (fn toggle-editor-mdpreview
    [{preview-status :editor.mdpreview-status :as db}]
    (assoc db :editor.mdpreview-status
              (not preview-status))))