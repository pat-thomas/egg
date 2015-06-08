(ns egg.dsl
  (:require [egg.helpers :as h]))

(defmacro defapp
  [app-name {:keys [routes] :as app-opts}]
  (let [routes (or routes :routes)]
    `(do
       (enable-console-print!)
       ~(println "route-namespaces:" (h/find-cljs-sources-in-dir (name routes)))
       :ok)))

(comment

  (macroexpand-1
   '(defapp foof
      {:routes :test/routes}))
  )
