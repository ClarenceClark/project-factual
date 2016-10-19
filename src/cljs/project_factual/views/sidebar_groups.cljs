(ns project-factual.views.sidebar-groups
  (:require [re-frame.core :as r]))

(defn sidebar-group-elem [group]
  [:li {:class "group-name"
        :on-click (r/dispatch [:new-active-group (:group.id group)])}
   [:div (:group.name group)]])

(defn sidebar-groups []
  (let [active (r/subscribe [:sidebar-active])
        groups (r/subscribe [:groups])]
    (fn []
      [:div {:class (str "sidebar-groups" (when-not @active " hidden"))}
       [:ul
        (for [group @groups]
          [sidebar-group-elem group])]])))