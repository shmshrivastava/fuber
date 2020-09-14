(ns fuber.handler
  (:require
   [fuber.middleware :as middleware]
   [fuber.layout :refer [error-page]]
   [fuber.routes.home :refer [home-routes]]
   [fuber.routes.api :refer [api-routes]]
   [reitit.ring :as ring]
   [ring.middleware.content-type :refer [wrap-content-type]]
   [ring.middleware.webjars :refer [wrap-webjars]]
   [fuber.env :refer [defaults]]
   [reitit.ring.middleware.exception :as exception]
   [clojure.tools.logging :as log]
   [clojure.pprint :refer [pprint]]
   [reitit.dev.pretty :as pretty]
   [reitit.coercion.spec]
   [muuntaja.core :as m]
   [reitit.swagger :as swagger]
   [reitit.swagger-ui :as swagger-ui]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.parameters :as parameters]
   [reitit.ring.middleware.multipart :as multipart]
   [reitit.ring.coercion :as coercion]
   [mount.core :as mount]))

(mount/defstate init-app
  :start ((or (:init defaults) (fn [])))
  :stop  ((or (:stop defaults) (fn []))))

(defn handler [message exception request]
  {:status 500
   :body {:message message
          :exception (.getClass exception)
          :data (ex-data exception)
          :uri (:uri request)}})

(def exception-middleware
  (exception/create-exception-middleware
    (merge
      exception/default-handlers
      {;; ex-data with :type ::error
       ::error (partial handler "error")

       ;; ex-data with ::exception or ::failure
       ::exception (partial handler "exception")
       
       ;; override the default handler
       ::exception/default (partial handler "default")

       ;; print stack-traces for all exceptions
       ::exception/wrap (fn [handler e request]
                          (log/error "ERROR" e)
                          (pprint e)
                          (handler e request))})))

(mount/defstate app-routes
  :start
  (ring/ring-handler
   (ring/router
    [(home-routes) (api-routes)]
    {;;:reitit.middleware/transform dev/print-request-diffs ;; pretty diffs
       ;;:validate spec/validate ;; enable spec validation for route data
       ;;:reitit.spec/wrap spell/closed ;; strict top-level validation
     :exception pretty/exception
     :conflicts nil
     :data {:coercion reitit.coercion.spec/coercion
            :muuntaja m/instance
            :middleware [;; swagger feature
                         swagger/swagger-feature

                           ;; query-params & form-params
                         parameters/parameters-middleware
                           ;; content-negotiation
                         muuntaja/format-negotiate-middleware
                           ;; encoding response body
                         muuntaja/format-response-middleware
                           ;; exception handling
                         exception-middleware
                           ;; decoding request body
                         muuntaja/format-request-middleware
                           ;; coercing response bodys
                         coercion/coerce-response-middleware
                           ;; coercing request parameters
                         coercion/coerce-request-middleware
                           ;; multipart
                         multipart/multipart-middleware]}})
   (ring/routes
    (swagger-ui/create-swagger-ui-handler
     {:path   "/swagger-ui"
      :url    "/api/swagger.json"
      :config {:validator-url nil}})
    (ring/create-resource-handler
     {:path "/"})
    (wrap-content-type
     (wrap-webjars (constantly nil)))
    (ring/create-default-handler
     {:not-found
      (constantly (error-page {:status 404, :title "404 - Page not found"}))
      :method-not-allowed
      (constantly (error-page {:status 405, :title "405 - Not allowed"}))
      :not-acceptable
      (constantly (error-page {:status 406, :title "406 - Not acceptable"}))}))))

(defn app []
  (middleware/wrap-base #'app-routes))
