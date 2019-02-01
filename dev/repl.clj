(ns repl
  (:gen-class)
  (:require [clojure.tools.nrepl.server :as nrepl.server]
            [figwheel.main :as fm]
            [figwheel.main.api :as fw]
            [figwheel.main.evalback :as eb]
            [cider.piggieback :as pback]))

(def cfg
  {:id "dev",
   :config {:log-level "DEBUG"
            :watch-dirs ["src"]
            :build-inputs ["src"]
            :reload-clj-files false}
   :options {:main 'clojure-friendly.core}})

(defn build-start [] (fw/start {:mode :serve} cfg))
(defn stop-build [] (fw/stop "dev"))
(defn cljs-repl [] (fw/cljs-repl "dev"))

(defn -main []
  (println "Booting server repl on port 7777")
  (nrepl.server/start-server :bind "localhost"
                             :port 7777
                             :handler (nrepl.server/default-handler #'pback/wrap-cljs-repl)))

(comment
  (build-start)
  (cljs-repl)

  :cljs/quit
  (stop-build))
  ; (cljs-tooling.complete/completions cljs.repl "red" 'cljs.core))
