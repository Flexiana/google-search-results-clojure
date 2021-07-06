(ns serpapi.schema-test
  (:require [serpapi.schema :as sc]
            [clojure.test :refer [deftest are]]))


(deftest param-transform
  (are [x y] (= x (sc/transform-params y))
    {"key-1" :val-1 "key2" "val2" "engine" "google" "key-3" 3 "key4" 4} {:key-1 :val-1 :key2 "val2" "key-3" 3 "key4" 4}
    {"engine" "baidu" "output" "xml"} {:engine :baidu :output :xml}
    {"output" "json"  "engine" "baidu"} {:output nil  :engine "baidu"}))

(deftest param-validation
  (are [x y] (= x (sc/validate-params y))
    nil {"q" "Coffee" "api_key" "key" "engine" "google"}
    {"api_key" ["missing required key"]
     "engine" ["missing required key"]
     "q" ["missing required key"]} {:q "Coffee"}))