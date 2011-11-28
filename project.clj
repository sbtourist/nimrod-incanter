(defproject nimrod-incanter "0.1-SNAPSHOT"
 :description "Nimrod metrics analysis with Incanter."
 :dependencies [
                [org.clojure/clojure "1.2.1"]
                [org.clojure/clojure-contrib "1.2.0"]
                [clj-http "0.2.5"]
                [incanter/incanter-core "1.2.4"]
                [incanter/incanter-io "1.2.4"]
                [incanter/incanter-charts "1.2.4"]
                [incanter/incanter-processing "1.2.4"]
                [swingrepl "1.0.0-SNAPSHOT"]
                [jline "0.9.94"]]
 :dev-dependencies [
                    [compojure "0.6.5"]
                    [ring/ring-core "0.3.11"]
                    [ring/ring-jetty-adapter "0.3.11"]]
 :aot [nimrod.incanter.main]
 :main nimrod.incanter.main)
