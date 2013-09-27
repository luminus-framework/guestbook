(ns guestbook.views.layout
  (:use noir.request)
  (:require [selmer.parser :as parser]
    [ring.util.response :refer [content-type response]]))

(def template-path "guestbook/views/templates/")

(deftype RenderableTemplate [template params]
  Renderable
  (render [this request]
    (content-type
      (->> (assoc params :servlet-context (:context request))
        (parser/render-file (str template-path template))
        response)
      "text/html; charset=utf-8")))

(defn render [template & [params]]
  (RenderableTemplate. template params))
