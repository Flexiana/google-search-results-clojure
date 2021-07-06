(ns serpapi.core-test
  (:require [serpapi.core :as sa]
            [serpapi.schema :as sc]
            serpapi.test-helpers
            [clj-http.fake :refer [with-fake-routes]]
            [cheshire.core :as json]
            [clojure.test :refer [is deftest are run-tests]]))


(deftest set-global-api-key
  (let [local-api-key (atom "")]
    (with-redefs [sa/api-key local-api-key]
      (sa/set-api-key "new-api-key")
      (is (= "new-api-key" @local-api-key)))))


(deftest test-prepare-request
  (let [local-api-key (atom "")]
    (with-redefs [sa/api-key local-api-key]
      (sa/set-api-key "new-api-key")
      (are [x y] (= y (sa/prepare-request sc/SearchParams x))
        {:q "Coffee"} {"q" "Coffee" "api_key" "new-api-key" "engine" "google"}
        {"q" "Coffee"} {"q" "Coffee" "api_key" "new-api-key" "engine" "google"}
        {"q" "Coffee" :api_key "another-api-key"} {"q" "Coffee" "api_key" "another-api-key" "engine" "google"}
        {"q" "Coffee" :api_key "another-api-key" :engine "baidu"} {"q" "Coffee" "api_key" "another-api-key" "engine" "baidu"}))))

(deftest search-integration
  (let [res {:result "Searches have results"}
        json-response (json/encode res)
        html-response "<h1>Searches have results</h1>"]
    (sa/set-api-key "api-key")
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
       {:address "https://serpapi.com/locations.json"
        :query-params {"q" "Austin" "limit" 2}}
       (fn [req] (prn req) {:status 200 :headers {} :body json-response})
       {:address "https://serpapi.com/account.json"
        :query-params {"api_key" "api-key"}}
       (fn [_] {:status 200 :headers {} :body json-response})
       {:address "https://serpapi.com/searches/search-id"
        :query-params {"api_key" "api-key"}}
       (fn [_] {:status 200 :headers {} :body json-response})
       {:address "https://serpapi.com/searches/search-id"
        :query-params {"api_key" "api-key" "output" "json"}}
       (fn [_] {:status 200 :headers {} :body json-response})
       {:address "https://serpapi.com/searches/search-id"
        :query-params {"api_key" "api-key" "output" "html"}}
       (fn [_] {:status 200 :headers {} :body html-response})}
      (are [x y] (= x  y)
        res (sa/edn-search {"q" "Coffee" "api_key" "api-key" "engine" "google"})
        html-response (sa/html-search {"q" "Coffee" "api_key" "api-key" "engine" "google"})
        json-response (sa/json-search {"q" "Coffee" "api_key" "api-key" "engine" "google"})
        json-response (sa/search {"q" "Coffee" "api_key" "api-key" "engine" "google"})
        ;; res (sa/locations {"q" "Austin" "limit" 2})
        res (sa/account {"api_key" "api-key"})
        html-response (sa/search-archive "search-id" {"api_key" "api-key" "output" "html"})
        res (sa/search-archive "search-id" {"api_key" "api-key" "output" "json"})
        res (sa/search-archive "search-id")
        res (sa/search-archive "search-id" {"api_key" "api-key"})))))


(deftest search-throws-with-wrong-params
  (let [local-api-key (atom "")]
    (with-redefs [sa/api-key local-api-key]
      (sa/set-api-key "new-api-key")
      (are [x y] (thrown-with-data? y  (sa/prepare-request sc/SearchParams x))
        {} {"q" ["missing required key"]}
        {:engine :wrong-engine} {"q" ["missing required key"] "engine" ["should be either google, baidu, bing, yahoo, yandex or ebay"]}
        {:engine :wrong-engine :q "Coffee"} {"engine" ["should be either google, baidu, bing, yahoo, yandex or ebay"]}))))

(deftest search-throws-without-api-key
  (sa/set-api-key "")
  (are [x y] (thrown-with-data? y (sa/prepare-request sc/SearchParams x))
    {} {"q" ["missing required key"] "api_key" ["should be at least 1 characters"]}
    {:q "Coffee"} {"api_key" ["should be at least 1 characters"]}))




















(comment
  
  (run-tests)

  
  "")