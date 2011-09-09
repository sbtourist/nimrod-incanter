(defproject nimrod-incanter "0.1-SNAPSHOT"
 :description "Nimrod metrics analysis with Incanter."
 :dependencies [
                [org.clojure/clojure "1.2.1"]
                [org.clojure/clojure-contrib "1.2.0"]
                [clj-http "0.2.0"]
                [incanter/incanter-core "1.2.3"]
                [incanter/incanter-io "1.2.3"]
                [incanter/incanter-charts "1.2.3"]
                [incanter/incanter-processing "1.2.3"]
                [swingrepl "1.0.0-SNAPSHOT"]
                [jline "0.9.94"]
                ]
 :main nimrod.incanter.main
 )
