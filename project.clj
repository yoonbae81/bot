(defproject yquant/bot "0.1.0-SNAPSHOT"
  :description "A telegram bot that monitors and alarms Korean stock prices when a price hits such a condition made by an user"
  :url "https://github.com/yoonbae81/yQuantBot"
  :license {:name "The MIT License"
            :url  "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure       "1.9.0"]
                 [org.clojure/tools.logging "0.4.0"]
                 [org.clojure/tools.namespace "0.2.11"]
                 [org.clojure/data.json     "0.2.6"]
                 [org.clojure/tools.cli     "0.3.5"]
                 [mount                     "0.1.12"]
                 [ring/ring-core            "1.6.3"]
                 [ring/ring-jetty-adapter   "1.6.3"]
                 [ring/ring-json            "0.4.0"]
                 [ring/ring-ssl             "0.3.0"]
                 [environ                   "1.1.0"]
                 [morse                     "0.2.4"]
                 [com.layerware/hugsql      "0.4.8"]
                 [org.clojure/java.jdbc     "0.7.5"]
                 [org.xerial/sqlite-jdbc "3.21.0.1"]]
  :main ^:skip-aot yquant.bot.core
  :resource-paths ["resources" "target/resources"]
  :profiles
  {:repl {:prep-tasks   ^:replace ["javac" "compile"]
          :repl-options {:init-ns user}}
   :uberjar {:aot :all}}
  )
