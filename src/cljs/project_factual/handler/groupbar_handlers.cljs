(ns project-factual.handler.groupbar-handlers
  (:require [project-factual.handler.handlers :refer [default-interceptors]]
            [re-frame.core :as r]))

(r/reg-event-db
  :remove-group-from-active
  [default-interceptors]
  (fn [db [group]]
    (update-in db [:items (:active-item-id db) :item.groups]
               #(disj % (:group.id group)))))