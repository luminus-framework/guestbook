(ns guestbook.models.db
  (:require [clojure.java.jdbc :as sql]))

(def db {:classname     "org.h2.Driver"
         :subprotocol   "h2"
         :user          "sa"
         :password      ""
         :subname       "site"})


(defn create-guestbook-table []
  (sql/with-connection
    db
    (sql/create-table
      :guestbook
      [:id "INTEGER PRIMARY KEY AUTO_INCREMENT"]
      [:timestamp :timestamp]
      [:name "varchar(30)"]
      [:message "varchar(200)"])
    (sql/do-commands
      "CREATE INDEX timestamp_index ON guestbook (timestamp)")))

(defn read-guests []
  (sql/with-connection
    db
    (sql/with-query-results res
      ["SELECT * FROM guestbook ORDER BY timestamp DESC"]
      (doall res))))

(defn save-message [name message]
  (sql/with-connection
    db
    (sql/insert-values
      :guestbook
      [:name :message :timestamp]
      [name message (new java.util.Date)])))

