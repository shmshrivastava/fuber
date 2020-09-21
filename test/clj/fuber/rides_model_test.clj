(ns fuber.rides-model-test
  (:require [midje.sweet :refer [=> fact facts provided anything]]
            [fuber.models.rides :as rides]
            [fuber.models.core :as db]
            [fuber.utils.time :as time]
            [fuber.utils.distance :as dis]))

(facts
 "Testing functions from fuber.models.rides"
 
 (facts
  "Testing create-ride function"

  (let [ride {:cab ..cab-id..
              :user ..user-id..
              :cab-type ..cab-type..
              :start-pos {:lat ..lat.. :long ..long..}
              :start-time ..start-time..}
        ride-out (assoc ride :id "..start-time..")]
    (fact
     (rides/create-ride {:id ..cab-id.. :type ..cab-type.. :lat ..lat.. :long ..long..}
                        ..user-id..)
     => ride-out
     (provided (time/now-ms) => ..start-time..))))
 
 (facts 
  "Testing compute-fare function"
  (rides/compute-fare 0 60000 "standard") => 1
  (rides/compute-fare 0 60000 "pink") => 6
  (rides/compute-fare 1 60000 "standard") => 3)
 
 (facts
  "Testing stop-ride function"
  (let [ride {:cab ..cab-id..
              :user ..user-id..
              :id ..ride-id..
              :cab-type "pink"
              :start-pos {:lat 0 :long 0}
              :start-time 0}
        ride-update {:end-pos {:lat 0 :long 1}
                     :distance 1.0
                     :duration 60000
                     :end-time 60000
                     :fare 8.0}
        ride-out (merge ride ride-update)]
    (fact
     "Testing valid ride id"
     (rides/stop-ride ..ride-id.. 0 1) => ride-out
     (provided
      (rides/get-ride ..ride-id..) => ride
      (time/now-ms) => 60000
      (rides/update-ride ..ride-id.. ride-update) => ride-out))
    )
   (fact
    "Testing for invalid ride"
    (rides/stop-ride ..ride-id.. ..lat.. ..long..) => nil
    (provided (rides/get-ride ..ride-id..) => nil))
  )
 )