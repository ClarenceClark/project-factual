(ns project-factual.views.master-view
  "Main view of the entire application"
  (:require [project-factual.views.pane-mid :as mid-pane]
            [project-factual.views.pane-right :as right-pane]
            [project-factual.views.sidebar :as sidebar]
            [project-factual.views.preferences :as pref]
            [re-frame.core :as r]
            [re-com.core :as rc]))

(defn dim-component [dim]
  [:div {:class (str "dim" (when-not dim " hide"))
         :on-click #(r/dispatch [:ui.sidebar.toggle])}])

(defn main-page []
  (let [pref-active? (r/subscribe [:ui.pref.show])
        theme (r/subscribe [:pref.theme])
        ui-font (r/subscribe [:pref.ui.font])]
    (fn []
      [:div {:class (str "main " @theme)
             :style {:font-family @ui-font}}
       [rc/h-box
        :class "main-panes"
        :children
        [[sidebar/sidebar]
         [mid-pane/pane-mid]
         [right-pane/pane-right]]]
       [pref/pref-pane @pref-active?]])))