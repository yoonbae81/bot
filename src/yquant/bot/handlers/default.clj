(ns yquant.bot.handlers.default
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [clojure.java.jdbc :as jdbc]
            [yquant.bot.db :refer [db]]
            [yquant.bot.handler :refer [handler]]
            [yquant.bot.utils.session :as s]
            [yquant.bot.utils.stock :refer [search]]))

(defmethod handler :default [message]
  {:text         (str/join "\n"
                           ["*제공기능*"
                            "/list 리스트"
                            "/holding 보유종목 추가"
                            "/monitor 모니터링 추가"])
   :reply_markup {:keyboard          [["/list"]
                                      ["/holding"]
                                      ["/monitor"]]
                  :one_time_keyboard true}})