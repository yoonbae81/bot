(defproject yquant.pricealarm "0.1.0-SNAPSHOT"
  :description "A telegram bot that monitors and alarms stock prices when a price hits such a condition made by an user"
  :url "http://example.com/FIXME"
  :license {:name "MIT License"
            :url "httpS://opensource.org/licenses/MIT"}
  :main ^:skip-aot yquant.pricealarm
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.clojure/data.json "0.2.6"]
                 [http-kit "2.3.0-alpha5"]
                 [ring/ring-core "1.6.3"]
                 [compojure "1.6.0"]
  ])
