(ns serpapi.core-test
  (:require [serpapi.core :as sa]
            serpapi.test-helpers
            [clj-http.fake :refer [with-fake-routes]]
            [cheshire.core :as json]
            [clojure.test :refer [is deftest are run-tests]]))


(deftest set-global-api-key
  (let [local-api-key (atom "")]
    (with-redefs [sa/api-key local-api-key]
      (sa/set-api-key "new-api-key")
      (is (= "new-api-key" @local-api-key)))))


(deftest search-test
  (let [local-api-key (atom "")]
    (with-redefs [sa/search-request identity
                  sa/api-key local-api-key]
      (sa/set-api-key "new-api-key")
      (are [x y] (= y (sa/search x))
        {:q "Coffee"} {"q" "Coffee" "api_key" "new-api-key" "engine" "google"}
        {"q" "Coffee"} {"q" "Coffee" "api_key" "new-api-key" "engine" "google"}
        {"q" "Coffee" :api_key "another-api-key"} {"q" "Coffee" "api_key" "another-api-key" "engine" "google"}
        {"q" "Coffee" :api_key "another-api-key" :engine "baidu"} {"q" "Coffee" "api_key" "another-api-key" "engine" "baidu"}))))

(deftest search-integration
  (let [res {:result "Searches have results"}
        json-response (json/encode res)
        html-response "<h1>Searches have results</h1>"]
    (with-fake-routes
      {{:address "https://serpapi.com/search"
        :query-params {"q" "Coffee" "api_key" "api-key" "engine" "google"}}
       (fn [_]  {:status 200 :headers {} :body json-response})
       {:address "https://serpapi.com/search"
        :query-params {"q" "Coffee" "api_key" "api-key" "engine" "google" "output" "json"}}
       (fn [_]  {:status 200 :headers {} :body json-response})
       {:address "https://serpapi.com/search"
        :query-params {"q" "Coffee" "api_key" "api-key" "engine" "google" "output" "html"}}
       (fn [_]  {:status 200 :headers {} :body html-response})
       }
      (are [x y] (= x  y)
        res (sa/edn-search {"q" "Coffee" "api_key" "api-key" "engine" "google"})
        html-response (sa/html-search {"q" "Coffee" "api_key" "api-key" "engine" "google"})
        json-response (sa/json-search {"q" "Coffee" "api_key" "api-key" "engine" "google"})
        json-response (sa/search {"q" "Coffee" "api_key" "api-key" "engine" "google"})))))


(deftest search-throws-with-wrong-params
  (let [local-api-key (atom "")]
    (with-redefs [sa/search-request identity
                  sa/api-key local-api-key]
      (sa/set-api-key "new-api-key")
      (are [x y] (thrown-with-data? y  (sa/search x))
        {} {"q" ["missing required key"]}
        {:engine :wrong-engine} {"q" ["missing required key"] "engine" ["should be either google, baidu, bing, yahoo, yandex or ebay"]}
        {:engine :wrong-engine :q "Coffee"} {"engine" ["should be either google, baidu, bing, yahoo, yandex or ebay"]}))))

(deftest search-throws-without-api-key
  (with-redefs [sa/search-request identity]
    (are [x y] (thrown-with-data? y (sa/search x))
      {} {"q" ["missing required key"] "api_key" ["should be at least 1 characters"]}
      {:q "Coffee"} {"api_key" ["should be at least 1 characters"]})))




















(comment
  
  (run-tests)

  
  "")