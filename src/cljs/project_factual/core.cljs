(ns project-factual.core
  (:require [reagent.core :as reagent]
            [cljsjs.react]
            [re-frame.core :as r]
            [project-factual.subs]
            [project-factual.handlers]
            [project-factual.views.main :as main]
            [clojure.string :as str]))

(defn main
  []
  (r/dispatch-sync [:init-db])
  (reagent/render [main/main-page]
                  (.getElementById js/document "app")))