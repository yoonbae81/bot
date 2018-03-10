(ns yquant.bot.handlers.list
  (:require [clojure.string     :as str]
            [clojure.edn        :as edn]
            [clojure.java.jdbc  :as jdbc]
            [yquant.bot.db      :refer [db]]
            [yquant.bot.handler :refer [handler]]
            [yquant.bot.utils.session :refer [get-session set-session clear-session]]
            [yquant.bot.utils.search  :refer [search]]))

(defn format-row [row]
  (let [no (+ 1 (:index row))
        name (key (first (search (:symbol row))))
        price (format "%,d" (:price row))
        date (str/replace (subs (:created_at row) 5 10) "-" "/")]
    (str no ". " name " " (:compare row) " " price " " "(" date ")")))

(defmethod handler :list [_ chat-id]
  (clear-session chat-id)
  (set-session chat-id {:mode :list})
  (let [rows (jdbc/find-by-keys db :monitor {:chat_id chat-id})
        indexed (map-indexed (fn [idx row] (assoc row :index idx)) rows)]
    {:text (str "모니터링 리스트\n"
                (str/join "\n" (for [row indexed] (format-row row))) "\n"
                "(번호 입력시 삭제)")}))

(comment
  (def chat-id 268911454)
  (def rows (jdbc/find-by-keys db :monitor {:chat_id chat-id}))
  (map-indexed (fn [idx row] (assoc row :no idx)) rows)
  (def row {:id 8,
            :chat_id "268911454",
            :symbol "015760",
            :compare ">",
            :price 33000,
            :created_at "2018-03-10 23:35:09",
            :notified_at nil})

  (handler "/list" chat-id)
  )