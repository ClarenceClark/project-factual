(ns project-factual.editor
  "Custom implementation of a markdown editor based on CodeMirror"
  (:require [re-frame.core :as r]))

(defn new-editor
  "return a new codemirror editor"
  [textarea clj-opts]
  (.fromTextArea
    js/CodeMirror
    textarea
    (clj->js (merge
               {:autofocus true
                :mode "markdown"
                :matchBrackets true
                :autoCloseBrackets true
                :lineNumbers false
                :lineWrapping true}
               clj-opts))))

(defn register-event-handler [cm event handler]
  (.on cm
       event
       handler))

(defn register-all-event-handlers [cm]
  (do
    (register-event-handler
      cm
      "changes"
      (fn [instance changes]
        (r/dispatch [:on-editor-changes instance changes])))))