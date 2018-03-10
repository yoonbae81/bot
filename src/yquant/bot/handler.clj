(ns yquant.bot.handler
  (:require
    [yquant.bot.utils.session :refer [get-session]]))

(defn dispatch
  "if text starts with / then converts keyword, otherwise retrieve :mode from session map"
  [text chat-id]
  (if (= (get text 0) \/)
    (keyword (subs text 1))
    (:mode (get-session chat-id))))

(defmulti handler dispatch)

(comment
  (def request {:body {:message {:chat {:id 268911454} :text "/help"}}})
  (def chat-id (get-in request [:body :message :chat :id]))
  (def text (get-in request [:body :message :text]))

  (def chat-id 268911454)
  (dispatch "/help" chat-id)
  )
