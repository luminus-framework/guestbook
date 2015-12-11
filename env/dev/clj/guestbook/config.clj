(ns guestbook.config
  (:require [selmer.parser :as parser]
            [taoensso.timbre :as timbre]
            [guestbook.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (timbre/info "\n-=[guestbook started successfully using the development profile]=-"))
   :middleware wrap-dev})
