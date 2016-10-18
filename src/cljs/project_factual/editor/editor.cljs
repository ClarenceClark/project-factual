(ns project-factual.editor.editor
  "Actual html hiccup view for the app, wrapper is in p/editor"
  (:require [re-frame.core :as r]
            [reagent.core :as reagent]
            [cljsjs.codemirror]))

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
                    {}]))}))

(defn editor []
  [:div {:class "editor"}
   [codemirror-component]])