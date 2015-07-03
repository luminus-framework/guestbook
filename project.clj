(defproject guestbook "0.1.0"

  :description "Luminus Guestbook example"
  :url "https://github.com/luminus-framework/guestbook"

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [selmer "0.8.2"]
                 [com.taoensso/timbre "4.0.2"]
                 [com.taoensso/tower "3.0.2"]
                 [markdown-clj "0.9.67"]
                 [environ "1.0.0"]
                 [compojure "1.3.4"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-session-timeout "0.1.0"]
                 [metosin/ring-middleware-format "0.6.0"]
                 [metosin/ring-http-response "0.6.2"]
                 [bouncer "0.3.3"]
                 [prone "0.8.2"]
                 [org.clojure/tools.nrepl "0.2.10"]
                 [cc.qbits/jet "0.6.5"]
                 [ring "1.4.0-RC2"
                  :exclusions [ring/ring-jetty-adapter]]
                 [ring-server "0.4.0"]
                 [migratus "0.8.2"]
                 [org.clojure/java.jdbc "0.3.7"]
                 [instaparse "1.4.1"]
                 [yesql "0.5.0-rc3"]
                 [com.h2database/h2 "1.4.187"]]

  :min-lein-version "2.0.0"
  :uberjar-name "guestbook.jar"
  :jvm-opts ["-server"]

;;enable to start the nREPL server when the application launches
;:env {:repl-port 7001}

  :main guestbook.core

  :plugins [[lein-ring "0.9.1"]
            [lein-environ "1.0.0"]
            [lein-ancient "0.6.5"]]

  :ring {:handler guestbook.handler/app
         :init    guestbook.handler/init
         :destroy guestbook.handler/destroy
         :uberwar-name "guestbook.war"}

  :profiles
  {:uberjar {:omit-source true
             :env {:production true}

             :aot :all}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.3.2"]
                        [pjstadig/humane-test-output "0.7.0"]
                        ]
         :source-paths ["env/dev/clj"]
         :repl-options {:init-ns guestbook.core}
         :injections [(require 'pjstadig.humane-test-output)
                      (pjstadig.humane-test-output/activate!)]
         :env {:dev true}}})
