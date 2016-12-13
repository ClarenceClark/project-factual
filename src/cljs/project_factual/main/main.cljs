(ns project-factual.main.main)

(def electron (js/require "electron"))

(def ipc-main (.-ipcMain electron))
(def app (.-app electron))
(def browser-window (.-BrowserWindow electron))
(def menu (.-Menu electron))

(goog-define dev? true)

; Keep ref to main window so it doesn't get GC'ed
(def main-win (atom nil))

(defn lauch-win! []
  (let []))

(defn init []
  (.on app "ready" lauch-win!))