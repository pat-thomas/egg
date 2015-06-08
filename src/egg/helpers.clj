(ns egg.helpers
  (:require [clojure.tools.namespace.find :as ns-find]))

(defn- cljs-file?
  [^java.io.File file]
  (and (.isFile file)
       (.endsWith (.getName file) ".cljs")))

(defn find-cljs-sources-in-dir
  [^String dir]
  (def dir dir)
  (let [abs-dir-path (-> "src/"
                         (str dir)
                         java.io.File.
                         .getAbsolutePath)
        current-dir  :something
        all-files-in-dir (->> abs-dir-path
                              java.io.File.
                              file-seq)]
    (def abs-dir-path abs-dir-path)
    (def all-files-in-dir all-files-in-dir)
    (println "abs-dir-path:" abs-dir-path)
    (->> all-files-in-dir
         (filter cljs-file?)
         (map #(clojure.string/replace % #"_" "-"))
         (sort-by #(.getAbsolutePath %)))))

(comment
  (find-cljs-sources-in-dir dir)
  )
