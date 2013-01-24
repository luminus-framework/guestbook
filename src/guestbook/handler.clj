(ns guestbook.handler
  (:use guestbook.routes.home
        compojure.core)
  (:require [noir.util.middleware :as middleware]
            [compojure.route :as route]
            [guestbook.models.db :as db]))

(defroutes app-routes  
  (route/resources "/")
  (route/not-found "Not Found"))

(defn init
  "init will be called once when
   app is deployed as a servlet on 
   an app server such as Tomcat
   put any initialization code here"
  []
  (if-not (.exists (new java.io.File "site.h2.db"))
    (db/create-guestbook-table))
  (println "guestbook started successfully..."))

(defn destroy []
  (println "shutting down..."))

;;append your application routes to the all-routes vector
(def all-routes [home-routes app-routes])
(def app (middleware/app-handler all-routes))
(def war-handler (middleware/war-handler app))
