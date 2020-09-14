(ns fuber.models.core
  (:require [clojure.java.io :refer [writer] :as io]
            [clojure.tools.logging :as log]))

(def ^:const folder-path "temp/")

(defn get-file-path [filename] (str folder-path filename))

(defn write-data-to-file
  "Write a data object to a file"
  [filename data]
  (with-open [w (writer (get-file-path filename))]
    (.write w (pr-str data))))

(defn read-data-from-file
  "Read a file and convert content to a clojure object"
  [filename]
  (try (read-string (slurp (io/resource (get-file-path filename)))) (catch Exception e (log/error e) nil)))

