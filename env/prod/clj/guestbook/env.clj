(ns guestbook.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[guestbook started successfully]=-"))
   :middleware identity})
