(ns yquant.bot.utils.session)

(def storage (atom {}))

(defn- keywordize [username]
  (-> (str username)
      (keyword)))

(defn- parse-username
  "Return :username after parsing from input"
  [input]
  (cond
    (keyword? input) input
    (string? input) (keywordize input)
    (map? input) (-> (get-in input [:from :username])
                     keywordize))
  )

(defn get-by
  "Return session corresponding username given as string or message"
  [session-key]
  (get @storage (parse-username session-key)))

(defn add [session-key new-map]
  (let [username (parse-username session-key)
        current (get-by username)]
    (swap! storage assoc
           username (merge current new-map))))

(defn clear [session-key]
  (swap! storage dissoc (parse-username session-key)))

(defn set-available-pages [session-key new-map]
  (add session-key {:available-pages new-map}))

(defn set-next-handler
  "Set current page with additional parameters and return passed page"
  ([session-key next]
   (let [param (get (:available-pages session-key) next)]
     (set-next-handler session-key next param)))
  ([session-key next param]
   (add session-key {:handler next :param param})
    next))
