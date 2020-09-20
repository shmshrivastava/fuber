(ns fuber.models.core
  (:require [clojure.java.io :refer [writer] :as io]
            [clojure.tools.logging :as log]
            [fuber.utils.time :as time]))

(def ^:const folder-path "temp/")

(defn get-file-path [filename] (str folder-path filename ".edn"))

(defn write-data-to-file
  "Write a data object to a file"
  [filename data]
  (with-open [w (writer (get-file-path filename))]
    (.write w (pr-str data))))

(defn read-data-from-file
  "Read a file and convert content to a clojure object"
  [filename]
  (try (read-string (slurp (io/resource (get-file-path filename)))) (catch Exception e (log/error e) nil)))

(defn gen-id
  "Returns a unique identifier for a db entry"
  []
  (time/now-ms))

(defn add-id
  "Adds the id key to a map with value as a unique id"
  [data]
  (assoc data :id (gen-id)))

(defn matches-query?
  "returns true if all keys of query have same values in data as in query"
  [data query]
  (reduce #(and %1 (= (get data %2) (get query %2))) true query))

(defprotocol model-methods
  "A protocol for models abstraction"
  (create-doc [model data])
  (get-doc [model id])
  (get-docs [model query])
  (update-doc [model id update-map]))

(defrecord model
  [model-name]
  model-methods
  (create-doc
   [model data]
   (let [data (add-id data)]
     (->> (assoc (read-data-from-file model-name) (:id data) data)
          (write-data-to-file model-name))))
  (get-doc
   [model id]
   (-> model-name
       (read-data-from-file)
       (get id)))
  (get-docs
   [model query]
   (filterv #(matches-query? % query) (mapv second (read-data-from-file model-name))))
  (update-doc
   [model id update-map]
   (write-data-to-file
    model-name
    (-> model-name
        (read-data-from-file)
        (update id merge (dissoc :id update-map))))
   (-> model-name
       (read-data-from-file)
       (get id))))