(ns jepsen.ceph.core-test
  (:require [clojure.test :refer :all]
            [jepsen.core :as jepsen]
            ;;[jepsen.ceph.core :refer :all]))
            [jepsen.ceph.core :as ceph]))

;;(deftest a-test
  ;;(testing "FIXME, I fail."
    ;;(is (= 0 1))))

(deftest ceph-test
;( :results (jepsen/run! (ceph/-main))))
(let [test (jepsen/run! (ceph/-main))]
    (is (:valid? (:results test)))))
  ;(is (:valid? (:results (range 5)))))
