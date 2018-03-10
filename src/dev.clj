(ns dev
  (:require [clojure.tools.namespace.repl :as tn]
            [mount.core :as mount]
            [yquant.bot.config :refer [config]]
            [yquant.bot.db     :refer [db]]
            [yquant.bot.www    :refer [www]]))

(defn start []
  (mount/start
    #'config
    #'db
    #'www))

(defn stop []
  (mount/stop))

(defn refresh []
  (stop)
  (tn/refresh))

(defn refresh []
  (stop)
  (tn/refresh-all))

(defn go
  "starts all states defined by defstate"
  []
  (start)
  :ready)

(defn reset
  "stops all states defined by defstate, reloads modified source files, and restarts the states"
  []
  (stop)
  (tn/refresh :after 'dev/go))