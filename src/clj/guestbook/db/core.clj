(ns guestbook.db.core
  (:require
    [conman.core :as conman]
    [mount.core :refer [defstate]]
    [config.core :refer [env]]))

(def pool-spec
  {:datasource
   (doto (org.h2.jdbcx.JdbcDataSource.)
     (.setURL (:database-url env))
     (.setUser "")
     (.setPassword ""))})

(defstate ^:dynamic *db*
          :start (conman/connect! pool-spec)
          :stop (conman/disconnect! *db*))

(conman/bind-connection *db* "sql/queries.sql")

