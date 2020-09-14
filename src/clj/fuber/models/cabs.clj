(ns fuber.models.cabs
  (:require [fuber.models.core :as db]))

(defn get-cabs
  "Get all cabs"
  []
  (db/read-data-from-file "cabs.edn"))

(defn get-cab
  "Get cab details for a cab id"
  [id]
  (some #(= id (:id %)) (get-cabs)))