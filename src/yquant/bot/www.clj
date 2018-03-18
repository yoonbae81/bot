(ns yquant.bot.www
  (:require
    [mount.core :refer [defstate]]
    [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
    [clojure.tools.logging :refer [trace debug info warn error fatal]]
    [yquant.bot.config :refer [config]]
    [yquant.bot.handler :refer [handler]]
    [yquant.bot.handlers.default]
    [yquant.bot.handlers.list]
    [yquant.bot.handlers.monitor]
    [yquant.bot.utils.session :as s]
    [ring.adapter.jetty :refer [run-jetty]]))

(defn webhook
  "The only handler which has been registered to Telegram as a webhook"
  [request]
  (let [incoming (get-in request [:body :message])
        outgoing (handler incoming)]
    (info incoming)
    (info (s/get-by incoming))
    (info outgoing)
    {:status  200
     :headers {"content-type" "application/json"}
     :body    (merge {:method     "sendMessage"
                      :chat_id    (get-in incoming [:chat :id])
                      :parse_mode "markdown"}
                     outgoing)}))

(defn start [config]
  (-> #'webhook
      (wrap-json-body {:keywords? true :bigdecimals? true})
      (wrap-json-response)
      (run-jetty config)))

(defstate www
          :start (start (:www config))
          :stop (.stop www))

(comment
  (def username "yoonbae81")
  (webhook {:body {:message {:chat {:id 268911454} :text "/help"}}})
  (webhook {:body {:message {:chat {:id 268911454} :text "/add"}}})
  (webhook {:body {:message {:chat {:id 268911454} :text "LG전자"}}})
  (webhook {:body {:message {:chat {:id 268911454} :text "85000"}}})
  )
