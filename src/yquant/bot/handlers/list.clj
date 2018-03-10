(ns yquant.bot.handlers.list
  (:require [clojure.string     :as str]
            [clojure.edn        :as edn]
            [clojure.java.jdbc  :as jdbc]
            [yquant.bot.db      :refer [db]]
            [yquant.bot.handler :refer [handler]]
            [yquant.bot.utils.session :refer [get_session set_session clear_session]]
            [yquant.bot.utils.search  :refer [search]]))

(defmethod handler :list [_ chat-id]
  (clear_session chat-id)
  {:text (jdbc/find-by-keys db :monitor {:chat_id chat-id})})
