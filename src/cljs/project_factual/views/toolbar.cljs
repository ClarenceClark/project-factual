(ns project-factual.views.toolbar
  (:require [re-frame.core :as r]
            [project-factual.views.suggestions-box :as sug-box]
            [clojure.set :as set]
            [clojure.string :as string]))

(defn groupbar-elem [group]
  [:div {:class "dropdown-container groupbar-elem hover-background"
         :on-context-menu #(do (r/dispatch [:remove-group-from-active group])
                               (.preventDefault %))}
   (:group.name group)])

(defn groupbar []
  (let [groups (r/subscribe [:active-groups])
        all-groups (r/subscribe [:all-normal-groups])]
    [:div.groupbar
     (for [group @groups]
       ^{:key group}
       [groupbar-elem group])
     [sug-box/suggestions-box
      (fn data-source [search]
        (sort-by #(:group.name %)
                 (set/difference
                   (set (filter #(string/includes? (string/lower-case (:group.name %))
                                                   (string/lower-case search))
                                @all-groups))
                   (set @groups))))
      ; Not-so-temporary workaround: since the component has no way of knowing
      ; that it should fetch new suggestions, we make sure that groups change
      ; before it fetches new components for the on-change operation
      #(r/dispatch-sync [:add-group-to-active-item %])
      #(:group.name %)
      :on-blur #(r/dispatch [:focus-editor])]]))

(defn toolbar []
  (let [preview-status (r/subscribe [:editor-mdpreview-status])]
    (fn []
      [:div {:class "toolbar border-bottom"}
       [groupbar]
       [:div.icon-bar
        [:div {:class "toolbar-icon hover-background icon-info-circled-alt"
               :on-click #(r/dispatch [:display-active-item-info])}]
        [:div {:class (str "toolbar-icon hover-background "
                           (if @preview-status "icon-eye" "icon-eye-off"))
               :on-click #(r/dispatch [:toggle-editor-mdpreview])}]
        [:div {:class "toolbar-icon hover-background icon-trash-empty"
               :on-click #(r/dispatch [:move-active-item-to-trash])}]
        [:div {:class "toolbar-icon hover-background icon-dot-3"
               :on-click #(r/dispatch [:menu-extra-toggle])}]]])))