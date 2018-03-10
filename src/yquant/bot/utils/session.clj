(ns yquant.bot.utils.session)

(def storage (atom {}))

(defn- keywordize [chat-id]
  (-> (str chat-id)
      (keyword)))

(defn get-session [chat-id]
  (let [k (keywordize chat-id)]
    (k @storage)))

(defn set-session [chat-id new-map]
  (let [current (get-session chat-id)]
    (swap! storage assoc
           (keywordize chat-id)
           (merge current new-map))))

(defn clear-session [chat-id]
  (swap! storage dissoc (keywordize chat-id)))
