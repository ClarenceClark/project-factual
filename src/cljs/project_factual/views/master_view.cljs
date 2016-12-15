(ns project-factual.views.master-view
  "Main view of the entire application"
  (:require [project-factual.views.pane-mid :as mid-pane]
            [project-factual.views.pane-right :as right-pane]
            [project-factual.views.sidebar :as sidebar]
            [re-frame.core :as r]))

(defn dim-component [dim]
  [:div {:class (str "dim" (when-not dim " hide"))
         :on-click #(r/dispatch [:ui.sidebar.show.set false])}])

(defn main-page []
  (let [dimmed (r/subscribe [:screen-dim])]
    (fn []
      [:div {:class "main theme-light"}
       [sidebar/sidebar]
       [mid-pane/pane-mid]
       [right-pane/pane-right]
       [dim-component @dimmed]])))