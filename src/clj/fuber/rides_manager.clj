(ns fuber.rides-manager
  (:require [fuber.models.cabs :as cabs]
            [fuber.models.rides :as rides]
            [clojure.tools.logging :as log]))

(defn assign-ride
  [user-id lat long cab-type]
  (log/info "Assigning ride" user-id lat long cab-type)
  (let [cab (cabs/assign-nearest-cab lat long cab-type)
        ride (when cab (rides/create-ride cab user-id))]
    (if ride
      (log/info "Created ride" ride)
      (log/info "Ride creation failed, no cabs found for" user-id lat long cab-type))
    ride))

(defn stop-ride
  [ride-id lat long]
  (log/info "Stopping ride" ride-id lat long)
  (let [ride (rides/stop-ride ride-id lat long)]
    (cabs/unassign-cab (:cab ride) lat long)
    ride))