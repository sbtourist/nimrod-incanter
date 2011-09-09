(ns nimrod.incanter.core
 (:use
   [clojure.contrib.json :as json]
   [clj-http.client :as http]
   [incanter.core :as incanter])
 (:refer-clojure :exclude [await get])
 )

(defn export-alerts [alerts value]
  (for [alert alerts] [(alert :timestamp) (Long. (alert (keyword value)))])
  )

(defn export-gauges [gauges value]
  (for [gauge gauges] [(gauge :timestamp) (gauge (keyword value))])
  )

(defn export-counters [counters value]
  (for [counter counters] [(counter :timestamp) (counter (keyword value))])
  )

(defn export-timers [timers value]
  (for [timer timers :when (> (timer :end) 0)] [(timer :timestamp) (timer (keyword value))])
  )

(defn export [url value export-fn]
    (let [response (http/get url)]
      (if (= 200 (response :status))
        (if-let [values ((json/read-json (response :body)) :values)]
          (incanter/dataset ["timestamp" value] (export-fn values value))
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

(defmacro read-alert [a value]
  `(let [alert# ~a
         url# (str ~'base "/logs/" ~'log "/alerts/" alert# "/history")]
     (export url# ~value export-alerts)
     )
  )

(defmacro read-gauge [g value]
  `(let [gauge# ~g
         url# (str ~'base "/logs/" ~'log "/gauges/" gauge# "/history")]
     (export url# ~value export-gauges)
     )
  )

(defmacro read-counter [c value]
  `(let [counter# ~c
         url# (str ~'base "/logs/" ~'log "/counters/" counter# "/history")]
     (export url# ~value export-counters)
     )
  )

(defmacro read-timer [t value]
  `(let [timer# ~t
         url# (str ~'base "/logs/" ~'log "/timers/" timer# "/history")]
     (export url# ~value export-timers)
     )
  )