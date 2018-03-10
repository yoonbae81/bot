(ns yquant.bot.handlers.monitor
  (:require [clojure.string     :as str]
            [clojure.edn        :as edn]
            [clojure.java.jdbc  :as jdbc]
            [yquant.bot.db      :refer [db]]
            [yquant.bot.handler :refer [handler]]
            [yquant.bot.utils.session :refer [get_session set_session clear_session]]
            [yquant.bot.utils.search  :refer [search]]))

(defmethod handler :mon [_ chat-id]
  (handler "/monitor" chat-id))

(defmethod handler :monitor [_ chat-id]
  (clear_session chat-id)
  (handler "/monitor-ask-stock" chat-id))

(defmethod handler :monitor-ask-stock [text chat-id]
  (set_session chat-id {:mode :monitor-search-stock})
  {:text "*모니터링할 종목은?*\n(종목명/종목코드 입력가능)"})

(defmethod handler :monitor-search-stock [text chat-id]
  (cond
    (= text "확인") (handler "/monitor-ask-price" chat-id)
    (= text "재입력") (handler "/monitor-ask-stock" chat-id)
    :else (let [result (search text)]
            (case (count result)
              0 {:text "검색된 종목이 없음\n*모니터링할 종목은?*"}
              1 (do
                  (set_session chat-id {:symbol (first (keys result))})
                  {:text (str "종목명 : "
                              (first (keys result)) "\n"
                              "종목코드 : "
                              (first (vals result)))
                   :reply_markup {:keyboard [["확인"] ["재입력"]]
                                  :one_time_keyboard true}})
              {:text (str "검색결과 : " (count result) "종목\n"
                          (str/join "\n" (keys result))
                          "\n*모니터링할 종목은?*")}))))

(defmethod handler :monitor-ask-price [text chat-id]
  (set_session chat-id {:mode :monitor-confirm-price})
  {:text "*모니터링 목표가는?*"})

(defmethod handler :monitor-confirm-price [text chat-id]
  (cond
    (or (= text "클때") (= text "작을때"))
    (let [compare (if (= text "클때") ">" "<")]
      (set_session chat-id {:compare compare})
      (handler "/monitor-result" chat-id))
    (= text "재입력") (handler "/monitor-ask-price" chat-id)
    :else (let [price (-> text
                          (str/replace #"^0+" "")
                          (str/replace #"\D"  "")
                          (edn/read-string))]
            (if (nil? price)
              (handler "/monitor-ask-price" chat-id)
              (do
                (set_session chat-id {:price price})
                {:text         (str "*알림을 받을 시점은?*\n"
                                    "목표가 " (format "%,d" price) "원 대비")
                 :reply_markup {:keyboard [["클때"]
                                           ["작을때"]
                                           ["재입력"]]
                                :one_time_keyboard true}})))))

(defmethod handler :monitor-result [text chat-id]
  (let [s (get_session chat-id)]
    (jdbc/insert! db :monitor {:chat_id chat-id
                               :symbol  (:symbol s)
                               :compare (:compare s)
                               :price   (:price s)}))
  (clear_session chat-id)
  {:text "등록완료\n(/list 모니터링 종목 리스트)"})

(comment
  (def request {:body {:message {:chat {:id 268911454} :text "/help"}}})
  (def chat-id (get-in request [:body :message :chat :id]))
  (def text (get-in request [:body :message :text]))

  (def chat-id 268911454)
  (handler "/help" chat-id)
  (get_session chat-id)
  (handler "/monitor" chat-id)
  (get_session chat-id)
  (handler "NotExists" chat-id)
  (handler "한국" chat-id)
  (handler "한국전력" chat-id)
  (handler "확인" chat-id)
  (get_session chat-id)
  (handler "33,000" chat-id)
  (handler "클때" chat-id)
  (get_session chat-id)
  (handler "No" chat-id)
  (get_session chat-id)
  (handler "34,000" chat-id)
  (get_session chat-id)
  (handler "Yes" chat-id)
  )
