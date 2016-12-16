(ns project-factual.views.master-view
  "Main view of the entire application"
  (:require [project-factual.views.pane-mid :as mid-pane]
            [project-factual.views.pane-right :as right-pane]
            [project-factual.views.sidebar :as sidebar]
            [project-factual.views.preferences :as pref]
            [re-frame.core :as r]))

(defn dim-component [dim]
  [:div {:class (str "dim" (when-not dim " hide"))
         :on-click #(r/dispatch [:ui.sidebar.toggle])}])

(defn main-page []
  (let [dimmed (r/subscribe [:screen-dim])
        pref-active? (r/subscribe [:ui.pref.show])
        theme (r/subscribe [:pref.theme])]
    (fn []
      [:div {:class (str "main " @theme)}
       [sidebar/sidebar]
       [mid-pane/pane-mid]
       [right-pane/pane-right]
       [pref/pref-pane @pref-active?]
       [dim-component @dimmed]])))