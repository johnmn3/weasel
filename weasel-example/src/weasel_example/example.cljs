(ns weasel-example.example
  (:require [weasel.repl :as repl]
            [weasel-example.foo :as foo :refer [baz]]))

(set-print-fn! #(js/console.log %))

(when-not (repl/alive?)
  (repl/connect "ws://localhost:9001"))

(if (repl/alive?)
  (println "Loaded example"))

(repl/alive?)
