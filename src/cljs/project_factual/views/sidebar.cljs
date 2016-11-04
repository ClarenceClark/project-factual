(ns project-factual.views.sidebar
  (:require [re-frame.core :as r]))

(defn sidebar-group-elem-base [group active-group icon]
  [:div {:class (str "sidebar-elem hover-background"
                    (when (= @active-group (:group.id group)) " sidebar-elem-active"))
         :on-click #(do
                     (r/dispatch [:new-active-group (:group.id group)])
                     (r/dispatch [:set-sidebar-visibility false]))}
   (if (not (nil? icon))
     [:i {:class (str "group-icon " icon)}])
   [:div {:class "group-name"}
    (:group.name group)]])

(defn sidebar-group-elem [group active-group]
  [sidebar-group-elem-base group active-group nil])

(defn sidebar-group-elem-icon [group active-group]
  [sidebar-group-elem-base group active-group (:group.icon group)])

(defn sidebar []
  (let [active (r/subscribe [:sidebar-active])
        groups (r/subscribe [:all-normal-groups])
        active-group (r/subscribe [:active-group-id])
        group-all @(r/subscribe [:group-all])] ; Will never change, probably
    (fn []
      [:div {:class (str "sidebar" (when-not @active " sidebar-hidden"))}
       [:div.special-elems
        [sidebar-group-elem-icon group-all active-group]]
       [:div.sidebar-groups
        (for [group @groups]
          ^{:key group}
          [sidebar-group-elem group active-group])]])))