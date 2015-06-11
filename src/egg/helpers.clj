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
      last
      (clojure.string/replace #"-" "_")))

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
  (sort-by #(.getAbsolutePath %)
           (filter cljs-file? (file-seq (java.io.File. dir)))))

(defn find-cljs-files-for-prefix
  [^String prefix]
  (let [src-root          (current-directory-absolute-with-src)
        cljs-files        (find-cljs-sources-in-dir src-root)
        project-root-name (pwd)]
    (->> cljs-files
         (map (fn [^java.io.File file]
                (let [absolute-path (.getAbsolutePath file)
                      idx           (ending-index absolute-path src-root)]
                  (.substring absolute-path idx))))
         (filter (fn [^String file]
                   (.startsWith file prefix)))
         (map (fn [^String file]
                (str src-root file))))))

(defn find-cljs-namespaces-for-prefix
  [prefix]
  (let [prefix            (name prefix)
        remove-cljs-suffix (fn [^String cljs-file-path]
                             (let [len (count cljs-file-path)]
                               (->> cljs-file-path
                                    (take (- len 5))
                                    (apply str))))
        src-root          (current-directory-absolute-with-src)
        cljs-files        (find-cljs-sources-in-dir src-root)
        project-root-name (pwd)]
    (->> cljs-files
         (map (fn [^java.io.File file]
                (let [absolute-path (.getAbsolutePath file)
                      idx           (ending-index absolute-path src-root)]
                  (-> absolute-path
                      (.substring idx)
                      remove-cljs-suffix
                      (clojure.string/replace #"\/" ".")
                      (clojure.string/replace #"_" "-")
                      symbol)))))))

(defn namespace->goog-require-str
  [^clojure.lang.Symbol namespace]
  (str (pwd) "." (-> namespace
                     name
                     (clojure.string/replace #"\." "/")
                     (clojure.string/replace #"-" "_"))))
