(ns serpapi.schema-test
  (:require [serpapi.schema :as sc]
            [clojure.test :refer [deftest are is]]))


(deftest param-transform-search-params
  (are [x y] (= x (sc/transform-params sc/SearchParams y))
    {"key-1" :val-1 "key2" "val2" "engine" "google" "key-3" 3 "key4" 4} {:key-1 :val-1 :key2 "val2" "key-3" 3 "key4" 4}
    {"engine" "baidu" "output" "xml"} {:engine :baidu :output :xml}
    {"output" "json"  "engine" "baidu"} {:output nil  :engine "baidu"}))

(deftest param-validation-search-params
  (are [x y] (= x (sc/validate-params sc/SearchParams y))
    nil {"q" "Coffee" "api_key" "key" "engine" "google"}
    {"api_key" ["missing required key"]
     "engine" ["missing required key"]
     "q" ["missing required key"]} {:q "Coffee"}))

(deftest param-transform-archive-params
  (are [x y] (= x (sc/transform-params sc/SearchArchiveParams y))
    {"key-1" :val-1 "key2" "val2" "key-3" 3 "key4" 4} {:key-1 :val-1 :key2 "val2" "key-3" 3 "key4" 4}
    {"output" "xml"} {:output :xml}
    {"output" "json"} {:output nil}))

(deftest param-validation-archive-params
  (are [x y] (= x (sc/validate-params sc/SearchArchiveParams y))
    nil {"api_key" "key" }
    {"api_key" ["missing required key"]} {:q "Coffee"}))

(deftest param-transform-account-params
  (is (=
         {"key-1" :val-1 "key2" "val2" "key-3" 3 "key4" 4} 
       (sc/transform-params sc/AccountParams {:key-1 :val-1 :key2 "val2" "key-3" 3 "key4" 4}))))

(deftest param-validation-account-params
  (are [x y] (= x (sc/validate-params sc/AccountParams y))
    nil { "api_key" "key" }
    {"api_key" ["missing required key"]} {:q "Coffee"}))