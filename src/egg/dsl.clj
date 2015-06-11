(ns egg.dsl
  (:require [egg.helpers :as h]))

(defmacro defapp
  [app-name {:keys [routes] :as app-opts}]
  (let [current-ns       *ns*
        routes           (or routes :routes)
        route-namespaces (h/find-cljs-namespaces-for-prefix routes)]
    `(do
       ~@(for [route-ns# route-namespaces]
           `(goog/require ~(h/namespace->goog-require-str route-ns#))))))
