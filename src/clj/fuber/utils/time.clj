(ns fuber.utils.time)

(defn now-ms
  []
  (.getTime (new java.util.Date)))