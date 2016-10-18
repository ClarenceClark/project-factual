(ns project-factual.core
  (:require [reagent.core :as reagent]
            [cljsjs.react]
            [re-frame.core :as r]
            [project-factual.subs :as subs]
            [project-factual.handlers :as handlers]
            [project-factual.editor.cm-wrapper :as editor]
            [project-factual.views.main :as main]
            [clojure.string :as str]))

(defn main
  []
  (r/dispatch-sync [:init-db])
  (reagent/render [main/main-page]
                  (.getElementById js/document "app")))


;;; REPL Conveniences

(def repl-db @re-frame.db/app-db)