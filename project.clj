(defproject
  guestbook "0.1.0-SNAPSHOT"
  :url "https://github.com/yogthos/guestbook"
  :description "sample guestbook application"
  :dependencies
  [[com.h2database/h2 "1.4.178"]
   [ring-server "0.3.1"]
   [environ "0.5.0"]
   [com.taoensso/timbre "3.2.1"]
   [markdown-clj "0.9.44"]
   [korma "0.3.1"]
   [com.taoensso/tower "2.0.2"]
   [selmer "0.6.6"]
   [org.clojure/clojure "1.6.0"]
   [log4j
    "1.2.17"
    :exclusions
    [javax.mail/mail
     javax.jms/jms
     com.sun.jdmk/jmxtools
     com.sun.jmx/jmxri]]
   [compojure "1.1.8"]
   [lib-noir "0.8.3"]]

  :plugins
  [[lein-ring "0.8.7"] [lein-environ "0.5.0"]]

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
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.2.2"]],
    :env {:dev true}}}

  :min-lein-version "2.0.0")
