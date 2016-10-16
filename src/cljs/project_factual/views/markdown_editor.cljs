(ns project-factual.views.markdown-editor
  "Actual html hiccup view for the app, wrapper is in p/editor"
  (:require [re-frame.core :as r]
            [reagent.core :as reagent]
            [cljsjs.codemirror]))

(defn- codemirror-component []
  ;(let [active-item (r/subscribe [:active-item])]
    (reagent/create-class
      {:reagent-render
       (fn []
         [:textarea.codemirror-textarea])

       ; Init CodeMirror instance on mount
       :component-did-mount
       (fn [this]
         (r/dispatch [:init-textarea
                      (reagent/dom-node this)
                      {}]))}))

(defn editor []
  [:div {:class "editor"
         :id "editor"}
   [codemirror-component]])