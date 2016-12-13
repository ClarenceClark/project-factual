(ns project-factual.main.menubar)

(def menu (.-Menu (js/require "electron")))

(defonce seperator {:type "separator"})

(def menu-sys
  {:label "Project Factual"
   :submenu
   [{:label "About Project Factual" :role "about"}
    seperator
    {:role "services" :submenu []}
    seperator
    {:label "Hide Project Factual" :accelerator "Cmd+H" :role "hide"}
    {:label "Hide Others" :accelerator "Cmd+Shift+H" :role "hideothers"}
    {:label "Show All" :role "unhide"}
    seperator
    {:label "Quit" :accelerator "Cmd+Q" :role "quit"}]})

(def menu-file
  {:label "File"
   :submenu
   [{:label "New Item" :accelerator "Cmd+N"}
    {:label "Delete Item" :accelerator "Cmd+Backspace"}]})

(def menu-edit
  {:label "Edit"
   :submenu
   [{:label "Undo" :accelerator "Cmd+Z" :role "undo"}
    {:label "Redo" :accelerator "Cmd+Shift+Z" :role "redo"}
    seperator
    {:label "Cut" :accelerator "Cmd+X" :role "cut"}
    {:label "Copy" :accelerator "Cmd+C" :role "copy"}
    {:label "Paste" :accelerator "Cmd+V" :role "paste"}
    {:label "Paste and Match Style" :accelerator "Cmd+Shift+V" :role "pasteandmatchstyle"}
    {:label "Select All" :accelerator "Cmd+A" :role "selectall"}
    seperator
    {:label "Speech" :submenu [{:role "startspeaking"} {:role "stopspeaking"}]}]})

(def menu-view
  {:label "View"
   :submenu
   [{:label "Sidebar" :accelerator "Cmd+1"}
    {:label "Items List" :accelerator "Cmd+2"}
    {:label "Editor Toolbar" :accelerator "Cmd+3"}
    seperator
    {:label "Reload" :accelerator "Cmd+Alt+R" :role "reload"}
    {:label "Toggle Developer Tools" :accelerator "Cmd+Alt+I" :role "toggledevtools"}]})

(def menu-format
  {:label "Format"
   :submenu
   [{:label "Bold" :accelerator "Cmd+B"}
    {:label "Italic" :accelerator "Cmd+I"}
    {:label "Strikethrough" :accelerator "Cmd+/"}
    {:label "Inline Code" :accelerator "Cmd+."}
    seperator
    {:label "Heading 1" :accelerator "Cmd+Shift+1"}
    {:label "Heading 2" :accelerator "Cmd+Shift+2"}
    {:label "Heading 3" :accelerator "Cmd+Shift+3"}
    {:label "Heading 4" :accelerator "Cmd+Shift+4"}
    {:label "Heading 5" :accelerator "Cmd+Shift+5"}
    {:label "Heading 6" :accelerator "Cmd+Shift+6"}
    seperator
    {:label "Ordered List" :accelerator "Cmd+L"}
    {:label "Unordered List" :accelerator "Cmd+Shift+L"}
    {:label "Todo List" :accelerator "Cmd+Shift+T"}
    {:label "Quote Block" :accelerator "Cmd+Shift+Q"}]})

(def menu-window
  {:label "Window"
   :submenu
   [{:label "Close" :accelerator "Cmd+W" :role "close"}
    {:label "Minimise" :accelerator "Cmd+M" :role "minimize"}
    seperator
    {:label "Bring All to Front" :role "front"}]})

(def menu-help {:role "help" :submenu []})

(def menu-template [menu-sys menu-file menu-edit menu-view menu-format menu-window menu-help])

(def built-menu (.buildFromTemplate menu (clj->js menu-template)))