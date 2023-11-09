(defproject clicker "0.1.0"
  :description "A collaborative clicker game"
  :url "https://github.com/balloneij/clicker"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [com.balloneij/slouch "0.1.0"]
                 [garden "1.3.10"]
                 [ring/ring-core "1.11.0-RC1"]
                 [metosin/reitit "0.7.0-alpha7"]
                 [org.springframework.security/spring-security-core "6.1.5"]
                 [info.sunng/ring-jetty9-adapter "0.30.2"]
                 [clojure.java-time "1.3.0"]]
  :global-vars {*warn-on-reflection* true}
  :repl-options {:init-ns clicker.core})
