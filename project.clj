(defproject
  boot-project
  "0.0.0-SNAPSHOT"
  :repositories
  [["clojars" {:url "https://repo.clojars.org/"}]
   ["maven-central" {:url "https://repo1.maven.org/maven2"}]]
  :dependencies
  [[org.clojure/clojure "1.8.0"]
   [org.clojure/clojurescript "1.9.229"]
   [cljsjs/react "15.3.1-0"]
   [cljsjs/nodejs-externs "1.0.4-1"]
   [cljsjs/codemirror "5.11.0-2"]
   [reagent "0.6.0"]
   [re-frame "0.8.0"]
   [re-com "1.0.0"]
   [re-frisk "0.3.1"]
   [onetom/boot-lein-generate "0.1.3" :scope "test"]
   [adzerk/boot-cljs "1.7.228-1" :scope "test"]
   [adzerk/boot-cljs-repl "0.3.3" :scope "test"]
   [adzerk/boot-reload "0.4.12" :scope "test"]
   [com.cemerick/piggieback "0.2.1" :scope "test"]
   [weasel "0.7.0" :scope "test"]
   [org.clojure/tools.nrepl "0.2.12" :scope "test"]]
  :source-paths
  ["src/cljs"]
  :resource-paths
  ["resources"])