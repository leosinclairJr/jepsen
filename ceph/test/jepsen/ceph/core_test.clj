(ns jepsen.ceph.core-test
  (:require [clojure.test :refer :all]
            ;;[jepsen.ceph.core :refer :all]))
            [jepsen.ceph.core :as ceph]))

;;(deftest a-test
  ;;(testing "FIXME, I fail."
    ;;(is (= 0 1))))

(deftest ceph-test
  (is (:valid? (:results (jepsen/run! (jepsen/ceph/ceph-test ""))))))
