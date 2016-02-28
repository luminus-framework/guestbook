(ns user
  (:require [guestbook.handler :refer [app init destroy]]
            [mount.core :as mount]
            [guestbook.config :refer [env]]
            guestbook.core))

(defn start []
  (mount/start-without #'guestbook.core/repl-server))

(defn stop []
  (mount/stop-except #'guestbook.core/repl-server))

(defn restart []
  (stop)
  (start))


