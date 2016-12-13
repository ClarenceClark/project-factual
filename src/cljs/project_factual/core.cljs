(ns project-factual.core
  (:require [reagent.core :as reagent]
            [cljsjs.react]
            [re-frame.core :as r]
            [re-frisk.core :as re-frisk]
            [clojure.string :as str]

            ; Included for deps loading and registration
            [project-factual.views.master-view :as main]
            [project-factual.handler.handlers]
            [project-factual.handler.editor-handlers]
            [project-factual.subs]))

(defn mount-root []
  (reagent/render [main/main-page]
                  (.getElementById js/document "app")))

(defn main
  []
  (r/dispatch-sync [:init-db])
  (re-frisk/enable-re-frisk!)
  (mount-root))