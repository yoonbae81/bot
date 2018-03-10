(ns yquant.bot.handlers.default
  (:require [clojure.string     :as str]
            [clojure.edn        :as edn]
            [clojure.java.jdbc  :as jdbc]
            [yquant.bot.db      :refer [db]]
            [yquant.bot.handler :refer [handler]]
            [yquant.bot.utils.session :refer [get_session set_session clear_session]]
            [yquant.bot.utils.search  :refer [search]]))

(defmethod handler :default [text chat-id]
  (clear_session chat-id)
  {:text         (str/join "\n"
                           ["*제공기능*"
                            "/list 모니터링 종목 리스트"
                            "/mon 모니터링 종목 추가"])
   :reply_markup {:keyboard          [["/list"]
                                      ["/mon"]]
                  :one_time_keyboard true}})