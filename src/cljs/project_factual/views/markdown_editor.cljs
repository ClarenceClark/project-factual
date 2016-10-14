(ns project-factual.views.markdown-editor
  "Actual html hiccup view for the app, wrapper is in p/editor"
  (:require [re-frame.core :as r]))

(defn no-item-active []
  [:p "No item currently active"])

(defn markdown-editor []
  (let [active-item (r/subscribe :items/active)]
    (fn []
      [:div
        (if (nil? @active-item)
          [no-item-active]
          [:textarea {:id "main-markdown-editor"}])])))