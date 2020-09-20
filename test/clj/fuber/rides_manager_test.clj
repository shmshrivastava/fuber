(ns fuber.rides-manager-test
  (:require [midje.sweet :refer [=> fact facts provided anything]]
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
  ))