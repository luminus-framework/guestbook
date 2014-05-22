(ns guestbook.db.core
  (:use korma.core
        [korma.db :only (defdb)])
  (:require [guestbook.db.schema :as schema]))

(defdb db schema/db-spec)

(defentity guestbook)

(defn save-message
  [name message]
  (insert guestbook
          (values {:name name
                   :message message
                   :timestamp (new java.util.Date)})))

(defn get-messages []
  (select guestbook))
