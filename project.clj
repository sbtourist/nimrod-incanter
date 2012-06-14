(defproject nimrod-incanter "0.1"
 :description "Nimrod metrics analysis with Incanter."
 :dependencies [[org.clojure/clojure "1.4.0"]
                [cheshire "4.0.0"]
                [clj-http "0.2.5"]
                [incanter/incanter-core "1.3.0"]
                [incanter/incanter-io "1.3.0"]
                [incanter/incanter-charts "1.3.0"]
                [incanter/incanter-processing "1.3.0"]
                [swingrepl "1.3.0"]
                [jline "0.9.94"]]
 :dev-dependencies [[compojure "1.0.0"]
                    [ring/ring-core "1.0.1"]
                    [ring/ring-jetty-adapter "1.0.1"]]
 :aot [nimrod.incanter.main]
 :main nimrod.incanter.main)
