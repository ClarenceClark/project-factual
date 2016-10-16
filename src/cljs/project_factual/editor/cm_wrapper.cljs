(ns project-factual.editor.cm-wrapper
  "Custom implementation of a markdown editor based on CodeMirror"
  (:require [re-frame.core :as r]))

(defn- init-codemirror
  "New codemirror object"
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

(defn- register-event-handler [cm event handler]
  (.on cm
       event
       handler))

(defn- register-all-event-handlers [cm]
  (do
    (register-event-handler
      cm
      "changes"
      (fn [instance changes]
        (r/dispatch [:write-editor-value-to-database instance])))))

(defn new-editor
  "return a new codemirror editor"
  [textarea clj-opts]
  (let [cm (init-codemirror textarea clj-opts)]
    (register-all-event-handlers cm)
    cm))

;;; ----
;;; cljs cm interface
;;; ----

(defn get-doc [cm] (.getDoc cm))

(defn get-value [cm] (.getValue (get-doc cm)))
(defn set-value [cm new-value] (.setValue (get-doc cm) new-value))
(defn get-line [cm line] (.getLine cm line))