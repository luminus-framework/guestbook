(ns guestbook.core
  (:require [guestbook.handler :refer [app init destroy]]
            [immutant.web :as immutant]
            [guestbook.db.migrations :as migrations]
            [clojure.tools.nrepl.server :as nrepl]
            [taoensso.timbre :as timbre]
            [environ.core :refer [env]])
  (:gen-class))

(defonce nrepl-server (atom nil))

(defn parse-port [port]
  (when port
    (cond
      (string? port) (Integer/parseInt port)
      (number? port) port
      :else          (throw (Exception. (str "invalid port value: " port))))))

(defn stop-nrepl []
  (when-let [server @nrepl-server]
    (nrepl/stop-server server)))

(defn start-nrepl
  "Start a network repl for debugging when the :nrepl-port is set in the environment."
  []
  (if @nrepl-server
    (timbre/error "nREPL is already running!")
    (when-let [port (env :nrepl-port)]
      (try
        (->> port
             (parse-port)
             (nrepl/start-server :port)
             (reset! nrepl-server))
        (timbre/info "nREPL server started on port" port)
        (catch Throwable t
          (timbre/error t "failed to start nREPL"))))))

(defn http-port [port]
  (parse-port (or port (env :port) 3000)))

(defonce http-server (atom nil))

(defn start-http-server [port]
  (init)
  (reset! http-server (immutant/run app :host "0.0.0.0" :port port)))

(defn stop-http-server []
  (when @http-server
    (destroy)
    (immutant/stop @http-server)
    (reset! http-server nil)))

(defn stop-app []
  (stop-nrepl)
  (stop-http-server)
  (shutdown-agents))

(defn start-app [[port]]
  (.addShutdownHook (Runtime/getRuntime) (Thread. stop-app))
  (start-nrepl)
  (start-http-server (http-port port))
  (timbre/info "server started on port:" (:port @http-server)))

(defn -main [& args]
  (cond
    (some #{"migrate" "rollback"} args) (migrations/migrate args)
    :else (start-app args)))
  
