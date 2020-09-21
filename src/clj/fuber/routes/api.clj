(ns fuber.routes.api
  (:require
   [ring.util.http-response :refer [ok created no-content]]
   [reitit.swagger :as swagger]
   [reitit.coercion.schema]
   [fuber.models.cabs :as cabs]
   [fuber.rides-manager :as rides]
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
    ["/cabs" {:get {:coercion reitit.coercion.schema/coercion
                    :summary "Get a list of all cabs"
                    :parameters {:query {(s/optional-key :status) (s/enum "available" "assigned")}}
                    :handler (fn [{{{:keys [status]} :query}
                                   :parameters}]
                               (let [query (merge {}
                                                  (when status {:status status}))]
                                 (ok (cabs/get-cabs query))))}
              :post {:coercion reitit.coercion.schema/coercion
                     :summary "Add a new cab"
                     :parameters {:body {:lat s/Num
                                         :long s/Num
                                         (s/optional-key :type) (s/enum "standard" "pink")
                                         (s/optional-key :status) (s/enum "available")}}
                     :handler (fn [{{cab :body} :parameters}]
                                (let [cab (cabs/add-cab cab)]
                                  (created (str "/api/cabs/" (:id cab)) cab)))}}]
    ["/cabs/:cab-id" {:get {:coercion reitit.coercion.schema/coercion
                            :summary "Get a cab details"
                            :parameters {:path {:cab-id s/Str}}
                            :handler (fn [{{{:keys [cab-id]} :path}
                                           :parameters}] 
                                       (ok (cabs/get-cab cab-id)))}}]
    ["/rides"
     ["/assign" {:post {:coercion reitit.coercion.schema/coercion
                        :summary "Assign a nearest cab from a lat long"
                        :parameters {:body {:lat s/Num 
                                            :long s/Num 
                                            (s/optional-key :user-id) s/Str
                                            (s/optional-key :cab-type) (s/enum "standard" "pink")}}
                        :handler
                        (fn [{{{:keys [lat long user-id cab-type]} :body}
                              :parameters}]
                          (if-let [ride (rides/assign-ride user-id lat long cab-type)]
                            (ok ride)
                            (no-content)))}}]
     ["/:ride-id"
      ["/stop" {:post {:coercion reitit.coercion.schema/coercion
                       :summary "Stop a ride"
                       :parameters {:path {:ride-id s/Str}
                                    :body {:lat s/Num :long s/Num}}
                       :handler
                       (fn [{{{:keys [ride-id]} :path
                              {:keys [lat long]} :body}
                             :parameters}]
                         (ok (rides/stop-ride ride-id lat long)))}}]]]]])