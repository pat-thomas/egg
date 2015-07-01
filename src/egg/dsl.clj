(ns egg.dsl
  (:require [egg.helpers :as h]))

(defmacro defapp
  [app-name & {:keys [routes] :as app-opts}]
  (let [current-ns       *ns*
        routes           (or routes :routes)
        route-namespaces (h/find-cljs-namespaces-for-prefix routes)]
    `(do
       (cljs.core/enable-console-print!)
       ~@(for [route-ns# route-namespaces]
           (let [require-str# (h/namespace->goog-require-str route-ns#)]
             `(do
                (goog/require ~require-str#)))))))
