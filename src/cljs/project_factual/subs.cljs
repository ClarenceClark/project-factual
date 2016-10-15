(ns project-factual.subs
  (:require [re-frame.core :as r]
            [com.rpl.specter :as s]))

(r/reg-sub
  :active-item-group
  (fn [db _]
    (vals (:items db))))

(r/reg-sub
  :active-item
  (fn [db _]
    (:active-item-id db)))