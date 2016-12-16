(ns project-factual.views.preferences
  (:require [re-com.core :as rc]
            [re-frame.core :as r]))

(def theme-choices
  [{:id "theme-light" :label "Light"}
   {:id "theme-dark" :label "Dark"}])

(defn pref-pane [active?]
  (let [theme (r/subscribe [:pref.theme])]
    (fn [active?]
      [rc/modal-panel
       :backdrop-on-click #(r/dispatch [:ui.pref.toggle])
       :class (str "pref-pane-container" (when-not active? " hide"))
       :child
       [rc/v-box
        :class "pref-pane"
        :width "700px"
        :gap "10px"
        :children
        [[rc/title
          :level :level2
          :label "Preferences"]
         [rc/h-box
          :gap "20px"
          :children
          [[rc/label :label "Theme: "]
           [rc/single-dropdown
            :choices theme-choices
            :model @theme
            :on-change #(r/dispatch [:pref.theme.set %])]]]]]])))