(ns project-factual.subs
  (:require [re-frame.core :as r]))

;;; -----
;;; ITEMS
;;; -----

(r/reg-sub
  :active-item-id
  (fn [db _]
    (:active-item-id db)))

(r/reg-sub
  :items-map
  (fn [db _]
    (:items db)))

(r/reg-sub
  :active-item
  :<- [:all-items]
  :<- [:active-item-id]
  (fn [[all-items active-item-id] _]
    (all-items active-item-id)))

;;; ------
;;; GROUPS
;;; ------

(r/reg-sub
  :groups-map
  (fn [db _]
    (:groups db)))

(r/reg-sub
  :active-group-id
  (fn [db _]
    (:active-group-id db)))

(r/reg-sub
  :all-groups
  :<- [:groups-map]
  (fn [groups-map _]
    (vals groups-map)))

(r/reg-sub
  :active-group
  (fn [db _]
    ((:groups db) (:active-group-id db))))

(r/reg-sub
  :all-normal-groups
  :<- [:all-groups]
  (fn [all-groups _]
    (filter #(= :group.col
                (:group.type %))
            all-groups)))

;;; ---------------------
;;; Active items & groups
;;; ---------------------

(r/reg-sub
  :active-items
  :<- [:active-group]
  :<- [:items-map]
  (fn [[active-group all-items] _]
    (let [group-type (:group.type active-group)]
      ; If there is ever a need for more types of groups, this needs to be refactored into its own
      ; group handling system with more documentation
      (case group-type
        :group.filter (filter (:group.filter active-group) (vals all-items))
        :group.col (vals (select-keys all-items (:group.elements active-group)))))))

(r/reg-sub
  :active-groups
  :<- [:active-item-id]
  :<- [:all-normal-groups]
  (fn [[active-item-id normal-groups] _]
    (filter #(contains? (:group.elements %)
                        active-item-id)
            normal-groups)))

;;; ------
;;; OTHERS
;;; ------

(r/reg-sub
  :sidebar-active
  (fn [db _]
    (:sidebar-active db)))

(r/reg-sub
  :screen-dim
  (fn [db _]
    ; The only source of screen dim (currently) is the sidebar
    (:sidebar-active db)))