(ns yquant.bot.config
  (:require [clojure.edn :as edn]
            [environ.core :refer [env]]
            [clojure.tools.logging :refer [info]]
            [mount.core :refer [defstate]]))

(defn load-config []
  {:www {:http?        false
         :ssl?         true
         :keystore     (env :yquant-bot-keystore)
         :key-password (env :yquant-bot-keypasswd)
         :join?        false}
   :db {:classname   "org.sqlite.JDBC"
        :subprotocol "sqlite"
        :subname     (env :yquant-bot-db)}})

(defstate config
          :start (load-config))
