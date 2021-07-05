(ns serpapi.core
  (:require [clj-http.client :as http]
            [cheshire.core :as json]
            [serpapi.schema :refer [transform-params validate-params]]))


(defonce api-key (atom ""))

(defn set-api-key [new-api-key]
  (reset! api-key new-api-key))


(defn search [params]
  (let [{:strs [api_key] :as transformed-params} (transform-params params)
        transformed-params (if api_key
                             transformed-params
                             (assoc transformed-params "api_key" @api-key))]
    (if-let [param-errors (validate-params transformed-params)]
      (throw (ex-info "Invalid Search Params" param-errors))
      (try
        (->
         (http/request {:method :get
                        :url "https://serpapi.com/search"
                        :socket-timeout 600 :connection-timeout 600
                        :query-params transformed-params})
         :body)
        (catch Exception _)))))


(defn html-search [params]
  (search (assoc params :output "html")))

(defn json-search [params]
  (search (assoc params :output "json")))

(defn edn-search [params]
  (some-> (assoc params :output "json")
          search
          (json/decode true)))

(comment

  (set-api-key (System/getenv "SERPAPIKEY"))
  @api-key
  
  (def res (-> (search {:q "Igboho Secession"})
                (json/decode true)))
  
  (html-search {:q "Igboho Secession"})
  (edn-search {:q "Igboho Secession"})
  
  "")