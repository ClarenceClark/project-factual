(ns project-factual.views.misc-views
  (:require [re-frame.core :as r]))

(defn toolbar-button [icon-name on-click-dispatch-vec]
  [:div {:class (str "toolbar-icon hover-background " icon-name)
         :on-click #(r/dispatch on-click-dispatch-vec)}])