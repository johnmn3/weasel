(defproject weasel "0.7.1-SNAPSHOT"
  :description "websocket REPL environment for ClojureScript"
  :url "http://github.com/tomjakubowski/weasel"
  :license {:name "Unlicense"
            :url "http://unlicense.org/UNLICENSE"
            :distribution :repo}

  :dependencies [[org.clojure/clojure "1.10.0-alpha4"]
                 [org.clojure/clojurescript "1.10.145"]
                 [org.java-websocket/Java-WebSocket "1.3.8"]]


  :repositories [["releases" {:url "https://clojars.org/repo"
                              :creds :gpg}]]

  :pom-addition [:developers [:developer
                              [:name "Tom Jakubowski"]
                              [:email "tom@crystae.net"]
                              [:url "https://github.com/tomjakubowski"]]
                             [:developer
                              [:name "John Newman"]
                              [:email "john.michael.newman@gmail.com"]
                              [:url "https://github.com/johnmn3"]]]
  :profiles {:dev {:dependencies [[com.cemerick/piggieback "0.2.2-SNAPSHOT"]]}}
  :source-paths ["src/clj" "src/cljs"]
  :scm {:name "git"
        :url "https://github.com/tomjakubowski/weasel"})
