(ns nimrod.incanter.core
  (:require
    [clojure.string :as string :only [split]]
    [cheshire.core :as json]
    [clj-http.client :as http :only [get]]
    [incanter.core :as incanter])
  (:refer-clojure :exclude [await get]))

(defn- number-or-string [v]
  (when (seq v) (if (re-matches #"\d+" v) (Long. v) v)))

(defn- extract-tags [tags all-tags]
  (if (and (seq tags) (seq all-tags))
    (let [all-tags-map (apply conj {} (map #(string/split %1 #":") all-tags))]
      (into [] (for [tag tags] (number-or-string (all-tags-map tag)))))
    [nil]))

(defn export-alerts [alerts value tags]
  (for [alert alerts] (remove nil? (apply conj [(alert :timestamp) (number-or-string (alert (keyword value)))] (extract-tags tags (alert :tags))))))

(defn export-gauges [gauges value tags]
  (for [gauge gauges] (remove nil? (apply conj [(gauge :timestamp) (gauge (keyword value))] (extract-tags tags (gauge :tags))))))

(defn export-counters [counters value tags]
  (for [counter counters] (remove nil? (apply conj [(counter :timestamp) (counter (keyword value))] (extract-tags tags (counter :tags))))))

(defn export-timers [timers value tags]
  (for [timer timers :when (> (timer :end) 0)] (remove nil? (apply conj [(timer :timestamp) (timer (keyword value))] (extract-tags tags (timer :tags))))))

(defn export [url value tags export-fn]
  (let [response (http/get url)]
    (if (= 200 (response :status))
      (if-let [values ((json/parse-string (response :body) true) :values)]
        (if (seq tags)
          (incanter/dataset (apply conj ["timestamp" value] tags) (export-fn values value tags))
          (incanter/dataset ["timestamp" value] (export-fn values value nil)))
        (throw (RuntimeException. (str "Unable to export metric " value " from " url))))
      (throw (RuntimeException. (str "Unable to export metric " value " from " url))))))

(defmacro from-nimrod [u & forms]
  `(let [~'base ~u]
    (do ~@forms)))

(defmacro from-log [l & forms]
  `(let [~'log ~l]
    (do ~@forms)))

(defmacro read-alert [& {a :name value :metric from :from to :to tags :tags}]
  `(let [alert# ~a
    url# (str ~'base "/logs/" ~'log "/alerts/" alert# "/history?from=" ~from "&to=" ~to)]
    (export url# ~value ~tags export-alerts)))

(defmacro read-gauge [& {g :name value :metric from :from to :to tags :tags}]
  `(let [gauge# ~g
    url# (str ~'base "/logs/" ~'log "/gauges/" gauge# "/history?from=" ~from "&to=" ~to)]
    (export url# ~value ~tags export-gauges)))

(defmacro read-counter [& {c :name value :metric from :from to :to tags :tags}]
  `(let [counter# ~c
    url# (str ~'base "/logs/" ~'log "/counters/" counter# "/history?from=" ~from "&to=" ~to)]
    (export url# ~value ~tags export-counters)))

(defmacro read-timer [& {t :name value :metric from :from to :to tags :tags}]
  `(let [timer# ~t
    url# (str ~'base "/logs/" ~'log "/timers/" timer# "/history?from=" ~from "&to=" ~to)]
    (export url# ~value ~tags export-timers)))