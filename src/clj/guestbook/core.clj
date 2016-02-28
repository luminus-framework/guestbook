(ns guestbook.core
  (:require [guestbook.handler :refer [app init destroy]]
            [luminus.repl-server :as repl]
            [luminus.http-server :as http]
            [guestbook.db.migrations :as migrations]
            [guestbook.config :refer [env]]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.tools.logging :as log]
            [mount.core :as mount])
  (:gen-class))

(def cli-options
  [["-p" "--port PORT" "Port number"
    :parse-fn #(Integer/parseInt %)]])

(mount/defstate http-server
                :start
                (http/start
                  {:handler app
                   :init    init
                   :port    (or (-> env :options :port)
                                (:port env))})
                :stop
                (http/stop http-server destroy))

(mount/defstate repl-server
                :start
                (when-let [nrepl-port (env :nrepl-port)]
                  (repl/start {:port nrepl-port}))
                :stop
                (when repl-server
                  (repl/stop repl-server)))

(defn stop-app []
  (doseq [component (:stopped (mount/stop))]
    (log/info component "stopped"))
  (shutdown-agents))

(defn start-app [args]
  (doseq [component (-> args
                        (parse-opts cli-options)
                        mount/start-with-args
                        :started)]
    (log/info component "started"))
  (.addShutdownHook (Runtime/getRuntime) (Thread. stop-app)))

(defn -main [& args]
  (cond
    (some #{"migrate" "rollback"} args)
    (do
      (mount/start #'guestbook.config/env)
      (migrations/migrate args)
      (System/exit 0))
    :else
    (start-app args)))

