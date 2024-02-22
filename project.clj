(defproject org.clojars.stopper/pg2-hugsql-adapter "0.0.1"
  :description "pg2 adapter for hugsql"
  :url "https://github.com/stopper74/pg2-hugsql-adapter"
  :license "MIT"
  :dependencies [[org.clojure/clojure "1.12.0-alpha4"]
                 [com.layerware/hugsql "0.5.1"]
                 [com.github.igrishaev/pg2-core "0.1.3"]]
  :deploy-repositories [["clojars" {:sign-releases false :url "https://clojars.org/repo"}]])