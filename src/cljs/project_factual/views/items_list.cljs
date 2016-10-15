(ns project-factual.views.items-list
  (:require [re-frame.core :as r]
            [clojure.string :as str]))

(defn items-list-elem [item active-item]
  "Elements of the items-list; summaries are controlled through CSS"
  [:li {:class (str "items-list-elem" (when (= (:id item) @active-item) " list-elem-active"))
        :on-click #(r/dispatch [:items-list-elem-clicked (:id item)])}
   [:div {:class "list-elem-title"}
    (:name item)]
   [:p {:class "list-elem-summary one-line-summary"}
    (first (str/split-lines (:content item)))]])

(defn items-list []
  (let [items (r/subscribe [:active-item-group])
        active-item (r/subscribe [:active-item])]
    (fn []
      [:div {:class "items-list-container"}
       [:ul
        (for [item @items]
          [items-list-elem item active-item])]])))