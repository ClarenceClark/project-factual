(ns project-factual.views.preferences
  (:require [re-com.core :as rc]
            [re-frame.core :as r]))

(def theme-choices
  [{:id "theme-light" :label "Light"}
   {:id "theme-dark" :label "Dark"}])

(defn identical-map [a] {:id a :label a})

(def fonts
  [(identical-map "Cormorant Garamond")
   (identical-map "Adobe Garamond Pro")
   (identical-map "Inconsolata")
   {:id "BlinkMacSystemFont" :label "San Francisco"}
   (identical-map "Helvetica Neue")
   (identical-map "Source Code Pro")])

(defn pref-pane-elem [name comp]
  [rc/h-box
   :gap "10px"
   :children
   [[rc/label :label name]
    comp]])

(defn pref-pane [active?]
  (let [theme (r/subscribe [:pref.theme])
        sidebar-dark-theme (r/subscribe [:pref.ui.sidebar.dark-theme])
        ui-font (r/subscribe [:pref.ui.font])]
    (fn [active?]
      [rc/modal-panel
       :backdrop-on-click #(r/dispatch [:ui.pref.toggle])
       :class (str "pref-pane-container" (when-not active? " hide"))
       :child
       [rc/v-box
        :class "pref-pane"
        :gap "10px"
        :children
        [[rc/title
          :level :level2
          :label "Preferences"]
         [pref-pane-elem "Theme:" [rc/single-dropdown
                                    :choices theme-choices
                                    :model @theme
                                    :on-change #(r/dispatch [:pref.theme.set %])]]
         [pref-pane-elem "UI Font:" [rc/single-dropdown
                                     :choices fonts
                                     :model @ui-font
                                     :on-change #(r/dispatch [:pref.ui.font.set %])]]
         [pref-pane-elem "Dark theme for sidebar? " [rc/checkbox
                                                     :model @sidebar-dark-theme
                                                     :on-change #(r/dispatch [:pref.ui.sidebar.dark-theme.set %])]]]]])))