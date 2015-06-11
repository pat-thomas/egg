(ns egg.dsl
  (:require [egg.helpers :as h]))

(defmacro defapp
  [app-name {:keys [routes] :as app-opts}]
  (let [current-ns       *ns*
        routes           (or routes :routes)
        route-namespaces (h/find-cljs-namespaces-for-prefix routes)]
    `(do
       ~@(for [route-ns# route-namespaces]
           `(goog/require ~(h/namespace->goog-require-str route-ns#)))
       #_@(for [route-ns# route-namespaces]
            `(do
               (cljs.core/in-ns ~route-ns#)
               :foof))
       #_`(cljs.core/in-ns ~current-ns))))

(comment
  (macroexpand-1
   '(defapp foof
      {:routes :routes}))
  (h/find-cljs-namespaces-for-prefix :routes)
  )
