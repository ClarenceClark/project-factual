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
         :on-click #(r/dispatch [:click-tag group])}
   (:group.name group)])

(defn groupbar []
  (let [groups (r/subscribe [:active-groups])]
    [:div.groupbar
     (for [group @groups]
       ^{:key group}
       [groupbar-elem group])]))

(defn editor []
  [:div {:class "content"}
   [:div.toolbar
    [groupbar]
    [:div {:class "toolbar-icon icon-right hover-background icon-dot-3"
           :on-click #(r/dispatch [:menu-extra-toggle])}]
    [:div {:class "toolbar-icon icon-right hover-background icon-trash-empty"
           :on-click #(r/dispatch [:move-active-item-to-trash])}]
    [:div {:class "toolbar-icon icon-right hover-background icon-info-circled-alt"
           :on-click #(r/dispatch [:display-active-item-info])}]]
   [:div.editor
    [codemirror-component]]])