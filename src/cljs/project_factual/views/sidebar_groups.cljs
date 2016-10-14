(ns project-factual.views.sidebar-groups
  (:require [re-frame.core :as r]))

(defn builtin-group-list-elem [group]
  [:li #(r/dispatch [:on-item-group-clicked (:id group)])
   [:img (:img group)]
   [:div.group-name
    (:name group)]])

(defn builtin-elems []
  (let [inbox (r/subscribe [:groups/inbox])
        all-items (r/subscribe [:itmes/all])
        favs (r/subscribe [:items/favourite])
        trash (r/subscribe [:items/trash])]
    (fn []
      [:ul
       [builtin-group-list-elem @inbox]
       [builtin-group-list-elem @all-items]
       [builtin-group-list-elem @favs]
       [builtin-group-list-elem @trash]])))

(defn sidebar-groups []
  (let [user-groups (r/subscribe [:user-root-groups])]
    (fn []
      [builtin-elems])))