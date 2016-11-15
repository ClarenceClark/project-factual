(ns project-factual.views.main
  "Main view of the entire application"
  (:require [project-factual.views.items-list :as items-list]
            [project-factual.editor.editor :as markdown-editor]
            [project-factual.views.sidebar :as sidebar]
            [re-frame.core :as r]))

(defn main-page []
  (let [dimmed (r/subscribe [:screen-dim])]
    (fn []
      [:div {:class "main theme-dark"}
       [sidebar/sidebar]
       [items-list/items-list]
       [markdown-editor/editor]
       [:div {:class (str "dim" (when-not @dimmed " hide"))
              :on-click #(r/dispatch [:set-sidebar-visibility false])}]])))