(ns project-factual.subs
  (:require [re-frame.core :as r]
            [com.rpl.specter :as s]))

(defn group-from-id [group-id db]
  (s/select* [:groups group-id] db))

(r/register-sub
  :active-item-group
  (fn [db _]
    (group-from-id (:active-item-group-id db) db)))