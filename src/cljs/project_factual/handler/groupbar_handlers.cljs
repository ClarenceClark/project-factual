(ns project-factual.handler.groupbar-handlers
  (:require [project-factual.handler.handlers :refer [default-interceptors]]
            [re-frame.core :as r]))

(r/reg-event-db
  :remove-group-from-active
  [default-interceptors]
  (fn [db [group]]
    (update-in db
               [:items (:active-item-id db) :item.groups]
               #(disj % (:group.id group)))))

(r/reg-event-db
  :groupbar-search-change
  [default-interceptors]
  (fn [db [new-value]]
    (assoc db :groupbar-suggestions-search new-value)))

(r/reg-event-db
  :groupbar-suggestions-active
  [default-interceptors]
  (fn [db [new]]
    (assoc db :groupbar-suggestions-active new)))

(r/reg-event-fx
  :groupbar-input
  [default-interceptors]
  (fn [{:keys [db]} [event]]
    (let [key (.-which event)
          ret-effects {:db db}]
      (cond
        ; Enter and Tab: select value
        (or (= key 13)
            (= key 9))
        (merge ret-effects
               {:dispatch [:add-group-to-active-item]
                :stop-event-default event})
        ; Up/Down: select prev/next item
        (= key 38)
        (merge ret-effects
               {:dispatch [:prev-suggestion]
                :stop-event-default event})
        (= key 40)
        (merge ret-effects
               {:dispatch [:next-suggestion]
                :stop-event-default event})
        ; Escape: cancel
        (= key 27)
        (merge ret-effects
               {:dispatch [:groupbar-cancel-add]})
        ; Else: do nothing special
        :else ret-effects))))

(r/reg-event-db
  :add-group-to-active-item
  (fn [db]
    #_(update-in db
               [:items (:active-item-id db) :item.groups]
               #(conj % (:group.id group)))
    db))

(r/reg-event-db
  :prev-suggestion
  (fn [db]
    db))

(r/reg-event-db
  :next-suggestion
  (fn [db]
    db))

(r/reg-event-db
  :groupbar-cancel-add
  (fn [db]
    db))

(r/reg-fx
  :stop-event-default
  (fn [event]
    (.preventDefault event)))