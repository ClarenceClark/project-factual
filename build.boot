(set-env!
  :source-paths #{"src/cljs"}
  :resource-paths #{"resources"}
  :dependencies '[[org.clojure/clojure "1.8.0"]
                  [org.clojure/clojurescript "1.9.229"]
                  [cljsjs/react "15.3.1-0"]
                  [cljsjs/nodejs-externs "1.0.4-1"]
                  [cljsjs/codemirror "5.11.0-2"]
                  [reagent "0.6.0"]
                  [re-frame "0.8.0"]
                  [re-com "1.0.0"]

                  ; Dev deps
                  [re-frisk "0.3.1"]
                  [binaryage/devtools "0.8.3"]

                  ; Boot deps

                  ; Generate lein
                  [onetom/boot-lein-generate "0.1.3" :scope "test"]

                  ; Build
                  [adzerk/boot-cljs "1.7.228-1" :scope "test"]
                  [adzerk/boot-cljs-repl "0.3.3" :scope "test"]
                  [adzerk/boot-reload "0.4.12" :scope "test"]
                  [com.cemerick/piggieback "0.2.1" :scope "test"]
                  [weasel "0.7.0" :scope "test"]
                  [org.clojure/tools.nrepl "0.2.12" :scope "test"]])

(deftask generate-lein []
  (require '[boot.lein])
  ((resolve 'boot.lein/generate)))

(require
  '[adzerk.boot-cljs :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
  '[adzerk.boot-reload :refer [reload]])

(deftask prod-build []
  (comp (cljs :ids #{"main"}
              :optimizations :simple)
        (cljs :ids #{"renderer"}
              :optimizations :advanced)))

(deftask dev-build []
  (comp
    ; Watch fs
    (watch)

    ; Inject REPL and reloading code into renderer build
    (cljs-repl :ids #{"renderer"}
               :nrepl-opts {:port 9001})

    ; Live-reload JS for the renderer
    (reload :ids #{"renderer"}
            :ws-host "localhost"
            :on-jsload 'project-factual.core/main)

    ; Compile renderer
    (cljs :ids #{"renderer"}
          :compiler-options {:preloads '[devtools.preload]})

    ;; Compile JS for main process
    ;; path.resolve(".") which is used in CLJS's node shim
    ;; returns the directory `electron` was invoked in and
    ;; not the directory our main.js file is in.
    ;; Because of this we need to override the compilers `:asset-path option`
    ;; See http://dev.clojure.org/jira/browse/CLJS-1444 for details.
    (cljs :ids #{"main"}
          :compiler-options {:asset-path "target/main.out"
                             :closure-defines {'project-factual.main.main/dev? true}})
    (target)))