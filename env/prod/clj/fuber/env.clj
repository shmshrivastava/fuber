(ns fuber.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[fuber started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[fuber has shut down successfully]=-"))
   :middleware identity})
