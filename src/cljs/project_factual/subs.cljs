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

(reg-keyword-sub :items.active-id)
(reg-keyword-diff-sub :items.map-list :items)

(r/reg-sub
  :items.all
  :<- [:items.map-list]
  (fn [items-map _]
    (vals items-map)))

(r/reg-sub
  :items.active
  :<- [:items.map-list]
  :<- [:items.active-id]
  (fn [[items-map active-item-id] _]
    (items-map active-item-id)))

;;; ------
;;; GROUPS
;;; ------

(reg-keyword-sub :groups.active-id)
(reg-keyword-diff-sub :groups.map-all :groups)

(r/reg-sub
  :groups.all
  :<- [:groups.map-all]
  (fn [groups-map _]
    (vals groups-map)))

(r/reg-sub
  :groups.active
  (fn [db _]
    ((:groups db) (:groups.active-id db))))

(r/reg-sub
  :groups.all-normal
  :<- [:groups.all]
  (fn [all-groups _]
    (filter #(= :group.collection
                (:group.type %))
            all-groups)))

(r/reg-sub
  :group-all
  (fn [all-groups _]
    project-factual.data.db/group-all))

;;; ---------------------
;;; Active items & groups
;;; ---------------------

(r/reg-sub
  :items.active-list
  :<- [:groups.active]
  :<- [:items.all]
  :<- [:groups.active-id]
  (fn [[active-group all-items active-group-id] _]
    (let [group-type (:group.type active-group)]
      ; If there is ever a need for more types of groups, this needs to be refactored into its own
      ; group handling system with more documentation
      (case group-type
        :group.filter (filter (:group.filter active-group) all-items)
        :group.collection (filter #(contains? (:item.groups %) active-group-id) all-items)))))

(r/reg-sub
  :groups.active-list
  :<- [:items.active]
  :<- [:groups.map-all]
  (fn [[active-item groups-map] _]
    (sort-by
      #(:group.name %)
      (vals (select-keys groups-map
                         (:item.groups active-item))))))

;;; ------
;;; OTHERS
;;; ------

(reg-keyword-sub :ui.pref.show)
(reg-keyword-sub :ui.sidebar.show)
(reg-keyword-sub :ui.pane-mid.show)
(reg-keyword-sub :ui.preview.show)

(reg-keyword-sub :pref.theme)
(reg-keyword-sub :pref.ui.font)
(reg-keyword-sub :pref.ui.sidebar.dark-theme)