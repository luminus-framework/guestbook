(ns guestbook.routes.home
  (:use compojure.core hiccup.form)
  (:require [guestbook.views.layout :as layout]
            [guestbook.models.db :as db]))


(defn show-guests []
  (into [:ul.guests]
        (for [{:keys [message name timestamp]} (db/read-guests)]
          [:li
           [:blockquote message]
           [:p "-" [:cite name]]
           [:time timestamp]])))


(defn home-page [& [name message error]]
  (layout/common
    [:h1 "Guestbook"]
    [:p "Welcome to my guestbook"]
    [:p error]
 
    ;here we call our show-guests function
    ;to generate the list of existing comments
    (show-guests)
 
    [:hr]
 
    (form-to [:post "/"]
      [:p "Name:" (text-field "name" name)]
      [:p "Message:" (text-area {:rows 10 :cols 40} "message" message)]
      (submit-button "comment"))))

(defn save-message [name message]
  (cond
 
    (empty? name)
    (home-page name message "Some dummy who forgot to leave a name")
 
    (empty? message)
    (home-page name message "Don't you have something to say?")
 
    :else
    (do
      (db/save-message name message)
      (home-page))))

(defn about-page []
  (layout/common
   "this is the story of guestbook... work in progress"))

(defroutes home-routes 
  (GET "/" [] (home-page))
  (GET "/"  [name message error] (home-page name message error))
  (POST "/" [name message] (save-message name message))
  (GET "/about" [] (about-page)))