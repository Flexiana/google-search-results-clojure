(ns serpapi.schema
  (:require [malli.core :as m]
            [malli.transform :as mt]
            [malli.error :as me]))


(def ApiKey ["api_key" [:string {:min 1}]])

(def Output ["output" {:optional true} [:enum {:default "json"} "json" "html"]])

(def SearchParams 
  [:map
   ["engine"  [:enum {:default "google"} "google" "baidu" "bing" "yahoo" "yandex" "ebay"]]
   ApiKey
   Output
   ["q" [:string {:min 1}]]])

(def SearchArchiveParams
  [:map ApiKey Output])

(def AccountParams
  [:map ApiKey])


(def param-transformer (mt/transformer
                        mt/string-transformer
                        {:name :enum-transformer
                         :encoders {:enum m/-keyword->string}}
                        mt/default-value-transformer))

(defn transform-params [schema params]
  (as-> params $
    (m/encode schema $ (mt/key-transformer {:encode name}))
    (m/encode schema $ param-transformer)))

(defn validate-params [schema params]
  (some->> params
           (m/explain schema)
           (me/humanize)))


(comment
  (def transformed-params
    (transform-params {"api-key" "api_key" :q "Sunday Igboho" :output :html :engine :baidu}))
  
  (validate-params transformed-params)

  "")