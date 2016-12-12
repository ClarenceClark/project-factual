(ns project-factual.handler.editor-handlers
  (:require [project-factual.editor.cm-wrapper :as editor]
            [re-frame.core :as r]
            [project-factual.handler.handlers :refer [default-interceptors]]))

(r/reg-event-db
  :init-textarea
  [default-interceptors]
  (fn [db [dom opts]]
    (assoc db :editor (editor/new-editor dom opts))))

(r/reg-event-db
  :save-editor-value
  [default-interceptors]
  (fn [db]
    (let [editor (:editor db)]
      (assoc-in db
                [:items (:items.active-id db) :item.content]
                (editor/get-value editor)))))

(r/reg-event-fx
  :destroy-editor
  [default-interceptors]
  (fn [{:keys [db]}]
    {:editor-destroy (:editor db)
     :db (assoc db :editor nil)}))

(r/reg-event-fx
  :exec-cm-command
  [default-interceptors]
  (fn [{:keys [db]} [cmd]]
    {:exec-cm-command [(:editor db) cmd]
     :db db}))

(r/reg-event-fx
  :set-editor-contents-bypass
  (fn [{:keys [db]}]
    {:db db
     :set-editor-value [(:editor db)
                        (get-in db [:items (:items.active-id db) :item.content])]}))

(r/reg-event-fx
  :focus-editor
  (fn [{:keys [db]} _]
    {:db db
     :focus-cm (:editor db)}))

;; ------------
;; Side-effects
;; ------------

(r/reg-fx
  :exec-cm-command
  (fn [[cm command]]
    (editor/exec-command cm command)))

(r/reg-fx
  :editor-destroy
  (fn [editor]
    (editor/destroy-editor editor)))

(r/reg-fx
  :set-editor-value
  (fn [[cm new-value]]
    (editor/set-value cm new-value)))

(r/reg-fx
  :focus-cm
  (fn [cm]
    (.focus cm)))