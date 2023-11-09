(ns clicker.core
  (:require [reitit.core :as r]
            [reitit.ring :as ring]
            [ring.adapter.jetty9 :refer [run-jetty]])
  (:import [org.eclipse.jetty.server Server]))


(defn home []
  {:status 200
   :body "OK :)"})

(def app
  (ring/ring-handler
   (ring/router
    ["/api"
     ["/ping" {:get (fn [request]
                      (println request)
                      {:status 200 :body "pong"})}]])))

r/router

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defonce server (ref nil))

(defn restart-server! []
  (dosync
   (when-let [s @server]
     (.stop ^Server s))
   (ref-set server (run-jetty app {:join? false :port 8080}))))

(comment
  (restart-server!))
