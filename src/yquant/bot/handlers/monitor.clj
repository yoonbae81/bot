(ns yquant.bot.handlers.monitor
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [clojure.java.jdbc :as jdbc]
            [yquant.bot.db :refer [db]]
            [yquant.bot.handler :refer [handler]]
            [yquant.bot.utils.session :as s]
            [yquant.bot.utils.stock :refer [search]]))

(defmethod handler :monitor [message]
  ((get-method handler :monitor-stock-input) message))

(defmethod handler :monitor-stock-input [message]
  (s/set-next-handler message :monitor-stock-search)
  {:text "*모니터링할 종목은?*\n(종목명/종목코드 입력가능)"})

(defmethod handler :monitor-stock-search [message]
  (let [text (:text message)
        result (search text)]
    (case (count result)
      0 {:text "검색된 종목이 없음\n*모니터링할 종목은?*"}
      1 (let [stock (first result)]
          (s/add message {:symbol (val stock)})
          (s/set-next-handler message :monitor-stock-confirm)
          {:text         (str "종목명 : " (key stock) "\n"
                              "종목코드 : " (val stock))
           :reply_markup {:keyboard          [["확인"] ["재입력"]]
                          :one_time_keyboard true}})
      {:text (str "검색결과 : " (count result) "종목\n"
                  (str/join "\n" (keys result))
                  "\n*모니터링할 종목은?*")})))

(defmethod handler :monitor-stock-confirm [message]
  (let [text (:text message)]
    (if (= text "확인")
      ((get-method handler :monitor-price-input) message)
      ((get-method handler :monitor-stock-input) message))))

(defmethod handler :monitor-price-input [message]
  (s/set-next-handler message :monitor-price-parse)
  {:text "*모니터링 목표가는?*"})

(defmethod handler :monitor-price-parse [message]
  (let [text (:text message)
        price (-> text
                  (str/replace #"^0+" "")
                  (str/replace #"\D" "")
                  (edn/read-string))]
    (if (nil? price)
      ((get-method handler :monitor-price-input) message)
      (do
        (s/add message {:price price})
        (s/set-next-handler message :monitor-price-confirm)
        {:text         (str "*알림을 받을 시점은?*\n"
                            "목표가 " (format "%,d" price) "원 대비")
         :reply_markup {:keyboard          [["클때"]
                                            ["작을때"]
                                            ["재입력"]]
                        :one_time_keyboard true}}))))

(defmethod handler :monitor-price-confirm [message]
  (let [text (:text message)]
    (if (or (= text "클때") (= text "작을때"))
      (let [compare (if (= text "클때") ">" "<")]
        (s/add message {:compare compare})
        ((get-method handler :monitor-result) message))
      ((get-method handler :monitor-price-input) message))))

(defmethod handler :monitor-result [message]
  (let [s (s/get-by message)]
    (jdbc/insert! db :monitor {:username (get-in message [:from :username])
                               :symbol  (:symbol s)
                               :compare (:compare s)
                               :price   (:price s)}))
  (s/clear message)
  {:text "등록완료\n(/list 리스트)"})

(comment
  (handler {:text "/monitor" :from {:username "yoonbae81"} :chat {:id 268911454}})
  (s/get-by {:text "/monitor" :from {:username "yoonbae81"} :chat {:id 268911454}})
  (handler {:text "한국전력" :from {:username "yoonbae81"} :chat {:id 268911454}})
  (handler {:text "확인" :from {:username "yoonbae81"} :chat {:id 268911454}})
  (handler {:text "35000" :from {:username "yoonbae81"} :chat {:id 268911454}})
  )
