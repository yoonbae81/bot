(ns yquant.bot.utils.search
  (:require [clojure.data.json :as json]
            [clojure.edn       :as edn]
            [clojure.java.io   :as io]))

(def symbols (edn/read-string (slurp (io/resource "symbols.edn"))))

(defn- search-symbol
  [text]
  (let [inv  (clojure.set/map-invert symbols)
        name (inv text)]
    (when name (hash-map name text))))

(defn- search-name
  [text]
  (let [symbol (symbols text)]
    (when symbol (hash-map text symbol))))

(defn- search-partial
  [text]
  (let [re (re-pattern (str "^" text ".+"))
        ks (filter some? (map #(re-find re %) (keys symbols)))]
    (into (sorted-map) (for [k ks] [k (symbols k)]))))
; => {"한국전력" "015760", "한국전력기술" "052690"}

(defn search
  [text]
  (some #(when (some? %) %)
        [(search-symbol  text)
         (search-name    text)
         (search-partial text)]))

(comment
  (search "015760")
  (search "한국전력")
  (search "KODEX")
  (search "TIGER")
  )
