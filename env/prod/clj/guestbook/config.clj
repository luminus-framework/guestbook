(ns guestbook.config
  (:require [taoensso.timbre :as timbre]))

(def defaults
  {:init
   (fn []
     (timbre/info "\n-=[guestbook started successfully]=-"))
   :middleware identity})
