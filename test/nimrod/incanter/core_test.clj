(ns nimrod.incanter.core-test
 (:use
   [clojure.test]
   [nimrod.incanter.core]
   [nimrod.incanter.server]
   )
 )

(start-mock-server)

(deftest test-alerts
  (let [dataset (from-nimrod "http://localhost:8182" (from-log "1" (read-alert "test" "alert")))]
    (is (= [:column-names ["timestamp" "alert"]] (first dataset)))
    (is (= [:rows '({"alert" 1, "timestamp" 1} {"alert" 2, "timestamp" 2})] (second dataset)))
    )
  )

(deftest test-gauges
  (let [dataset (from-nimrod "http://localhost:8182" (from-log "1" (read-gauge "test" "gauge")))]
    (is (= [:column-names ["timestamp" "gauge"]] (first dataset)))
    (is (= [:rows '({"gauge" 1, "timestamp" 1} {"gauge" 2, "timestamp" 2})] (second dataset)))
    )
  )

(deftest test-counters
  (let [dataset (from-nimrod "http://localhost:8182" (from-log "1" (read-counter "test" "counter")))]
    (is (= [:column-names ["timestamp" "counter"]] (first dataset)))
    (is (= [:rows '({"counter" 1, "timestamp" 1} {"counter" 2, "timestamp" 2})] (second dataset)))
    )
  )

(deftest test-timers
  (let [dataset (from-nimrod "http://localhost:8182" (from-log "1" (read-timer "test" "timer")))]
    (is (= [:column-names ["timestamp" "timer"]] (first dataset)))
    (is (= [:rows '({"timer" 2, "timestamp" 2})] (second dataset)))
    )
  )