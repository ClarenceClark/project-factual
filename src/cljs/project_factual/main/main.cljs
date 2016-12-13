(ns project-factual.main.main
  (:require [project-factual.main.menubar :as menubar]))

(def electron (js/require "electron"))

(def ipc-main (.-ipcMain electron))
(def app (.-app electron))
(def browser-window (.-BrowserWindow electron))
(def menu (.-Menu electron))

(goog-define dev? false)

; Keep ref to main window so it doesn't get GC'ed
(def main-win (atom nil))

(defn load-main-page [window]
  (if dev?
    (.loadURL window (str "file://" js/__dirname "/../../../index.html"))
    (.loadURL window (str "file://" js/__dirname "/index.html"))))

(defn lauch-win! []
  (let [win (browser-window. (clj->js {:height 750
                                       :width 1200
                                       :title "Project Factual"}))]
    (.setApplicationMenu menu menubar/built-menu)
    (reset! main-win win)
    (load-main-page win)
    (if dev? (.openDevTools win))
    (.on win "closed" #(reset! main-win nil))))

(defn init []
  (set! *main-cli-fn* (fn [] nil))
  (.on app "ready" lauch-win!)
  (.on app "window-all-closed" #(.quit app)))