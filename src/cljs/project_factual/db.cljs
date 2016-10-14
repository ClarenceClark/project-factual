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
              :name "Element 1"
              :content "This is a bunch of markdown text. Seriously. Trust me."}
           2 {:id 2
              :type "markdown"
              :name "Another element!"
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
              :name "I am running out of ideas"
              :content "I don't know what else to write"}}
   :active-item-id 1
   :groups []})