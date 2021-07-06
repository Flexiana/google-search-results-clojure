(ns serpapi.core
  (:require [clj-http.client :as http]
            [cheshire.core :as json]
            [serpapi.schema :as sc :refer [transform-params validate-params]]))


(defonce api-key (atom ""))

(defn set-api-key [new-api-key]
  (reset! api-key new-api-key))


(defn serpapi-request [url params]
  (try
    (some->
     (http/get url
               {:connection-timeout 600 :socket-timeout 600
                :query-params params})
     :body)
    (catch Exception _)))


(defn prepare-request [schema params]
  (let [{:strs [api_key] :as transformed-params} (transform-params schema params)
        transformed-params (if api_key
                             transformed-params
                             (assoc transformed-params "api_key" @api-key))]
    (if-let [param-errors (validate-params schema transformed-params)]
      (throw (ex-info "Invalid Params" param-errors))
      transformed-params)))


(defn call-serpapi
  ([url schema params]
   (call-serpapi url schema params false))
  ([url schema params decode?]
   (let [{:strs [output] :as validated-params} (prepare-request schema params)
         result (serpapi-request url validated-params)]
     (case output
       (nil "json") (if decode? (json/decode result true)
                        result)
       result))))

(def search (partial call-serpapi "https://serpapi.com/search" sc/SearchParams))


(defn html-search [params]
  (search (assoc params :output "html")))

(defn json-search [params]
  (search (assoc params :output "json")))

(defn edn-search [params]
  (search (assoc params :output "json") true))

(defn locations [params]
  (some->
   (serpapi-request "https://serpapi.com/locations.json" params)
   (json/decode true)))

(defn account [params]
  (call-serpapi "https://serpapi.com/account.json" sc/AccountParams params true))

(defn search-archive
  ([search-id] (search-archive search-id {}))
  ([search-id params]
   (call-serpapi (str "https://serpapi.com/searches/" search-id) sc/SearchArchiveParams params true)))

(comment

  (set-api-key (System/getenv "SERPAPIKEY"))
  
  "")