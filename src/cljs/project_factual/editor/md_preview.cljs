(ns project-factual.editor.md-preview
  "Markdown preview module"
  (:require [re-frame.core :as r]))

(defonce markdown-it (js/require "markdown-it"))

(defn new-mdrenderer []
  (markdown-it.
    #js{"html" false
        "xhtmlout" true
        "breaks" true
        "linkify" false
        "typographer" true}))

(defn md-preview []
  (let [active-item (r/subscribe [:items.active])
        active? (r/subscribe [:editor.mdpreview-status])
        renderer (new-mdrenderer)]
    (fn []
      [:div {:class (str "md-preview" (when-not @active? " hide"))}
       [:div
        {:class "md-preview-text-container"
         :dangerouslySetInnerHTML
         {:__html
          (if @active?
            (.render renderer (:item.content @active-item))
            "<strong> You should NOT be seeing this </strong>")}}]])))