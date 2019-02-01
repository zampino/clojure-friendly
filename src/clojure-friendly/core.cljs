(ns clojure-friendly.core)

(defn start [] (println "Hello Clojure"))

(let [elm (.createElement js/document "h1")]
    (set! (. elm -innerHTML) "Hello, Clojure")
    (-> js/document
        (.getElementById "app")
        (.appendChild elm)))
