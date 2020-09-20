(ns fuber.models.rides
  (:require [fuber.models.core :as db]
            [fuber.utils.time :as time]
            [fuber.utils.distance :as dis]))

(def Rides (db/->model "rides"))

(defn create-ride
  "Create and start ride for user"
  [cab user-id]
  (db/create-doc Rides 
                 {:cab (:id cab) 
                  :user user-id 
                  :cab-type (:type cab)
                  :start-time (time/now-ms)
                  :start-pos (select-keys cab [:lat :long])}))

(defn compute-fare
  "Compute fare from distance, duration in ms and cab-type"
  [distance duration cab-type]
  (+ (/ duration 60000)
     (* 2 distance)
     (if (= cab-type "pink") 5 0)))

(defn stop-ride
  "Stop ride and calculate fare"
  [ride-id lat long]
  (let [ride (db/get-doc Rides ride-id)
        distance (dis/get-distance lat long
                                   (-> ride :start-pos :lat)
                                   (-> ride :start-pos :long))
        current-time (time/now-ms)
        duration (- current-time (:start-time ride))
        fare (compute-fare distance duration (:cab-type ride))]
    (db/update-doc Rides ride-id
                   {:distance distance
                    :duration duration
                    :end-time current-time
                    :end-pos {:lat lat :long long}
                    :fare fare})))