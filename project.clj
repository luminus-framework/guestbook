(defproject guestbook "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [ring-server "0.3.1"]
                 [selmer "0.8.0"]
                 [com.taoensso/timbre "3.3.1"]
                 [com.taoensso/tower "3.0.2"]
                 [markdown-clj "0.9.62"]
                 [environ "1.0.0"]
                 [im.chit/cronj "1.4.3"]
                 [ring/ring-defaults "0.1.3"]
                 [ring/ring-session-timeout "0.1.0"]
                 [ring-middleware-format "0.4.0"]
                 [noir-exception "0.2.3"]
                 [crypto-password "0.1.3"]
                 [bouncer "0.3.1"]
                 [prone "0.8.0"]
                 [ragtime "0.3.8"]
                 [yesql "0.5.0-rc1"]
                 [com.h2database/h2 "1.4.182"]]

  :min-lein-version "2.0.0"
  :uberjar-name "guestbook.jar"
  :repl-options {:init-ns guestbook.handler}
  :jvm-opts ["-server"]



  :plugins [[lein-ring "0.9.0"]
            [lein-environ "1.0.0"]
            [lein-ancient "0.5.5"]
            [ragtime/ragtime.lein "0.3.8"]]

  :ring {:handler guestbook.handler/app
         :init    guestbook.handler/init
         :destroy guestbook.handler/destroy
         :uberwar-name "guestbook.war"}

  :ragtime
  {:migrations ragtime.sql.files/migrations
   :database "jdbc:h2:./site.db"}

  :profiles
  {:uberjar {:omit-source true
             :env {:production true}

             :aot :all}
   :production {:ring {:open-browser? false
                       :stacktraces?  false
                       :auto-reload?  false}}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.3.2"]
                        [pjstadig/humane-test-output "0.6.0"]
                        ]

         :injections [(require 'pjstadig.humane-test-output)
                      (pjstadig.humane-test-output/activate!)]
         :env {:dev true}}})
