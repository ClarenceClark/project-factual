(ns project-factual.core
  (:require [reagent.core :as reagent]
            [cljsjs.react]
            [project-factual.views.main :as main]))


(defn main-page
  []
  [main/main-page])

(defn mount-root
  []
  (reagent/render [main-page] (.getElementById js/document "app")))

(defn init!
  []
  (mount-root))