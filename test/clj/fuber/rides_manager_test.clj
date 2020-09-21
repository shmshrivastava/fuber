(ns fuber.rides-manager-test
  (:require [midje.sweet :refer [=> fact facts provided anything =contains=>]]
            [fuber.rides-manager :as rides-manager]
            [fuber.models.cabs :as cabs]
            [fuber.models.rides :as rides]))

(facts
 "Testing functions from namespace fuber.rides-manager"
 
 (facts
  "Testing assign-ride function"
  
  (fact
   "Testing for cab found"
   (rides-manager/assign-ride ..user-id.. ..lat.. ..long.. ..cab-type..) => ..ride..
   (provided
    (cabs/assign-nearest-cab ..lat.. ..long.. ..cab-type..) => ..cab..
    (rides/create-ride ..cab.. ..user-id..) => ..ride..))
  
  (fact
   "Testing for no cab found"
   (rides-manager/assign-ride ..user-id.. ..lat.. ..long.. ..cab-type..) => nil
   (provided
    (cabs/assign-nearest-cab ..lat.. ..long.. ..cab-type..) => nil
    (rides/create-ride anything ..user-id..) => anything :times 0))
  )
 
 (facts
  "Testing stop-ride function"
  (rides-manager/stop-ride ..ride-id.. ..lat.. ..long..) => ..ride..
  (provided
   (rides/stop-ride ..ride-id.. ..lat.. ..long..) => ..ride..
   ..ride.. =contains=> {:cab ..cab-id..}
   (cabs/unassign-cab ..cab-id.. ..lat.. ..long..) => anything)))