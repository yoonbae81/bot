(ns yquant.bot.db
  (:require
    [clojure.java.jdbc :as jdbc]
    [mount.core :refer [defstate]]
    [yquant.bot.config :refer [config]]))

(defn start [config]
  (let [conn (jdbc/get-connection config)]
    (assoc config :connection conn)))

(defstate
  ^{:on-reload :noop}
  db
  :start (start (:db config))
  :stop (-> db :connection .close))

(comment
  (def chat-id 268911454)
  (def db (start (:db config)))
  (jdbc/query db "SELECT 3*5 as RESULT")
  (jdbc/find-by-keys db :monitor {:chat_id chat-id})
  )
