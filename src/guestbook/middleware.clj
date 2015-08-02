(ns guestbook.middleware
  (:require [guestbook.layout :refer [*servlet-context*]]
            [taoensso.timbre :as timbre]
            [environ.core :refer [env]]
            [clojure.java.io :as io]
            [selmer.middleware :refer [wrap-error-page]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring-ttl-session.core :refer [ttl-memory-store]]
            [ring.util.response :refer [redirect]]
            [ring.middleware.reload :as reload]
            [ring.middleware.webjars :refer [wrap-webjars]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
            [ring.middleware.format :refer [wrap-restful-format]]))

(defn wrap-servlet-context [handler]
  (fn [request]
    (binding [*servlet-context*
              (if-let [context (:servlet-context request)]
                ;; If we're not inside a servlet environment
                ;; (for example when using mock requests), then
                ;; .getContextPath might not exist
                (try (.getContextPath context)
                     (catch IllegalArgumentException _ context)))]
      (handler request))))

(defn wrap-internal-error [handler]
  (fn [req]
    (try
      (handler req)
      (catch Throwable t
        (timbre/error t)
        {:status 500
         :headers {"Content-Type" "text/html"}
         :body (-> "templates/error.html" io/resource slurp)}))))

(defn wrap-dev [handler]
  (if (env :dev)
    (-> handler
        reload/wrap-reload
        wrap-error-page
        wrap-exceptions)
    handler))

(defn wrap-csrf [handler]
  (wrap-anti-forgery handler))

(defn wrap-formats [handler]
  (wrap-restful-format handler {:formats [:json-kw :transit-json :transit-msgpack]}))

(defn wrap-base [handler]
  (-> handler
      wrap-dev
      wrap-formats
      (wrap-defaults
        (-> site-defaults
            (assoc-in [:security :anti-forgery] false)
            (assoc-in  [:session :store] (ttl-memory-store (* 60 30)))))
      wrap-webjars
      wrap-servlet-context
      wrap-internal-error))
