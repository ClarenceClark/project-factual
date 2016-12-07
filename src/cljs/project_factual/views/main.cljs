(ns project-factual.views.main
  "Main view of the entire application"
  (:require [project-factual.views.pane-mid :as mid-pane]
            [project-factual.views.pane-right :as right-pane]
            [project-factual.views.sidebar :as sidebar]
            [re-frame.core :as r]))

(defn main-page []
  (let [dimmed (r/subscribe [:screen-dim])]
    (fn []
      [:div {:class "main theme-light"}
       [sidebar/sidebar]
       [mid-pane/pane-mid]
       [right-pane/pane-right]
       [:div {:class (str "dim" (when-not @dimmed " hide"))
              :on-click #(r/dispatch [:sidebar.set-visibility false])}]])))