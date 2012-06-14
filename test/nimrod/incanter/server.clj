(ns nimrod.incanter.server
  (:require
    [cheshire.core :as json]
    [compojure.core :as http]
    [compojure.handler :as handler]
    [ring.adapter.jetty :as jetty]))

(http/defroutes test-routes
  (http/GET "/logs/1/alerts/test/history" [from to]
    {:status 200 :body (json/generate-string {:values [{:timestamp (Long/valueOf from) :alert "1" :tags ["tag:1"]} {:timestamp (Long/valueOf to) :alert "2" :tags ["tag:2"]}]})})
  (http/GET "/logs/1/gauges/test/history" [from to]
    {:status 200 :body (json/generate-string {:values [{:timestamp (Long/valueOf from) :gauge 1 :tags ["tag:1"]} {:timestamp (Long/valueOf to) :gauge 2 :tags ["tag:2"]}]})})
  (http/GET "/logs/1/counters/test/history" [from to]
    {:status 200 :body (json/generate-string {:values [{:timestamp (Long/valueOf from) :counter 1 :tags ["tag:1"]} {:timestamp (Long/valueOf to) :counter 2 :tags ["tag:2"]}]})})
  (http/GET "/logs/1/timers/test/history" [from to]
    {:status 200 :body (json/generate-string {:values [{:timestamp (Long/valueOf from) :end 0 :timer 1 :tags ["tag:2"]} {:timestamp (Long/valueOf to) :end 2 :timer 2 :tags ["tag:2"]}]})})
  (http/GET "/logs/1/gauges/test-with-tags/history" [from to]
    {:status 200 :body (json/generate-string {:values [{:timestamp (Long/valueOf from) :gauge 1 :tags ["tag:1" "tag2:1:1"]} {:timestamp (Long/valueOf to) :gauge 2 :tags ["tag:2" "tag2:2:2"]}]})}))

(defn start-mock-server []
  (jetty/run-jetty (handler/api test-routes) {:port 8182 :join? false}))