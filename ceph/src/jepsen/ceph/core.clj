(ns jepsen.ceph.core
  (:require [clojure.tools.logging :refer :all]
            [clojure.java.io    :as io]
            [clojure.string     :as str]
            [jepsen [db         :as db]
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

(defn ceph-node-ids
  "Returns a map of node names to node ids."
  [test]
  (->> test
       :nodes
       (map-indexed (fn [i node] [node i]))
       (into {})))

(defn ceph-node-id
  "Given a test and a node name from that test, returns the ID for that node."
  [test node]
  ((ceph-node-ids test) node))

(defn db
  "ceph DB for a particular version."
  []
  (reify db/DB
    (setup! [_ test node]
      (c/su
        (info node "installing ceph" )

        (info node "id is" (ceph-node-id test node))))

    (teardown! [_ test node]
      (info node "tearing down ceph"))))


;;(defn r   [k] (c/exec :ceph :config-key :get k :-o :value :&& :cat :value :&& :echo :" ") )
(defn r   [k] (c/exec :ceph :config-key :get k) )
(defn w   [k v] (c/exec :ceph :config-key :put k v))

(defn -main [ ]
  ;[version]
  (assoc tests/noop-test
         :name    "ceph"
         :os      debian/os
         ;:db      db
         ;:db      (db version)
         ;:client  (client nil nil)
         ;:nemesis (nemesis/partition-random-halves)
         :generator (->> r
                         (gen/stagger 1)
                         ;(gen/clients)
                         (gen/time-limit 15)))
         ;:model   (model/set)
         ))

(comment
;& args
(defn -main []
  (println "Working!")
  tests/noop-test
  )
)

(comment
(defn ceph-test
  [version]
  (assoc tests/noop-test
         :name    "ceph"
         :os      debian/os
         ;:db      (db version)
         ;:client  (client nil nil)
         :nemesis (nemesis/partition-random-halves)
         :generator (->> (gen/mix [r w])
                         (gen/stagger 1)
                         (gen/nemesis
                           (gen/seq (cycle [(gen/sleep 5)
                                            {:type :info, :f :start}
                                            (gen/sleep 5)
                                            {:type :info, :f :stop}])))
                         (gen/time-limit 60))
         ;:model   (model/set)
         :checker (checker/compose
                    {:perf   (checker/perf)
                     :linear checker/linearizable})))
)
