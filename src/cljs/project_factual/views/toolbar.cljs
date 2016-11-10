(ns project-factual.views.toolbar
  (:require [re-frame.core :as r]
            [re-com.core :as re-com]))

(defn groupbar-elem [group]
  [:div {:class "dropdown-container groupbar-elem hover-background"
         :on-click #(r/dispatch [:click-tag group])
         :on-context-menu #(r/dispatch [:remove-group-from-active group])}
   (:group.name group)])

(defn suggestion [index group selected?]
  [:div {:class (str "suggestion" (when selected? " selected"))
         :on-mouse-enter #(r/dispatch [:set-active-suggestions-index index])
         :on-click #(do (r/dispatch [:add-group-to-active-item group])
                        (r/dispatch [:reset-suggestions]))}
   (:group.name group)])

(defn new-group []
  (let [active? (r/subscribe [:groupbar-suggestions-active])
        suggestions (r/subscribe [:groupbar-suggestions])
        active-index (r/subscribe [:active-suggestions-index])]
    (fn []
      [:div.new-group
       [:input {:id "group-input"
                :on-change #(r/dispatch-sync [:groupbar-search-change (.-value (.-target %))])
                :on-focus #(r/dispatch [:groupbar-suggestions-active true])
                :on-blur #(r/dispatch [:groupbar-suggestions-active false])
                :on-key-down #(r/dispatch-sync [:groupbar-input %])}]
       [:div {:class (str "suggestions-container" (when-not @active? " hide"))}
        (doall
          (for [[index group] (map vector (range) @suggestions)
                :let [selected? (= index @active-index)]]
            ^{:key index}
            [suggestion index group selected?]))]])))

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