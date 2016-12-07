(ns project-factual.views.pane-right
  "Actual html hiccup view for the app, wrapper is in p/editor"
  (:require [re-frame.core :as r]
            [reagent.core :as reagent]
            [project-factual.views.toolbar :as toolbar]
            [project-factual.editor.md-preview :as md-preview]
            [project-factual.views.misc-views :as misc]
            [clojure.string :as string]
            [clojure.set :as set]
            [project-factual.views.suggestions-box :as sug-box]))

(defn- codemirror-component []
  (reagent/create-class
    {:reagent-render
     (fn []
       [:textarea#codemirror-textarea])

     ; Init CodeMirror instance on mount
     :component-did-mount
     (fn [this]
       (r/dispatch [:init-textarea
                    (reagent/dom-node this)
                    {}])
       (r/dispatch [:set-editor-contents-bypass]))

     ; Destoy cm instance on unmount, or else it will cause a memory leak
     :componentWillUnmount
     (fn [this]
       (r/dispatch [:destroy-editor]))}))

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

(defn toolbar-editor []
  (let [preview-status (r/subscribe [:editor.mdpreview-status])]
    (fn []
      [:div {:class "toolbar toolbar-editor border-bottom"}
       [groupbar]
       [:div.icon-bar
        [misc/toolbar-button "icon-info-circled-alt" [:display-active-item-info]]
        [misc/toolbar-button (if @preview-status "icon-eye-off" "icon-eye")
         [:toggle-editor-mdpreview]]
        [misc/toolbar-button "icon-trash-empty" [:move-active-item-to-trash]]
        [misc/toolbar-button "icon-dot-3" [:todo]]]])))

(defn pane-right []
  (let [preview? (r/subscribe [:editor.mdpreview-status])]
    (fn []
      [:div {:class "pane-right"}
       [toolbar-editor]
       [:div {:class (str "cm-container" (when @preview? " hide"))}
        [codemirror-component]]
       [md-preview/md-preview]])))