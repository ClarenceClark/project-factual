(ns project-factual.editor.editor
  "Actual html hiccup view for the app, wrapper is in p/editor"
  (:require [re-frame.core :as r]
            [reagent.core :as reagent]
            [cljsjs.codemirror]
            [cljsjs.codemirror.addon.edit.continuelist]
            [cljsjs.codemirror.addon.edit.closebrackets]
            [cljsjs.codemirror.addon.edit.matchbrackets]
            [cljsjs.codemirror.mode.markdown]))

(defn- codemirror-component []
  (reagent/create-class
    {:reagent-render
     (fn []
       [:textarea#codemirror-textarea])

     ; Init CodeMirror instance on mount
     :component-did-mount
     (fn [this]
       (r/dispatch [:init-textarea
                    (reagent/dom-node this)
                    {}]))

     ; Destoy cm instance on unmount, or else it will cause a memory leak
     :componentWillUnmount
     (fn [this]
       (r/dispatch [:destroy-editor]))}))

(defn editor []
  [:div {:class "content"}
   [:div.editor
    [codemirror-component]]])