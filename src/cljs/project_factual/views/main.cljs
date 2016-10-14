(ns project-factual.views.main
  "Main view of the entire application"
  (:require [project-factual.views.items-list :as items-list]))

(defn main-page []
  [items-list/items-list])