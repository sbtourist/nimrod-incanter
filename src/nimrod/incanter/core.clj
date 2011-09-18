(ns nimrod.incanter.core
 (:use
   [clojure.string :as string :only [split]]
   [clojure.contrib.json :as json]
   [clj-http.client :as http]
   [incanter.core :as incanter])
 (:refer-clojure :exclude [await get])
 )

(defn- number-or-string [v]
  (if (re-matches #"\d+" v) (Long. v) v)
  )

(defn- extract-tag [all-tags tag]
  (when (and (seq all-tags) (not (nil? tag)))
    (number-or-string 
      (first 
        (remove nil? 
          (map #(let [tag-and-value (string/split %1 #":")] (when (= tag (first tag-and-value)) (second tag-and-value))) all-tags)
          )
        )
      )
    )
  )

(defn export-alerts [alerts value tag]
  (for [alert alerts] [(alert :timestamp) (number-or-string (alert (keyword value))) (extract-tag (alert :tags) tag)])
  )

(defn export-gauges [gauges value tag]
  (for [gauge gauges] [(gauge :timestamp) (gauge (keyword value)) (extract-tag (gauge :tags) tag)])
  )

(defn export-counters [counters value tag]
  (for [counter counters] [(counter :timestamp) (counter (keyword value)) (extract-tag (counter :tags) tag)])
  )

(defn export-timers [timers value tag]
  (for [timer timers :when (> (timer :end) 0)] [(timer :timestamp) (timer (keyword value)) (extract-tag (timer :tags) tag)])
  )

(defn export [url value tag export-fn]
    (let [response (http/get url)]
      (if (= 200 (response :status))
        (if-let [values ((json/read-json (response :body)) :values)]
          (if (nil? tag)
            (incanter/dataset ["timestamp" value] (export-fn values value nil))
            (incanter/dataset ["timestamp" value tag] (export-fn values value tag))
            )
          (throw (RuntimeException. (str "Unable to export metric " value " from " url)))
          )
        (throw (RuntimeException. (str "Unable to export metric " value " from " url)))
        )
      )
    )

(defmacro from-nimrod [u & forms]
  `(let [~'base ~u]
     (do ~@forms)
     )
  )

(defmacro from-log [l & forms]
  `(let [~'log ~l]
     (do ~@forms)
     )
  )

(defmacro read-alert [a value tag]
  `(let [alert# ~a
         url# (str ~'base "/logs/" ~'log "/alerts/" alert# "/history")]
     (export url# ~value ~tag export-alerts)
     )
  )

(defmacro read-gauge [g value tag]
  `(let [gauge# ~g
         url# (str ~'base "/logs/" ~'log "/gauges/" gauge# "/history")]
     (export url# ~value ~tag export-gauges)
     )
  )

(defmacro read-counter [c value tag]
  `(let [counter# ~c
         url# (str ~'base "/logs/" ~'log "/counters/" counter# "/history")]
     (export url# ~value ~tag export-counters)
     )
  )

(defmacro read-timer [t value tag]
  `(let [timer# ~t
         url# (str ~'base "/logs/" ~'log "/timers/" timer# "/history")]
     (export url# ~value ~tag export-timers)
     )
  )