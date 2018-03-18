(ns yquant.bot.handler
  (:require
    [yquant.bot.utils.session :as s]))

(def top-level-pages {:list    {}
                      :holding {}
                      :monitor {}})

(defn dispatch
  "if text starts with / then converts keyword, otherwise retrieve :mode from session map"
  [message]
  (let [session (s/get-by message)
        handler (-> message
                    :text
                    (subs 1)
                    keyword)]
    (if (or (contains? top-level-pages handler)
            (contains? (:available-pages session) handler))
      (s/set-next-handler message handler)
      (:handler session))))

(defmulti handler dispatch)

(comment
  (def message {:text "/list" :from {:username "yoonbae81"} :chat {:id 268911454}})
  (dispatch {:text "/list" :from {:username "yoonbae81"} :chat {:id 268911454}})
  )
