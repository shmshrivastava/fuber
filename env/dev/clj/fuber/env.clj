(ns fuber.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [fuber.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[fuber started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[fuber has shut down successfully]=-"))
   :middleware wrap-dev})
