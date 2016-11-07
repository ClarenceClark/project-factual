(ns project-factual.editor.editor
  "Actual html hiccup view for the app, wrapper is in p/editor"
  (:require [re-frame.core :as r]
            [reagent.core :as reagent]
            [project-factual.views.toolbar :as toolbar]))

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
                    {}])
       (r/dispatch [:set-editor-contents-bypass]))

     ; Destoy cm instance on unmount, or else it will cause a memory leak
     :componentWillUnmount
     (fn [this]
       (r/dispatch [:destroy-editor]))}))

(defn editor []
  [:div {:class "content"}
   [toolbar/toolbar]
   [:div.editor
    [codemirror-component]]])
