(ns fuber.cabs-model-test
  (:require [midje.sweet :refer [=> fact facts provided =contains=>]]
            [fuber.models.cabs :as cabs]
            [fuber.models.core :as db]
            [fuber.utils.time :as time]
            [fuber.utils.distance :as dis]))

(facts
 "Testing functions in namespace fuber.models.cabs"
 
 (facts
  "Testing cabs/assign-nearest-cab function"

  (let [cabs [{:lat 0 :long 0 :id ..cab1.. :status "available"}
              {:lat 1 :long 0 :id ..cab2.. :status "available"}
              {:lat 2 :long 0 :id ..cab3.. :status "available"}]
        cab {:lat 1 :long 0 :id ..cab2.. :status "assigned"}]
    (fact
     "Testing for cabs available"
     (cabs/assign-nearest-cab 1 0 ..cab-type..) => cab
     (provided
      (cabs/get-cabs {:status "available" :type ..cab-type..}) => cabs
      ..cab2.. =contains=> {:id ..cab2-id..}
      (cabs/assign-cab ..cab2..) => cab)))
  (fact
   "Testing for cabs unavailable"
   (cabs/assign-nearest-cab 1 0 ..cab-type..) => nil
   (provided
    (cabs/get-cabs {:status "available" :type ..cab-type..}) => []))))