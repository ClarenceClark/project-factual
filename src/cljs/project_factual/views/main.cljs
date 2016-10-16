(ns project-factual.views.main
  "Main view of the entire application"
  (:require [project-factual.views.items-list :as items-list]
            [project-factual.views.markdown-editor :as markdown-editor]))

(defn main-page []
  [:div {:class "main"}
   [items-list/items-list]
   [markdown-editor/editor]])