(ns fuber.models.cabs
  (:require [fuber.models.core :as db]))

(def Cabs (db/->model "cabs"))

(defn get-cabs
  "Get all cabs"
  ([]
   (db/get-docs Cabs {}))
  ([query-map]
   (db/get-docs Cabs query-map)))

(defn get-cab
  "Get cab details for a cab id"
  [id]
  (db/get-doc Cabs id))

(defn get-cab-distance
  "Get distance of a cab from given lat and long. Uses Pythagoras' theorem for now."
  [{:keys [lat long] :as cab} cur-lat cur-long]
  (let [lat-diff (- lat cur-lat)
        long-diff (- long cur-long)]
    (Math/sqrt (+ (* lat-diff lat-diff) (* long-diff long-diff)))))

(defn assign-cab
  "Assign a cab"
  [cab-id]
  (db/update-doc Cabs cab-id {:status "assigned"}))

(defn unassign-cab
  "Make the cab available"
  [cab-id]
  (db/update-doc Cabs cab-id {:status "available"}))

(defn get-nearest-cab
  "Get a nearest cab from provided lat long"
  [lat long cab-type]
  (->> (get-cabs (merge {:status "available"} (when cab-type {:type cab-type})))
       (mapv #(assoc % :distance (get-cab-distance % lat long)))
       (sort-by :distance)
       (first)))