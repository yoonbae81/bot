(defproject pricemonitor "0.1.0-SNAPSHOT"
  :description "A telegram bot that monitors and notifies stock prices when a price hits such condition made by an user"
  :url "http://github.com/yoonbae81/yQuant.PriceMonitor/"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot pricemonitor.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
