(ns project-factual.views.items-list
  (:require [re-frame.core :as r]
            [clojure.string :as str]))

(defn items-list-elem [item]
  "Elements of the items-list; summaries are controlled through CSS"
  [:li {:class (str "items-list-elem" #_(when (:active item) " list-elem-active"))}
   [:div {:class "list-elem-title"}
    (:name item)]
   [:p {:class ""}
    (first (str/split-lines (:content item)))]])

(defn items-list []
  (let [items (r/subscribe [:active-item-group])]
    (fn []
      [:div
       [:ul
        (for [item @items]
          [items-list-elem item])]])))