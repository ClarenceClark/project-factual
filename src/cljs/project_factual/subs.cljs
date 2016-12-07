(ns project-factual.subs
  (:require [re-frame.core :as r]
            [clojure.string :as s]
            [clojure.set :as set]))

(defn reg-keyword-diff-sub [key1 key2]
  "Same as `reg-keyword-sub`, except that key1 is the sub name and key2
   is the name of the db field"
  (r/reg-sub
    key1
    (fn [db _]
      (key2 db))))

(defn reg-keyword-sub [key]
  "For the subscriptions that are a field in the database with the
   same name as the sub"
  (reg-keyword-diff-sub key key))

;;; -----
;;; ITEMS
;;; -----

(reg-keyword-sub :active-item-id)
(reg-keyword-diff-sub :items-map :items)

(r/reg-sub
  :all-items
  :<- [:items-map]
  (fn [items-map _]
    (vals items-map)))

(r/reg-sub
  :active-item
  :<- [:items-map]
  :<- [:active-item-id]
  (fn [[items-map active-item-id] _]
    (items-map active-item-id)))

;;; ------
;;; GROUPS
;;; ------

(reg-keyword-sub :active-group-id)
(reg-keyword-diff-sub :groups-map :groups)

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
    (filter #(= :group.collection
                (:group.type %))
            all-groups)))

(r/reg-sub
  :group-all
  (fn [all-groups _]
    project-factual.data.db/group-all))

;;; --------
;;; Groupbar
;;; --------

(reg-keyword-sub :groupbar-suggestions-search)
(reg-keyword-sub :groupbar-suggestions-active)
(reg-keyword-sub :active-suggestions-index)

;;; ---------------------
;;; Active items & groups
;;; ---------------------

(r/reg-sub
  :active-items
  :<- [:active-group]
  :<- [:all-items]
  :<- [:active-group-id]
  (fn [[active-group all-items active-group-id] _]
    (let [group-type (:group.type active-group)]
      ; If there is ever a need for more types of groups, this needs to be refactored into its own
      ; group handling system with more documentation
      (case group-type
        :group.filter (filter (:group.filter active-group) all-items)
        :group.collection (filter #(contains? (:item.groups %) active-group-id) all-items)))))

(r/reg-sub
  :active-groups
  :<- [:active-item]
  :<- [:groups-map]
  (fn [[active-item groups-map] _]
    (sort-by #(:group.name %)
      (vals (select-keys groups-map
                         (:item.groups active-item))))))

(r/reg-sub
  :groupbar-suggestions
  :<- [:groupbar-suggestions-search]
  :<- [:all-normal-groups]
  :<- [:active-groups]
  (fn [[input groups active-groups] _]
    (sort-by #(:group.name %)
             (set/difference
               (set (filter #(s/includes? (:group.name %) input)
                             groups))
               (set active-groups)))))

;;; ------
;;; OTHERS
;;; ------

(reg-keyword-sub :sidebar-active)
(reg-keyword-sub :editor.mdpreview-status)

(r/reg-sub
  :screen-dim
  (fn [db _]
    ; The only source of screen dim (currently) is the sidebar
    (:sidebar-active db)))

(println "I made it heret")