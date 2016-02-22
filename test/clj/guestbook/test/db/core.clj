(ns guestbook.test.db.core
  (:require [guestbook.db.core :refer [*db*] :as db]
            [guestbook.db.migrations :as migrations]
            [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [config.core :refer [env]]
            [conman.core :refer [with-transaction]]
            [mount.core :as mount]))

(use-fixtures
  :once
  (fn [f]
    (mount/start #'guestbook.db.core/*db*)
    (migrations/migrate ["migrate"])
    (f)))

(deftest test-users
  (jdbc/with-db-transaction [t-conn *db*]
      (jdbc/db-set-rollback-only! t-conn)
      (let [message {:name "test"
                     :message "test"
                     :timestamp (java.util.Date.)}]
        (is (= 1 (db/save-message! t-conn message)))
        (is (= [(assoc message :id 1)] (db/get-messages t-conn {})))))
  (is (empty? (db/get-messages))))
