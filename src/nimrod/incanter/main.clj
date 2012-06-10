(ns nimrod.incanter.main
  (:use [nimrod.incanter.core] [incanter core stats charts datasets])
  (:refer-clojure :exclude [await get])
  (:gen-class))

(defn -main [& args]
  (if-let [command (first args)]
    (binding [*ns* (the-ns 'nimrod.incanter.main)]
      (load-file command))))