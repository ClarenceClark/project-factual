(ns project-factual.subs
  (:require [re-frame.core :as r]
            [com.rpl.specter :as s]
            [reagent.ratom :as ratom]))

(r/reg-sub
  :items-map
  (fn [db _]
    (:items db)))

(r/reg-sub
  :active-group
  (fn [db _]
    ((:groups db) (:active-group-id db))))

(r/reg-sub
  :active-item-group
  (fn [query-v _]
    [(r/subscribe [:items-map])
     (r/subscribe [:active-group])])

  (fn [[all-items active-group] _]
    (let [group-type (:group.type active-group)]
      ; If there is ever a need for more types of groups, this needs to be refactored into its own
      ; group handling system with more documentation
      (case group-type
        :group.filter (filter (:group.filter active-group) (vals all-items))
        :group.col (vals (select-keys all-items (:group.elements active-group)))))))

(r/reg-sub
  :active-item-id
  (fn [db _]
    (:active-item-id db)))

(r/reg-sub
  :active-item
  (fn [db _]
    ((:items db) (:active-item-id db))))

(r/reg-sub
  :groups
  (fn [db _]
    (vals (:groups db))))

(r/reg-sub
  :sidebar-active
  (fn [db _]
    (:sidebar-active db)))

(r/reg-sub
  :active-group-id
  (fn [db _]
    (:active-group-id db)))

(r/reg-sub
  :screen-dim
  (fn [db _]
    ; The only source of screen dim (currently) is the sidebar
    (:sidebar-active db)))