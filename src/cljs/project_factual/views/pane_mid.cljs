(ns project-factual.views.pane-mid
  (:require [re-frame.core :as r]
            [clojure.string :as str]
            [goog.string :as gstring]
            [project-factual.views.misc-views :as misc]))

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
      [:li {:class (str "items-list-elem hover-background"
                        (when (= id active-item-id) " active"))
            :on-click #(r/dispatch [:item.switch-to id])}
       [:div {:class "list-elem-title one-line-summary"}
        (space-if-blank (first lines))]
       [:div {:class "list-elem-summary one-line-summary"}
        (space-if-blank (second lines))]])))

(defn items-list [items active-item-id]
  [:ul {:class "items-list"}
   (for [item items]
     ^{:key item}
     [items-list-elem item active-item-id])])

(defn toolbar-items []
  [:div {:class "toolbar toolbar-midpane border-bottom"}
   [misc/toolbar-button "icon-menu" [:sidebar.set-visibility true]]
   [misc/toolbar-button "icon-search" [:todo]]
   [misc/toolbar-button "icon-doc-new icon-right" [:item.new :item.markdown]]])

(defn pane-mid []
  (let [items (r/subscribe [:active-items])
        active-item-id (r/subscribe [:active-item-id])]
    (fn []
      [:div {:class "pane-mid"}
       [toolbar-items]
       [items-list @items @active-item-id]])))