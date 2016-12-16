(ns project-factual.core
  (:require [reagent.core :as reagent]
            [cljsjs.react]
            [re-frame.core :as r]
            [re-frisk.core :as re-frisk]
            [clojure.string :as str]

            ; Views
            [project-factual.views.master-view :as main]

            ; Handlers and views are required so that they actually register
            ; with re-frame
            ; Handlers
            [project-factual.handler.handlers]
            [project-factual.handler.editor-handlers]
            [project-factual.handler.keyboard-shorccuts]
            [project-factual.handler.ui-handlers]

            ; Subs
            [project-factual.subs]))

(defn mount-root []
  (reagent/render [main/main-page]
                  (.getElementById js/document "app")))

(defn reload []
  (r/clear-subscription-cache!)
  (mount-root))

(defn main
  []
  (r/dispatch-sync [:init-db])
  (r/dispatch-sync [:init-ipc-handlers])
  (re-frisk/enable-re-frisk!)
  (mount-root))