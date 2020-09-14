(ns fuber.models.cabs
  (:require [fuber.models.core :as db]))

(def Cabs (db/->model "cabs"))

(defn get-cabs
  "Get all cabs"
  []
  (db/get-docs Cabs {}))

(defn get-cab
  "Get cab details for a cab id"
  [id]
  (db/get-doc Cabs id))