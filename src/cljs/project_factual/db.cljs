(ns project-factual.db
  (:require [cljs.spec :as s]))

;; DATABASE SPEC

;; ITEMS

;; TODO: write specs

;; ----------
;; MOCK DATA
;; ----------

(defn multiline-string [& strings]
  (clojure.string/join "\n" strings))

(defn testing-database []
  {:items {1 {:id 1
              :type "markdown"
              :content "First line\nSecond line"}
           2 {:id 2
              :type "markdown"
              :content (multiline-string "# Mary had a little lamb"
                                         " "
                                         "Mary had a little lamb  "
                                         "Little lamb  "
                                         "Little lamb  "
                                         " "
                                         "Mary had a little lamb  "
                                         "That was as white as snow")}
           3 {:id 3
              :type "markdown"
              :content "Header only test"}}
   :groups {1 {:id 1
               :name "Default"
               :elements [1 2 3]}}
   :active-item-id 1
   :active-group-id 1
   :editor nil})