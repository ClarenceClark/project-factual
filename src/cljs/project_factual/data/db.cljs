(ns project-factual.data.db
  (:require [cljs.spec :as s]))

;; DATABASE SPEC

(s/def ::item.id int?)
(s/def ::item.type keyword?)
(s/def ::item.content string?)
(s/def ::item.item (s/keys :req-un [::item.id ::item.type ::item.content]))
(s/def ::items (s/map-of ::item.id ::item.item))

(s/def ::group.id int?)
(s/def ::group.type keyword?)
(s/def ::group.name string?)
(s/def ::group.group (s/keys :req-un [::group.id ::group.type ::group.name]))
(s/def ::groups (s/map-of ::group.id ::group.group))

(s/def ::db (s/keys :req-un [::items ::groups ::active-item-id ::active-group-id ::editor]))

;; ----------
;; MOCK DATA
;; ----------

(defn multiline-string [& strings]
  (clojure.string/join "\n" strings))

(def testing-database
  {:items {1 {:item.id 1
              :item.type :item.markdown
              :item.content "First line\nSecond line"}
           2 {:item.id 2
              :item.type :item.markdown
              :item.content (multiline-string "# Mary had a little lamb"
                                         " "
                                         "Mary had a little lamb  "
                                         "Little lamb  "
                                         "Little lamb  "
                                         " "
                                         "Mary had a little lamb  "
                                         "That was as white as snow")}
           3 {:item.id 3
              :item.type :item.markdown
              :item.content "Header only test"}}
   :groups {1 {:group.id 1
               :group.name "All"
               :group.type :group.filter
               :group.filter (constantly true)}
            2 {:group.id 2
               :group.name "Default"
               :group.type :group.col
               :group.elements [1 2 3]}}
   :active-item-id 1
   :active-group-id 1
   :sidebar-active false
   :screen-dim true
   :editor nil})