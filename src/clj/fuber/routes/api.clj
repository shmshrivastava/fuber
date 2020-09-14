(ns fuber.routes.api
  (:require
   [ring.util.http-response :refer [ok created]]
   [reitit.swagger :as swagger]
   [reitit.coercion.schema]
   [schema.core :as s]))

(defn api-routes []
  [["/api"
    ["/swagger.json"
     {:get {:no-doc true
            :swagger
            {:info
             {:title "Fuber APIs"}}
            :handler (swagger/create-swagger-handler)}}]]
   ["/api"
    ["/cabs"
     ["" {:get {:coercion reitit.coercion.schema/coercion
                :summary "Get a list of all cabs"
                :handler (fn [_] (ok [1]))}}]]]])