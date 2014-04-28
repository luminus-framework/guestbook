(defproject
  guestbook "0.1.0-SNAPSHOT"
  :url "https://github.com/yogthos/guestbook"
  :description "sample guestbook application"
  :dependencies
  [[com.h2database/h2 "1.3.174"]
   [ring-server "0.3.1"]
   [environ "0.4.0"]
   [com.taoensso/timbre "2.7.1"]
   [markdown-clj "0.9.40"]
   [korma "0.3.0-RC6"]
   [com.taoensso/tower "2.0.0"]
   [selmer "0.5.7"]
   [org.clojure/clojure "1.5.1"]
   [log4j
    "1.2.17"
    :exclusions
    [javax.mail/mail
     javax.jms/jms
     com.sun.jdmk/jmxtools
     com.sun.jmx/jmxri]]
   [compojure "1.1.6"]
   [lib-noir "0.7.9"]
   [com.postspectacular/rotor "0.1.0"]]

  :plugins
  [[lein-ring "0.8.7"] [lein-environ "0.4.0"]]

  :ring
  {:handler guestbook.handler/app,
   :init guestbook.handler/init,
   :destroy guestbook.handler/destroy}

  :profiles
  {:uberjar {:aot :all}
   :production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}},
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.2.1"]],
    :env {:selmer-dev true}}}

  :min-lein-version "2.0.0")
