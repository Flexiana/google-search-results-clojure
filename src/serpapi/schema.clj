(ns serpapi.schema
  (:require [malli.core :as m]
            [malli.transform :as mt]
            [malli.error :as me]))

(def SearchParams 
  [:map
   ["engine"  [:enum {:default "google"} "google" "baidu" "bing" "yahoo" "yandex" "ebay"]]
   ["api_key" [:string {:min 1}]]
   ["output" {:optional true} [:enum {:default "json"} "json" "html"]]
   ["q" [:string {:min 1}]]])


(def param-transformer (mt/transformer
                        mt/string-transformer
                        {:name :enum-transformer
                         :encoders {:enum m/-keyword->string}}
                        mt/default-value-transformer))

(defn transform-params [params]
  (as-> params $
    (m/encode SearchParams $ (mt/key-transformer {:encode name}))
    (m/encode SearchParams $ param-transformer)))

(defn validate-params [params]
  (some->> params
           (m/explain SearchParams)
           (me/humanize)))


(comment
  (def transformed-params
    (transform-params {"api-key" "api_key" :q "Sunday Igboho" :output :html :engine :baidu}))
  
  (validate-params transformed-params)

  "")