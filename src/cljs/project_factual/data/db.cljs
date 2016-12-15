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

(s/def ::items.active-id int?)
(s/def ::groups.active-id int?)

(s/def ::ui.sidebar.show boolean?)
(s/def ::ui.pane-mid.show boolean?)
(s/def ::ui.preview.show boolean?)
(s/def ::ui.pref.show boolean?)

(s/def ::db (s/keys :req-un [::items ::groups
                             ::items.active-id ::groups.active-id

                             ::ui.sidebar.show
                             ::ui.pane-mid.show
                             ::ui.preview.show
                             ::ui.pref.show

                             ::editor]))

;; ----------
;; MOCK DATA
;; ----------

(def group-all {:group.id -1 ; TODO get rid of magical constant
                :group.name "All items"
                :group.type :group.filter
                :group.filter (constantly true)
                :group.icon "icon-docs"})

(defn multiline-string [& strings]
  (clojure.string/join "\n" strings))

(def testing-database
  {:items {1 {:item.id 1
              :item.type :item.markdown
              :item.groups #{1}
              :item.content "First line\nSecond line"}
           2 {:item.id 2
              :item.type :item.markdown
              :item.groups #{1 2}
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
              :item.groups #{1 2}
              :item.content (str "# Intro\nGo ahead, play around with the editor! "
                                 "Be sure to check out **bold** and *italic* styling, "
                                 "or even [links](http://google.com). "
                                 "You can type the Markdown syntax, use the toolbar, "
                                 "or use shortcuts like `cmd-b` or `ctrl-b`.\n\n"
                                 "## Lists\nUnordered lists can be started using "
                                 "the toolbar or by typing `* `, `- `, or `+ `. "
                                 "Ordered lists can be started by typing `1. `.\n\n"
                                 "#### Unordered\n"
                                 "* Lists are a piece of cake\n"
                                 "* They even auto continue as you type\n"
                                 "* A double enter will end them\n"
                                 "* Tabs and shift-tabs work too\n\n"
                                 "#### Ordered\n"
                                 "1. Numbered lists...\n"
                                 "2. ...work too!\n"
                                 "3. \n\n## What about images?"
                                 "\n![Yes](http://i.imgur.com/sZlktY7.png)")}}

   :groups {-1 group-all ; TODO get rid of magical constant
            1 {:group.id 1
               :group.name "Default"
               :group.type :group.collection}
            2 {:group.id 2
               :group.name "Number 2"
               :group.type :group.collection}
            3 {:group.id 3
               :group.name "Number 3"
               :group.type :group.collection}
            4 {:group.id 4
               :group.name "number 4"
               :group.type :group.collection}}

   :items.active-id 1
   :groups.active-id -1

   :ui.sidebar.show false
   :ui.pane-mid.show false
   :ui.preview.show false
   :ui.pref.show false

   :pref.theme "theme-light"

   :editor nil})