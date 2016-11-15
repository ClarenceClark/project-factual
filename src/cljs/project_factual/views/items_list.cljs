(ns project-factual.views.items-list
  (:require [re-frame.core :as r]
            [clojure.string :as str]
            [goog.string :as gstring]))

(defn- space-if-blank [str]
  (if (str/blank? str)
    ; Trick browser into rendering a blank line so that the
    ; item sizes are consistent (it looks prettier)
    (gstring/unescapeEntities "&nbsp;")
    str))

(defn items-list-elem [item active-item-id]
  "Elements of the items-list; summaries are controlled through CSS"
  (fn [item active-item-id]
    (let [lines (filter #(not (str/blank? %)) ; We don't want to preview blank lines
                        (str/split-lines (:item.content item)))
          id (:item.id item)]
      [:li {:class    (str "items-list-elem hover-background"
                           (when (= id @active-item-id) " active"))
            :on-click #(r/dispatch [:new-active-item id])}
       [:div {:class "list-elem-title one-line-summary"}
        (space-if-blank (first lines))]
       [:p {:class "list-elem-summary one-line-summary"}
        (space-if-blank (second lines))]])))

(defn items-toolbar []
  [:div.toolbar.border-bottom
   [:div {:class "toolbar-icon hover-background"
          :on-click #(r/dispatch [:set-sidebar-visibility true])}
    [:i.icon-menu]]
   [:div {:class "toolbar-icon"}
    [:i.icon-search]]
   [:div {:class "toolbar-icon icon-right hover-background"
          :on-click #(r/dispatch [:new-item :item.markdown])}
    [:i.icon-doc-new]]])

(defn items-list []
  (let [items (r/subscribe [:active-items])
        active-item-id (r/subscribe [:active-item-id])]
    (fn []
      [:div {:class "items-list-container border-right"}
       [items-toolbar]
       [:ul {:class "items-list"}
        (for [item @items]
          ^{:key item}
          [items-list-elem item active-item-id])]])))