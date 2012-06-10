(ns nimrod.incanter.core
 (:require
   [clojure.string :as string :only [split]]
   [cheshire.core :as json]
   [clj-http.client :as http :only [get]]
   [incanter.core :as incanter])
 (:refer-clojure :exclude [await get]))

(defn- number-or-string [v]
  (if (re-matches #"\d+" v) (Long. v) v))

(defn- extract-tag [tag all-tags]
  (when (and (seq all-tags) (not (nil? tag)))
    (number-or-string 
      (first 
        (remove nil? 
          (map #(let [tag-and-value (string/split %1 #":")] (when (= tag (first tag-and-value)) (second tag-and-value))) all-tags))))))

(defn export-alerts [alerts value tag]
  (for [alert alerts] [(alert :timestamp) (number-or-string (alert (keyword value))) (extract-tag tag (alert :tags))]))

(defn export-gauges [gauges value tag]
  (for [gauge gauges] [(gauge :timestamp) (gauge (keyword value)) (extract-tag tag (gauge :tags))]))

(defn export-counters [counters value tag]
  (for [counter counters] [(counter :timestamp) (counter (keyword value)) (extract-tag tag (counter :tags))]))

(defn export-timers [timers value tag]
  (for [timer timers :when (> (timer :end) 0)] [(timer :timestamp) (timer (keyword value)) (extract-tag tag (timer :tags))]))

(defn export [url value tag export-fn]
  (let [response (http/get url)]
    (if (= 200 (response :status))
      (if-let [values ((json/parse-string (response :body) true) :values)]
        (if (= :no-tag tag)
          (incanter/dataset ["timestamp" value] (export-fn values value nil))
          (incanter/dataset ["timestamp" value tag] (export-fn values value tag)))
        (throw (RuntimeException. (str "Unable to export metric " value " from " url))))
      (throw (RuntimeException. (str "Unable to export metric " value " from " url))))))

(defmacro from-nimrod [u & forms]
  `(let [~'base ~u]
     (do ~@forms)))

(defmacro from-log [l & forms]
  `(let [~'log ~l]
     (do ~@forms)))

(defmacro read-alert [& {a :name value :metric from :from to :to tag :tag}]
  `(let [alert# ~a
         url# (str ~'base "/logs/" ~'log "/alerts/" alert# "/history?from=" ~from "&to=" ~to)]
     (export url# ~value ~tag export-alerts)))

(defmacro read-gauge [& {g :name value :metric from :from to :to tag :tag}]
  `(let [gauge# ~g
         url# (str ~'base "/logs/" ~'log "/gauges/" gauge# "/history?from=" ~from "&to=" ~to)]
     (export url# ~value ~tag export-gauges)))

(defmacro read-counter [& {c :name value :metric from :from to :to tag :tag}]
  `(let [counter# ~c
         url# (str ~'base "/logs/" ~'log "/counters/" counter# "/history?from=" ~from "&to=" ~to)]
     (export url# ~value ~tag export-counters)))

(defmacro read-timer [& {t :name value :metric from :from to :to tag :tag}]
  `(let [timer# ~t
         url# (str ~'base "/logs/" ~'log "/timers/" timer# "/history?from=" ~from "&to=" ~to)]
     (export url# ~value ~tag export-timers)))