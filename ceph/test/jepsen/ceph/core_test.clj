(ns jepsen.ceph.core-test
  (:require [clojure.test :refer :all]
            [jepsen.core :as jepsen]
            ;;[jepsen.ceph.core :refer :all]))
            [jepsen.ceph.core :as ceph]))

;;(deftest a-test
  ;;(testing "FIXME, I fail."
    ;;(is (= 0 1))))

(deftest ceph-test
  (jepsen/run! (ceph/-main nil)))
  ;(is (:valid? (:results (range 5)))))
