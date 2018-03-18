(ns yquant.bot.handlers.list
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [clojure.java.jdbc :as jdbc]
            [yquant.bot.db :refer [db]]
            [yquant.bot.handler :refer [handler]]
            [yquant.bot.utils.session :as s]
            [yquant.bot.utils.stock :refer [search]]))

(defn format-row [row]
  (let [no (+ 1 (:index row))
        name (key (first (search (:symbol row))))
        price (format "%,d" (:price row))
        date (str/replace (subs (:created_at row) 5 10) "-" "/")]
    (str no ". " name " " (:compare row) " " price " " "(" date ")")))

(defmethod handler :list [message]
  (let [username (get-in message [:from :username])
        rows (jdbc/find-by-keys db :monitor {:username username})
        indexed (map-indexed (fn [idx row] (assoc row :index idx)) rows)]
    {:text (str "리스트\n"
                (str/join "\n" (for [row indexed] (format-row row))) "\n"
                "(번호 입력시 삭제)")}))

(comment
  (handler {:text "/list" :from {:username "yoonbae81"} :chat {:id 268911454}})
  (def rows (jdbc/find-by-keys db :holding {:username username}))
  (def row {:id         1,
            :chat_id    "268911454",
            :symbol     "015760",
            :price      40000,
            :created_at "2018-03-17 19:33:54"})
  (map-indexed (fn [idx row] (assoc row :no idx)) rows)
  )