(defproject guestbook "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [selmer "0.9.5"]
                 [com.taoensso/timbre "4.1.4"]
                 [com.taoensso/tower "3.0.2"]
                 [markdown-clj "0.9.80"]
                 [environ "1.0.1"]
                 [compojure "1.4.0"]
                 [ring-webjars "0.1.1"]
                 [ring/ring-defaults "0.1.5"]
                 [ring "1.4.0"
                  :exclusions [ring/ring-jetty-adapter]]
                 [metosin/ring-middleware-format "0.6.0"]
                 [metosin/ring-http-response "0.6.5"]
                 [bouncer "0.3.3"]
                 [org.clojure/tools.nrepl "0.2.12"]
                 [org.webjars/bootstrap "3.3.5"]
                 [org.webjars/jquery "2.1.4"]
                 [com.fzakaria/slf4j-timbre "0.2.1"]
                 [migratus "0.8.7"]
                 [conman "0.2.6"]
                 [com.h2database/h2 "1.4.188"]
                 [org.immutant/web "2.1.1" :exclusions [ch.qos.logback/logback-classic]]]

  :min-lein-version "2.0.0"
  :uberjar-name "guestbook.jar"
  :jvm-opts ["-server"]

  :main guestbook.core
  :migratus {:store :database}

  :plugins [[lein-environ "1.0.1"]
            [migratus-lein "0.2.0"]]
  :profiles
  {:uberjar {:omit-source true
             :env {:production true}
             :aot :all
             :source-paths ["env/prod/clj"]}
   :dev           [:project/dev :profiles/dev]
   :test          [:project/test :profiles/test]
   :project/dev  {:dependencies [[prone "0.8.2"]
                                 [ring/ring-mock "0.3.0"]
                                 [ring/ring-devel "1.4.0"]
                                 [pjstadig/humane-test-output "0.7.0"]
                                 [mvxcvi/puget "1.0.0"]]
                  
                  
                  :source-paths ["env/dev/clj"]
                  :repl-options {:init-ns guestbook.core}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]
                  ;;when :nrepl-port is set the application starts the nREPL server on load
                  :env {:dev        true
                        :port       3000
                        :nrepl-port 7000
                        :log-level  :trace}}
   :project/test {:env {:test       true
                        :port       3001
                        :nrepl-port 7001
                        :log-level  :trace}}
   :profiles/dev {}
   :profiles/test {}})
