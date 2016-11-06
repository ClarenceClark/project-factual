(ns project-factual.editor.editor
  "Actual html hiccup view for the app, wrapper is in p/editor"
  (:require [re-frame.core :as r]
            [reagent.core :as reagent]
            [cljsjs.codemirror]
            [cljsjs.codemirror.addon.edit.continuelist]
            [cljsjs.codemirror.addon.edit.closebrackets]
            [cljsjs.codemirror.addon.edit.matchbrackets]
            [cljsjs.codemirror.mode.markdown]))

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
         :on-click #(r/dispatch [:click-tag group])
         :on-context-menu #(r/dispatch [:remove-group-from-active group])}
   (:group.name group)])

(defn new-group []
  (let [suggestions (r/subscribe [:groupbar-suggestions])
        sug-active (r/subscribe [:groupbar-suggestions-active])]
    (fn new-group []
      [:div.new-group
       [:input#group-input {:on-change #(r/dispatch [:groupbar-search-change (.-value (.-target %))])
                            :on-blur #(r/dispatch [:groupbar-suggestions-active false])
                            :on-focus #(r/dispatch [:groupbar-suggestions-active true])
                            ; Sync because events gets recycled by react, and we must do
                            ; stuff with it before that happens. It only dispatches more events, so
                            ; the fact that it is out of order shouldn't matter, all other events
                            ; in queue will be processed before the ones dispatched by this one
                            :on-key-down #(r/dispatch-sync [:groupbar-input %])}]
       [:div {:class (str "suggestions"
                          #_(when-not @sug-active " hide"))}
        (for [sug @suggestions]
          [:div.suggestion (:group.name sug)])]])))


(defn groupbar []
  (let [groups (r/subscribe [:active-groups])]
    [:div.groupbar
     (for [group @groups]
       ^{:key group}
       [groupbar-elem group])
     [new-group]]))

(defn editor []
  [:div {:class "content"}
   [:div.toolbar
    [groupbar]
    [:div.icon-bar
     [:div {:class "toolbar-icon hover-background icon-info-circled-alt"
            :on-click #(r/dispatch [:display-active-item-info])}]
     [:div {:class "toolbar-icon hover-background icon-trash-empty"
            :on-click #(r/dispatch [:move-active-item-to-trash])}]
     [:div {:class "toolbar-icon hover-background icon-dot-3"
            :on-click #(r/dispatch [:menu-extra-toggle])}]]]
   [:div.editor
    [codemirror-component]]])
