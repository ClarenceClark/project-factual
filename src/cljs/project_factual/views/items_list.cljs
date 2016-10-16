(ns project-factual.views.items-list
  (:require [re-frame.core :as r]
            [clojure.string :as str]
            [goog.string :as gstring]))

(defn- space-if-blank [str]
  (if (str/blank? str)
    ; Trick browser into rendering a line so that the
    ; item sizes are consistent (it looks prettier)
    (gstring/unescapeEntities "&nbsp;")
    str))

(defn items-list-elem [item active-item-id]
  "Elements of the items-list; summaries are controlled through CSS"
  (fn [item active-item-id]
    (let [lines (filter #(not (str/blank? %)) ; We don't want to preview blank lines
                        (str/split-lines (:content item)))
          id (:id item)]
      [:li {:class    (str "items-list-elem" (when (= id @active-item-id) " list-elem-active"))
            :on-click #(r/dispatch [:new-active-item (:id item)])}
       [:div {:class "list-elem-title"}
        (space-if-blank (first lines))]
       [:p {:class "list-elem-summary one-line-summary"}
        (space-if-blank (second lines))]])))

(defn items-list []
  (let [items (r/subscribe [:active-item-group])
        active-item-id (r/subscribe [:active-item-id])]
    (fn []
      [:div {:class "items-list-container"}
       [:ul
        (for [item @items]
          [items-list-elem item active-item-id])]])))