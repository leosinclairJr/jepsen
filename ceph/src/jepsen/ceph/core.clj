(ns jepsen.ceph.core
  (:require [avout.core :as avout]
	    [clojure.tools.logging :refer :all]
            [clojure.java.io    :as io]
            [clojure.string     :as str]
            [jepsen [db         :as db]
                    [checker    :as checker]
                    [client     :as client]
                    [control    :as c]
                    [generator  :as gen]
                    [nemesis    :as nemesis]
                    [tests      :as tests]
                    ;;[model      :as model]
                    [util       :refer [timeout]]
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

(comment
(defn client
  "A client for a single compare-and-set register"
 ; [conn a]
  [a]
  (reify client/Client
    (setup! [_ test node]
      (let [;conn (avout/connect (name node))
           ; a    (avout/zk-atom conn "/jepsen" 0)]
	  a  0]
        (client conn a)))

    ;(invoke! [this test op])
    
    (invoke! [this test op]
      (timeout 5000 (assoc op :type :info, :error :timeout)
               (case (:f op)
                ; :read (assoc op :type :ok, :value @a)
                ; :write (do (avout/reset!! a (:value op)) (assoc op :type :ok))
	        :read (c/exec :ceph :config-key :get @a :-o :value @a :&& :cat @a)
	        )))

    (teardown! [_ test]
      (.close conn))))
)
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


;(defnr   [k] (c/exec :ceph :config-key :get k :-o :value :&& :cat :value :&& :echo :" ") )
;(defn r   [k] (c/exec :ceph :config-key :get k :-o :value :&& :cat :value))
;(defn w   [k v] (c/exec :ceph :config-key :put k v))
(defn r   [_ _] {:type :info, :value nil})
(defn w   [_ _] {:type :ok, :value (rand-int 5)})

(defn -main []
  ;[version]
  (println "main is executed 1")
  (assoc tests/noop-test
         :name    "ceph"
         :os      debian/os
         :generator (->> r
                         (gen/stagger 1)
                        ; (gen/clients)
                         (gen/time-limit 15))        
  ) (println "main is executed 2")
)


(comment
;& args
(defn -main [str]
  (println "Working!")
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
