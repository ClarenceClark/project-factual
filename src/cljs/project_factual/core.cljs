(ns project-factual.core
  (:require [reagent.core :as reagent]
            [cljsjs.react]
            [re-frame.core :as r]
            [project-factual.subs :as subs]
            [project-factual.handler.handlers :as handlers]
            [project-factual.handler.editor-handlers :as editor-handlers]
            [project-factual.editor.cm-wrapper :as editor]
            [project-factual.views.main :as main]
            [clojure.string :as str]))

(defn mount-root []
  (reagent/render [main/main-page]
                  (.getElementById js/document "app")))

(defn main
  []
  (r/dispatch-sync [:init-db])
  (mount-root))

;;; REPL Conveniences

(def repl-db re-frame.db/app-db)