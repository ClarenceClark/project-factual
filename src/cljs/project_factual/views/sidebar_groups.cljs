(ns project-factual.views.sidebar-groups
  (:require [re-frame.core :as r]))

(defn sidebar-group-elem [group active-group]
  [:li {:class (str "sidebar-elem"
                    (when (= @active-group (:group.id group)) " sidebar-elem-active"))
        :on-click #((do
                      (r/dispatch [:new-active-group (:group.id group)])
                      (r/dispatch [:set-sidebar-visibility false])))}
   [:div {:class "group-name"}
    (:group.name group)]])

(defn sidebar-groups []
  (let [active (r/subscribe [:sidebar-active])
        groups (r/subscribe [:groups])
        active-group (r/subscribe [:active-group-id])]
    (fn []
      [:div {:class (str "sidebar" (when-not @active " sidebar-hidden"))}
       [:ul
        (for [group @groups]
          [sidebar-group-elem group active-group])]])))