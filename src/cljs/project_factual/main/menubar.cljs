(ns project-factual.main.menubar)

(def electron (js/require "electron"))
(def menu (.-Menu electron))
(def web-contents (.-webContents electron))
(def ipc (.-ipcMain electron))

(defonce seperator {:type "separator"})

(defn send-rf-event [eventid]
  (let [focused (.getFocusedWebContents web-contents)]
    (when focused
      (.send focused "shortcut-event" eventid))))

(defn normal-item [label shortcut eventid]
  {:label label
   :accelerator shortcut
   :click #(send-rf-event eventid)})

(defn role-item [label shortcut role]
  {:label label
   :accelerator shortcut
   :role role})

(def menu-sys
  {:label "Project Factual"
   :submenu
   [{:label "About Project Factual" :role "about"}
    seperator
    {:role "services" :submenu []}
    seperator
    (role-item "Hide Project Factual" "Cmd+H" "hide")
    (role-item "Hide Others" "Cmd+Shift+H" "hideothers")
    {:label "Show All" :role "unhide"}
    seperator
    (role-item "Quit" "Cmd+Q" "quit")]})

(def menu-file
  {:label "File"
   :submenu
   [(normal-item "New Item" "Cmd+N" "items.new.mditem")
    (normal-item "Delete Item" "Cmd+Backspace" "items.active.trash")]})

(def menu-edit
  {:label "Edit"
   :submenu
   [(role-item "Undo" "Cmd+Z" "undo")
    (role-item "Redo" "Cmd+Shift+Z" "redo")
    seperator
    (role-item "Cut" "Cmd+X" "cut")
    (role-item "Copy" "Cmd+C" "copy")
    (role-item "Paste" "Cmd+V" "paste")
    (role-item "Paste and Match Style" "Cmd+Shift+V" "pasteandmatchstyle")
    (role-item "Select All" "Cmd+A" "selectall")
    seperator
    {:label "Speech" :submenu [{:role "startspeaking"} {:role "stopspeaking"}]}]})

(def menu-view
  {:label "View"
   :submenu
   [(normal-item "Sidebar" "Cmd+1" "ui.sidebar.show.toggle")
    (normal-item "Items List" "Cmd+2" "ui.pane-mid.show.toggle")
    seperator
    (normal-item "Preview" "Cmd+P" "ui.preview.show.toggle")
    seperator
    (role-item "Reload" "Cmd+Alt+R" "reload")
    (role-item "Dev Tools" "Cmd+Alt+I" "toggledevtools")]})

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