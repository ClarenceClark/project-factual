(ns project-factual.views.toolbar
  (:require [re-frame.core :as r]))

(defn groupbar-elem [group]
  [:div {:class "dropdown-container groupbar-elem hover-background"
         :on-click #(r/dispatch [:click-tag group])
         :on-context-menu #(r/dispatch [:remove-group-from-active group])}
   (:group.name group)])

(defn new-group []
  (let [suggestions (r/subscribe [:groupbar-suggestions])
        sug-active (r/subscribe [:groupbar-suggestions-active])
        search (r/subscribe [:groupbar-suggestions-search])]
    (fn new-group []
      [:div.new-group
       [:input#group-input {:on-change #(r/dispatch [:groupbar-search-change (.-value (.-target %))])
                            :on-blur #(r/dispatch [:groupbar-suggestions-active false])
                            :on-focus #(do (r/dispatch [:groupbar-suggestions-active true]))
                            ; Sync because events gets recycled by react, and we must do
                            ; stuff with it before that happens. It only dispatches more events, so
                            ; the fact that it is out of order shouldn't matter, all other events
                            ; in queue will be processed before the ones dispatched by this one
                            :on-key-down #(r/dispatch-sync [:groupbar-input %])}]
       [:div {:class (str "suggestions"
                          (when-not @sug-active " hide"))}
        [:div {:class "suggestion hover-background"
               :on-click #(r/dispatch [:create-and-add-group-to-active])}
         (str "New group: \"" @search "\"")]
        (for [sug @suggestions]
          ^{:key sug}
          [:div {:class "suggestion hover-background"
                 :on-click #(r/dispatch [:add-group-to-active-item sug])}
           (:group.name sug)])]])))

(defn groupbar []
  (let [groups (r/subscribe [:active-groups])]
    [:div.groupbar
     (for [group @groups]
       ^{:key group}
       [groupbar-elem group])
     [new-group]]))

(defn toolbar []
  [:div.toolbar
   [groupbar]
   [:div.icon-bar
    [:div {:class "toolbar-icon hover-background icon-info-circled-alt"
           :on-click #(r/dispatch [:display-active-item-info])}]
    [:div {:class "toolbar-icon hover-background icon-trash-empty"
           :on-click #(r/dispatch [:move-active-item-to-trash])}]
    [:div {:class "toolbar-icon hover-background icon-dot-3"
           :on-click #(r/dispatch [:menu-extra-toggle])}]]])