(ns project-factual.handler.keyboard-shorccuts
  (:require [re-frame.core :as r]))

(def electron (js/require "electron"))
(def ipc (.-ipcRenderer electron))

(def rf-event
  {"ui.pref.show.toggle" [:ui.pref.toggle]
   "ui.sidebar.show.toggle" [:ui.sidebar.toggle]
   "ui.pane-mid.show.toggle" [:ui.pane-mid.toggle]
   "ui.preview.show.toggle" [:ui.preview.toggle]

   "items.new.mditem" [:item.new :item.markdown]
   "items.active.trash" [:item.trash]})

(r/reg-event-fx
  :init-ipc-handlers
  (fn [cofx]
    {:reg-ipc
     ["shortcut-event"
      (fn on-shortcut-event [event arg]
        ; Lookup eventid in the map and dispatch it
        ; The reason we are doing this is that IPC doesn't want to play
        ; nice with cljs data structures, which means we can't just pass
        ; a re-frame event from the main thread
        ; This will remain the system until a cleaner solution is found
        (let [ev (rf-event arg)]
          (if ev
            (r/dispatch ev)
            (.error js/console (str "ERRROR: no event id: " arg)))))]}))

(r/reg-fx
  :reg-ipc
  (fn [[channel func]]
    (.on ipc channel func)))