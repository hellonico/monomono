(defproject monomono "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :main monomono.core
  :aot :all
  :uberjar-name "monomono.jar"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
  [org.clojure/clojure "1.8.0"]
  [selmer "1.11.1"]
  [clj-time "0.14.0"]
  [com.climate/claypoole "1.1.4"]
  [org.clojure/tools.cli "0.3.5"]
  [progrock "0.1.2"]
  ])
