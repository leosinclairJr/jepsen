(ns jepsen.ceph.core
  (:require [clojure.tools.logging :refer :all]
            [clojure.java.io    :as io]
            [clojure.string     :as str]
            [jepsen ;;[db         :as db]
                    [checker    :as checker]
                    ;;[client     :as client]
                    [control    :as c]
                    [generator  :as gen]
                    [nemesis    :as nemesis]
                    [tests      :as tests]
                    ;;[model      :as model]
                    ;;[util       :refer [timeout]]
             ]
            [jepsen.os.debian   :as debian]
            [knossos.model      :as model]))

;;(defn r   [k] (c/exec :ceph :config-key :get k :-o :value :&& :cat :value :&& :echo :" ") )
;(defn r   [k] (c/exec :ceph :config-key :get k) )
;(defn w   [k v] (c/exec :ceph :config-key :put k v))


(defn ceph-test
  [version]
  tests/noop-test)
  ;(assoc tests/noop-test
         ;:name    "ceph"
         ;:os      debian/os
         ;;:db      (db version)
         ;;:client  (client nil nil)
         ;:nemesis (nemesis/partition-random-halves)
         ;:generator (->> (gen/mix [r w])
                         ;(gen/stagger 1)
                         ;(gen/nemesis
                           ;(gen/seq (cycle [(gen/sleep 5)
                                            ;{:type :info, :f :start}
                                            ;(gen/sleep 5)
                                            ;{:type :info, :f :stop}])))
                         ;(gen/time-limit 60))
         ;;:model   (model/set)
         ;:checker (checker/compose
                    ;{:perf   (checker/perf)
                     ;:linear checker/linearizable})))
