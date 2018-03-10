(ns yquant.bot.db
  (:require
    [clojure.java.jdbc      :as    jdbc]
    [mount.core             :refer [defstate]]
    [yquant.bot.config      :refer [config]]))

(declare db)

(defn start [config]
  (let [conn (jdbc/get-connection config)]
    (assoc config :connection conn)))

(defstate
  ^{:on-reload :noop}
  db :start (start (:db config))
             :stop  (-> db :connection .close))

(comment
  (def db (start (:db config)))
  (jdbc/query db "SELECT 3*5 as RESULT")
  )
