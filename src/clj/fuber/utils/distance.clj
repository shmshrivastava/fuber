(ns fuber.utils.distance)

(defn get-distance
  "Get distance of a cab from given lat and long. Uses Pythagoras' theorem for now."
  [lat1 long1 lat2 long2]
  (println lat1 long1 lat2 long2)
  (let [lat-diff (- lat1 lat2)
        long-diff (- long1 long2)]
    (Math/sqrt (+ (* lat-diff lat-diff) (* long-diff long-diff)))))