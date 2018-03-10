(ns yquant.bot.handler
  (:require
    [yquant.bot.utils.session :refer [get_session]]))

(defn dispatch
  "if text starts with / then converts keyword, otherwise retrieve :mode from session map"
  [text chat-id]
  (if (= (get text 0) \/)
    (keyword (subs text 1))
    (:mode (get_session chat-id))))

(defmulti handler dispatch)

