(ns egg.dsl
  (:require [egg.helpers :as h]))

(defmacro defapp
  [app-name {:keys [routes] :as app-opts}]
  (let [routes           (or routes :routes)
        route-namespaces (h/find-namespaces-for-prefix (name routes))]
    `(do
       (cljs.core/enable-console-print!)
       ~@(for [namespace# route-namespaces]
           `(goog/require ~namespace#))
       :ok)))
