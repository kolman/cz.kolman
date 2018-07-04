(ns cz.kolman.ring-handler
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :refer (content-type resource-response)]
            [puppetlabs.ring-middleware.core :refer [wrap-proxy]]
            [clojure.java.io :as io]))

(defroutes app-routes
  (route/resources "/" {:root "public"})
  ;; Deliver index.html for all client-side routes
  (GET "/*" [] (-> (resource-response "index.html" {:root "public"})
                   (content-type "text/html; charset=utf-8")))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-defaults site-defaults)
      ;; Proxy to AWS lambda backend
      (wrap-proxy "/api" "https://35h973s33e.execute-api.us-east-1.amazonaws.com/dev")
      ))

