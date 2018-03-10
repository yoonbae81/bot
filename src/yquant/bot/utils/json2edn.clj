(ns yquant.bot.utils.json2edn
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [clojure.edn :as edn]))

(def input (json/read-str (slurp (io/resource "symbols.json"))))
(def output (into (sorted-map) (for [symbol (keys input)] [((input symbol) "name") symbol])))

(with-open [w (io/writer "resources/symbols.edn")]
  (.write w (pr-str output)))
