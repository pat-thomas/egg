(ns egg.dsl
  (:require [egg.helpers :as h]))

(defmacro defapp
  [app-name {:keys [routes] :as app-opts}]
  (let [routes (or routes :routes)
        route-namespaces (h/find-cljs-sources-in-dir (name routes))]
    (println "route-namespaces:" route-namespaces)
    `(do
       (cljs.core/enable-console-print!)
       :ok)))
