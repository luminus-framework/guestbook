(ns guestbook.db.core
  (:require
    [clojure.java.jdbc :as jdbc]
    [yesql.core :refer [defqueries]]
    [taoensso.timbre :as timbre]
    [environ.core :refer [env]])
  (:import java.sql.BatchUpdateException))

(defonce conn (atom nil))

(defqueries "sql/queries.sql")


(defn connect! []
  (try
    (reset!
      conn
      {:classname   "org.h2.Driver"
       :connection-uri (:database-url env)
       :make-pool?     true
       :naming         {:keys   clojure.string/lower-case
                        :fields clojure.string/upper-case}})
    (catch Exception e
      (timbre/error "Error occured while connecting to the database!" e))))

(defn disconnect! [])

(defn run
  "executes a Yesql query using the given database connection and parameter map
  the parameter map defaults to an empty map and the database conection defaults
  to the conn atom"
  ([query-fn] (run query-fn {}))
  ([query-fn query-map] (run query-fn query-map @conn))
  ([query-fn query-map db]
   (try
     (query-fn query-map {:connection db})
     (catch BatchUpdateException e
       (throw (or (.getNextException e) e))))))
