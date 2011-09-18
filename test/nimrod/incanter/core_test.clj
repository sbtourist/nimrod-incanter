(ns nimrod.incanter.core-test
 (:use
   [clojure.test]
   [nimrod.incanter.core]
   [nimrod.incanter.server]
   )
 )

(start-mock-server)

(deftest test-alerts
  (let [dataset (from-nimrod "http://localhost:8182" (from-log "1" (read-alert "test" "alert" :no-tag)))]
    (is (= [:column-names ["timestamp" "alert"]] (first dataset)))
    (is (= [:rows '({"alert" 1, "timestamp" 1} {"alert" 2, "timestamp" 2})] (second dataset)))
    )
  )

(deftest test-alerts-with-tag
  (let [dataset (from-nimrod "http://localhost:8182" (from-log "1" (read-alert "test" "alert" "tag")))]
    (is (= [:column-names ["timestamp" "alert" "tag"]] (first dataset)))
    (is (= [:rows '({"alert" 1, "timestamp" 1 "tag" 1} {"alert" 2, "timestamp" 2 "tag" 2})] (second dataset)))
    )
  )

(deftest test-gauges
  (let [dataset (from-nimrod "http://localhost:8182" (from-log "1" (read-gauge "test" "gauge" :no-tag)))]
    (is (= [:column-names ["timestamp" "gauge"]] (first dataset)))
    (is (= [:rows '({"gauge" 1, "timestamp" 1} {"gauge" 2, "timestamp" 2})] (second dataset)))
    )
  )

(deftest test-gauges-with-tags
  (let [dataset (from-nimrod "http://localhost:8182" (from-log "1" (read-gauge "test" "gauge" "tag")))]
    (is (= [:column-names ["timestamp" "gauge" "tag"]] (first dataset)))
    (is (= [:rows '({"gauge" 1, "timestamp" 1 "tag" 1} {"gauge" 2, "timestamp" 2 "tag" 2})] (second dataset)))
    )
  )

(deftest test-counters
  (let [dataset (from-nimrod "http://localhost:8182" (from-log "1" (read-counter "test" "counter" :no-tag)))]
    (is (= [:column-names ["timestamp" "counter"]] (first dataset)))
    (is (= [:rows '({"counter" 1, "timestamp" 1} {"counter" 2, "timestamp" 2})] (second dataset)))
    )
  )

(deftest test-counters-with-tags
  (let [dataset (from-nimrod "http://localhost:8182" (from-log "1" (read-counter "test" "counter" "tag")))]
    (is (= [:column-names ["timestamp" "counter" "tag"]] (first dataset)))
    (is (= [:rows '({"counter" 1, "timestamp" 1 "tag" 1} {"counter" 2, "timestamp" 2 "tag" 2})] (second dataset)))
    )
  )

(deftest test-timers
  (let [dataset (from-nimrod "http://localhost:8182" (from-log "1" (read-timer "test" "timer" :no-tag)))]
    (is (= [:column-names ["timestamp" "timer"]] (first dataset)))
    (is (= [:rows '({"timer" 2, "timestamp" 2})] (second dataset)))
    )
  )

(deftest test-timers-with-tags
  (let [dataset (from-nimrod "http://localhost:8182" (from-log "1" (read-timer "test" "timer" "tag")))]
    (is (= [:column-names ["timestamp" "timer" "tag"]] (first dataset)))
    (is (= [:rows '({"timer" 2, "timestamp" 2 "tag" 2})] (second dataset)))
    )
  )