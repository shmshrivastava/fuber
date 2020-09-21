(ns fuber.routes.home
  (:require
   [fuber.layout :as layout]
   [clojure.java.io :as io]
   [fuber.middleware :as middleware]
   [ring.util.response]
   [ring.util.http-response :as response]))

(defn home-page [request]
  (layout/render request "home.html" {}))


(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]
    :no-doc true}
   ["/" {:get home-page}]])

