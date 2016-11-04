(ns project-factual.editor.cm-wrapper
  "Custom implementation of a markdown editor based on CodeMirror"
  (:require [re-frame.core :as r]
            [clojure.string :as string]))

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
                :lineWrapping true
                :extraKeys {"Enter" "newlineAndIndentContinueMarkdownList"
                            "Tab" #(r/dispatch [:exec-cm-command "indentMore"])
                            "Shift-Tab" (r/dispatch [:exec-cm-command "indentLess"])}}
               clj-opts))))

(defonce spellcheck (js/Typo. "en_GB" false false #js{:dictionaryPath "./dict"}))
(defonce word-seperators "'!'\\\"#$%&()*+,-./:);<=>?@[\\\\]^_`{|}~ '")
(defonce number-regex #"[0-9]+")

(defn advance-to-next-word-seperator [stream word seperators]
  "WARNING: SIDE EFFECTS, advances stream to the next seperator"
  (let [ch (.peek stream)]
    (if (and (not (nil? ch))
             (not (string/includes? seperators ch)))
      (do (.next stream)
          (recur stream (str word ch) seperators))
      word)))

(defn spellcheck-tokeniser [stream]
  (let [ch (.peek stream)]
    (if (string/includes? word-seperators ch)
      (do (.next stream)
          nil)
      (let [word (advance-to-next-word-seperator stream "" word-seperators)]
        (if (or (.check spellcheck word)
                (re-matches number-regex word)) ; Is a number
          nil
          "spell-error")))))

(def spellcheck-overlay #js{"token" spellcheck-tokeniser})

(defn- register-event-handler [cm event handler]
  (.on cm
       event
       handler))

(defn register-all-event-handlers [cm]
  (do
    (register-event-handler
      cm
      "changes"
      #(r/dispatch [:save-editor-value]))
    (.addOverlay cm spellcheck-overlay)))

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