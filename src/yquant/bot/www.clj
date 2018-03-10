(ns yquant.bot.www
  (:require
    [mount.core             :refer [defstate]]
    [ring.middleware.json   :refer [wrap-json-body wrap-json-response]]
    [clojure.tools.logging  :refer [trace debug info warn error fatal]]
    [yquant.bot.config      :refer [config]]
    [yquant.bot.handler     :refer [handler]]
    [yquant.bot.handlers.default]
    [yquant.bot.handlers.list]
    [yquant.bot.handlers.monitor]
    [yquant.bot.utils.session :refer [get-session]]
    [ring.adapter.jetty     :refer [run-jetty]]))

(defn webhook
  "The only handler which has been registered to Telegram as a webhook"
  [request]
  (let [text    (get-in request [:body :message :text])
        chat-id (get-in request [:body :message :chat :id])
        message (handler text chat-id)]
    (info (get-session chat-id))
    {:status  200
     :headers {"content-type" "application/json"}
     :body    (merge {:method     "sendMessage"
                      :chat_id    chat-id
                      :parse_mode "markdown"}
                     message)}))

(defn start [config]
  (-> #'webhook
      (wrap-json-body {:keywords? true :bigdecimals? true})
      (wrap-json-response)
      (run-jetty config)))

(defstate www
          :start (start (:www config))
          :stop  (.stop www))

(comment
  (webhook {:body {:message {:chat {:id 268911454} :text "/help"}}})
  (webhook {:body {:message {:chat {:id 268911454} :text "/add"}}})
  (webhook {:body {:message {:chat {:id 268911454} :text "LG전자"}}})
  (webhook {:body {:message {:chat {:id 268911454} :text "85000"}}})
  )
