(ns project-factual.editor.cm-wrapper
  "Custom implementation of a markdown editor based on CodeMirror"
  (:require [re-frame.core :as r]
            [cljsjs.codemirror]
            [cljsjs.codemirror.addon.edit.continuelist]
            [cljsjs.codemirror.addon.edit.closebrackets]
            [cljsjs.codemirror.addon.edit.matchbrackets]
            [cljsjs.codemirror.addon.scroll.scrollpastend]
            [cljsjs.codemirror.mode.markdown]
            [project-factual.editor.spellcheck :as spellcheck]))

(defn- init-codemirror
  "New codemirror object"
  [textarea clj-opts]
  (.fromTextArea
    js/CodeMirror
    textarea
    (clj->js (merge
               {:autofocus true
                :mode "markdown"
                :matchBrackets true ; From addon matchbrackets
                :autoCloseBrackets true ; From addon closebrackets
                :lineNumbers false
                :lineWrapping true
                :scrollPastEnd true
                :extraKeys {"Enter" "newlineAndIndentContinueMarkdownList" ; From addon continuelist
                            "Tab" #(r/dispatch [:exec-cm-command "indentMore"])
                            "Shift-Tab" (r/dispatch [:exec-cm-command "indentLess"])}}
               clj-opts))))

(defn- register-event-handler [cm event handler]
  (.on cm
       event
       handler))

(def linebreak-overlay
  #js{"token"
      (fn [stream]
        (if (.match stream #"^\s\s+$")
          "linebreak"
          (do (.match stream #"^\s*\S*")
              nil)))})

(defn register-all-event-handlers [cm]
  (do
    (register-event-handler
      cm
      "changes"
      #(r/dispatch [:save-editor-value]))
    (.addOverlay cm spellcheck/spellcheck-overlay)
    (.addOverlay cm linebreak-overlay)))

(defn new-editor
  "return a new codemirror editor"
  [textarea clj-opts]
  (let [cm (init-codemirror textarea clj-opts)]
    (register-all-event-handlers cm)
    cm))

;;; ----
;;; cljs cm interface
;;; ----

(defn destroy-editor [cm] (.toTextArea cm))
(defn get-doc [cm] (.getDoc cm))
(defn get-value [cm] (.getValue (get-doc cm)))
(defn set-value [cm new-value] (.setValue (get-doc cm) new-value))
(defn get-line [cm line] (.getLine cm line))
(defn exec-command [cm command] (.execCommand cm command))