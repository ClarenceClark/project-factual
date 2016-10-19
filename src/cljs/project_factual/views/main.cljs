(ns project-factual.views.main
  "Main view of the entire application"
  (:require [project-factual.views.items-list :as items-list]
            [project-factual.editor.editor :as markdown-editor]
            [project-factual.views.sidebar-groups :as sidebar-groups]))

(defn main-page []
  [:div {:class "main"}
   [sidebar-groups/sidebar-groups]
   [items-list/items-list]
   [markdown-editor/editor]])