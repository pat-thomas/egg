(ns egg.helpers
  (:require [clojure.tools.namespace.find :as ns-find]))

(defn cljs-file?
  [^java.io.File file]
  (and (.isFile file)
       (.endsWith (.getName file) ".cljs")))

(defn pwd
  []
  (-> "."
      java.io.File.
      .getCanonicalPath
      (clojure.string/split #"/")
      last))

(defn ending-index
  [^String target ^String s]
  (loop [counter        0
         [c & more-c]   s
         [tc & more-tc] target]
    (cond
      (not= c tc)
      counter
      
      (empty? target)
      counter
      
      :keep-going
      (recur (inc counter)
             more-c
             more-tc))))

(defn current-directory-absolute-with-src
  []
  (str (.getCanonicalPath (java.io.File. ".")) "/src/" (pwd) "/"))

(defn find-cljs-sources-in-dir
  [^String dir]
  (let [curr-dir         (current-directory-absolute-with-src)
        abs-dir-path     (-> "src/"
                             (str (pwd) "/" dir)
                             java.io.File.
                             .getAbsolutePath)]
    (->> abs-dir-path
         java.io.File.
         file-seq
         (filter cljs-file?)
         (map #(clojure.string/replace % #"_" "-"))
         (sort-by #(.getAbsolutePath %))
         (map (fn [^String source-file]
                (-> source-file
                    (.substring (ending-index source-file curr-dir))
                    (clojure.string/replace #".cljs" "")
                    (clojure.string/replace #"\/" ".")
                    symbol))))))
