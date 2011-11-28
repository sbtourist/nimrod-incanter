(ns nimrod.incanter.server
 (:use
   [clojure.contrib.json :as json]
   [compojure.core :as http]
   [compojure.handler :as handler]
   [ring.adapter.jetty :as jetty]))

(http/defroutes test-routes
  (http/GET "/logs/1/alerts/test/history" [age]
    {:status 200 :body (json/json-str {:values [{:timestamp (Long/valueOf age) :alert "1" :tags ["tag:1"]} {:timestamp 2 :alert "2" :tags ["tag:2"]}]})})
  (http/GET "/logs/1/gauges/test/history" [age]
    {:status 200 :body (json/json-str {:values [{:timestamp (Long/valueOf age) :gauge 1 :tags ["tag:1"]} {:timestamp 2 :gauge 2 :tags ["tag:2"]}]})})
  (http/GET "/logs/1/counters/test/history" [age]
    {:status 200 :body (json/json-str {:values [{:timestamp (Long/valueOf age) :counter 1 :tags ["tag:1"]} {:timestamp 2 :counter 2 :tags ["tag:2"]}]})})
  (http/GET "/logs/1/timers/test/history" [age]
    {:status 200 :body (json/json-str {:values [{:timestamp (Long/valueOf age) :end 0 :timer 1 :tags ["tag:2"]} {:timestamp 2 :end 2 :timer 2 :tags ["tag:2"]}]})}))

(defn start-mock-server []
  (jetty/run-jetty (handler/api test-routes) {:port 8182 :join? false}))