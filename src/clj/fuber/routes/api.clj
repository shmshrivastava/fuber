(ns fuber.routes.api
  (:require
   [ring.util.http-response :refer [ok created no-content]]
   [reitit.swagger :as swagger]
   [reitit.coercion.schema]
   [fuber.models.cabs :as cabs]
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
                :handler (fn [_] (ok (cabs/get-cabs)))}}]
     ["/nearest" {:get {:coercion reitit.coercion.schema/coercion
                        :summary "Get a nearest cab from a lat long"
                        :parameters {:query {:lat s/Str :long s/Str}}
                        :handler
                        (fn [{{{:keys [lat long]} :query}
                              :parameters}]
                          (if-let [cab (cabs/get-nearest-cab lat long)]
                            (ok cab)
                            (no-content)))}}]]]])