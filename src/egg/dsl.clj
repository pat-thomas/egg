(ns egg.dsl
  (:require [clojure.tools.namespace.find :as ns-find]))

(defn- cljs-file?
  [^java.io.File file]
  (and (.isFile file)
       (.endsWith (.getName file) ".cljs")))

(comment
  (-> "foo.bar_baz"
      (clojure.string/replace #"_" "-"))
  )

(defn- find-cljs-sources-in-dir
  [^String dir]
  (let [abs-dir-path (-> dir
                         java.io.File.
                         .getAbsolutePath)]
    (println "abs-dir-path:" abs-dir-path)
    (->> abs-dir-path
         java.io.File.
         file-seq
         (map #(clojure.string/replace #"_" "-"))
         (filter cljs-file?)
         (sort-by #(.getAbsolutePath %)))))

(comment
  (let [dir          "src/egg/routes"
        abs-dir-path (-> dir
                         java.io.File.
                         .getAbsolutePath)]
    (->> abs-dir-path
         java.io.File.
         file-seq
         (map #(clojure.string/replace % #"_" "-"))))
  )

(defmacro defapp
  [app-name {:keys [routes] :as app-opts}]
  (let [routes (or routes :routes)]
    (println "route-namespaces:" (find-cljs-sources-in-dir (name routes)))
    #_(let [route-namespaces (find-cljs-sources-in-dir (name routes))]
        (println route-namespaces)
        `(do
           ;;(enable-console-print!)
           (println "route-namespaces:" ~route-namespaces)))
    :ok))

(comment
  (.isDirectory (java.io.File. "src"))
  clojure.tools.namespace.file
  )
