(ns yquant.bot.utils.session)

(def storage (atom {}))

(defn- keywordize [chat-id]
  (-> (str chat-id)
      (keyword)))

(defn get_session [chat-id]
  (let [k (keywordize chat-id)]
    (k @storage)))

(defn set_session [chat-id new-map]
  (let [current (get_session chat-id)]
    (swap! storage assoc
           (keywordize chat-id)
           (merge current new-map))))

(defn clear_session [chat-id]
  (swap! storage dissoc (keywordize chat-id)))
