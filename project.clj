(defproject project-factual "0.1.0-alpha1"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "GPLv3"
            :url "https://www.gnu.org/licenses/gpl-3.0.en.html"}

  :source-paths ["src/cljs"]

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.229"]
                 [cljsjs/react "15.3.1-0"]
                 [cljsjs/nodejs-externs "1.0.4-1"]
                 [cljsjs/codemirror "5.11.0-2"]
                 [reagent "0.6.0"]
                 [re-frame "0.8.0"]
                 [com.rpl/specter "0.11.2"]]

  :plugins [[lein-cljsbuild "1.1.3"]]

  :min-lein-version "2.5.3"

  :cljsbuild {:builds {:app {:source-paths ["src/cljs"]
                             :compiler {:output-to     "app/js/p/app.js"
                                        :output-dir    "app/js/p/out"
                                        :asset-path    "js/p/out"
                                        :optimizations :none
                                        :pretty-print  true
                                        :cache-analysis true}}}}

  :clean-targets ^{:protect false} [:target-path "out" "app/js/p"]

  :figwheel {:css-dirs ["app/css"]}

  :profiles {:dev {:cljsbuild {:builds {:app {:source-paths ["env/dev/cljs"]
                                              :compiler {:source-map true
                                                         :main       "project-factual.dev"
                                                         :verbose true}
                                              :figwheel {:on-jsload "project-factual.core/mount-root"}}}}
                   :source-paths ["env/dev/cljs"]
                   :dependencies [[figwheel-sidecar "0.5.7"]]
                   :plugins [[lein-ancient "0.6.8"]]}
                             ;[lein-kibit "0.1.2"]
                             ;[lein-cljfmt "0.4.1"]
                             ;[lein-figwheel "0.5.0-6"]]}

             :production {:cljsbuild {:builds {:app {:compiler {:optimizations :advanced
                                                                :main          "project-factual.prod"
                                                                :parallel-build true
                                                                :cache-analysis false
                                                                :closure-defines {"goog.DEBUG" false}
                                                                :externs ["externs/misc.js"]
                                                                :pretty-print false}
                                                     :source-paths ["env/prod/cljs"]}}}}})

