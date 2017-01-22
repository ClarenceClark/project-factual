(defproject
  boot-project
  "0.0.0-SNAPSHOT"
  :repositories
  [["clojars" {:url "https://repo.clojars.org/"}]
   ["maven-central" {:url "https://repo1.maven.org/maven2"}]]
  :dependencies
  [[org.clojure/clojure "1.8.0"]
   [org.clojure/clojurescript "1.9.293"]
   [cljsjs/react "15.3.1-0"]
   [cljsjs/nodejs-externs "1.0.4-1"]
   [cljsjs/codemirror "5.21.0-1"]
   [reagent "0.6.0"]
   [re-frame "0.9.1"]
   [re-com "1.3.0"]
   [re-frisk "0.3.2"]
   [binaryage/devtools "0.8.3"]
   [onetom/boot-lein-generate "0.1.3" :scope "test"]
   [deraen/boot-sass "0.3.0" :scope "test"]
   [org.slf4j/slf4j-nop "1.7.22" :scope "test"]
   [adzerk/boot-cljs "1.7.228-2" :scope "test"]
   [adzerk/boot-cljs-repl "0.3.3" :scope "test"]
   [adzerk/boot-reload "0.5.0" :scope "test"]
   [com.cemerick/piggieback "0.2.1" :scope "test"]
   [weasel "0.7.0" :scope "test"]
   [org.clojure/tools.nrepl "0.2.12" :scope "test"]]
  :source-paths
  ["src/cljs"]
  :resource-paths
  ["resources"])