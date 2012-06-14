(ns nimrod.incanter.core-test
  (:use
    [clojure.test]
    [nimrod.incanter.core]
    [nimrod.incanter.server]))

(start-mock-server)

(deftest test-alerts
  (let [dataset (from-nimrod "http://localhost:8182" (from-log "1" (read-alert :name "test" :metric "alert" :from 1 :to 2)))]
    (is (= [:column-names ["timestamp" "alert"]] (first dataset)))
    (is (= [:rows '({"alert" 1 "timestamp" 1} {"alert" 2 "timestamp" 2})] (second dataset)))))

(deftest test-alerts-with-tag
  (let [dataset (from-nimrod "http://localhost:8182" (from-log "1" (read-alert :name "test" :metric "alert" :from 1 :to 2 :tags ["tag"])))]
    (is (= [:column-names ["timestamp" "alert" "tag"]] (first dataset)))
    (is (= [:rows '({"alert" 1 "timestamp" 1 "tag" 1} {"alert" 2 "timestamp" 2 "tag" 2})] (second dataset)))))

(deftest test-gauges
  (let [dataset (from-nimrod "http://localhost:8182" (from-log "1" (read-gauge :name "test" :metric "gauge" :from 1 :to 2)))]
    (is (= [:column-names ["timestamp" "gauge"]] (first dataset)))
    (is (= [:rows '({"gauge" 1 "timestamp" 1} {"gauge" 2 "timestamp" 2})] (second dataset)))))

(deftest test-gauges-with-tag
  (let [dataset (from-nimrod "http://localhost:8182" (from-log "1" (read-gauge :name "test" :metric "gauge" :from 1 :to 2 :tags ["tag"])))]
    (is (= [:column-names ["timestamp" "gauge" "tag"]] (first dataset)))
    (is (= [:rows '({"gauge" 1 "timestamp" 1 "tag" 1} {"gauge" 2 "timestamp" 2 "tag" 2})] (second dataset)))))

(deftest test-counters
  (let [dataset (from-nimrod "http://localhost:8182" (from-log "1" (read-counter :name "test" :metric "counter" :from 1 :to 2)))]
    (is (= [:column-names ["timestamp" "counter"]] (first dataset)))
    (is (= [:rows '({"counter" 1 "timestamp" 1} {"counter" 2 "timestamp" 2})] (second dataset)))))

(deftest test-counters-with-tag
  (let [dataset (from-nimrod "http://localhost:8182" (from-log "1" (read-counter :name "test" :metric "counter" :from 1 :to 2 :tags ["tag"])))]
    (is (= [:column-names ["timestamp" "counter" "tag"]] (first dataset)))
    (is (= [:rows '({"counter" 1 "timestamp" 1 "tag" 1} {"counter" 2 "timestamp" 2 "tag" 2})] (second dataset)))))

(deftest test-timers
  (let [dataset (from-nimrod "http://localhost:8182" (from-log "1" (read-timer :name "test" :metric "timer" :from 1 :to 2)))]
    (is (= [:column-names ["timestamp" "timer"]] (first dataset)))
    (is (= [:rows '({"timer" 2 "timestamp" 2})] (second dataset)))))

(deftest test-timers-with-tag
  (let [dataset (from-nimrod "http://localhost:8182" (from-log "1" (read-timer :name "test" :metric "timer" :from 1 :to 2 :tags ["tag"])))]
    (is (= [:column-names ["timestamp" "timer" "tag"]] (first dataset)))
    (is (= [:rows '({"timer" 2 "timestamp" 2 "tag" 2})] (second dataset)))))

(deftest test-with-multiple-tags
  (let [dataset (from-nimrod "http://localhost:8182" (from-log "1" (read-gauge :name "test-with-tags" :metric "gauge" :from 1 :to 2 :tags ["tag" "tag2"])))]
    (is (= [:column-names ["timestamp" "gauge" "tag" "tag2"]] (first dataset)))
    (is (= [:rows '({"gauge" 1 "timestamp" 1 "tag" 1 "tag2" "1:1"} {"gauge" 2 "timestamp" 2 "tag" 2 "tag2" "2:2"})] (second dataset)))))
