(ns yquant.bot.handlers.default
  (:require [clojure.string     :as str]
            [clojure.edn        :as edn]
            [clojure.java.jdbc  :as jdbc]
            [yquant.bot.db      :refer [db]]
            [yquant.bot.handler :refer [handler]]
            [yquant.bot.utils.session :refer [get-session set-session clear-session]]
            [yquant.bot.utils.search  :refer [search]]))

(defmethod handler :default [text chat-id]
  (clear-session chat-id)
  {:text         (str/join "\n"
                           ["*제공기능*"
                            "/list 모니터링 리스트"
                            "/mon 모니터링 추가"])
   :reply_markup {:keyboard          [["/list"]
                                      ["/mon"]]
                  :one_time_keyboard true}})