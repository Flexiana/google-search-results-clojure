# Google Search Results in Clojure


This Clojure Library is meant to scrape and parse results from Google, Bing, Baidu, Yandex, Yahoo, Ebay and more using [SerpApi](https://serpapi.com).

The following services are provided:

  * [Search API](https://serpapi.com/search-api)
  * [Location API](https://serpapi.com/locations-api)
  * [Search Archive API](https://serpapi.com/search-archive-api)
  * [Account API](https://serpapi.com/account-api)

SerpApi.com provides a [script builder](https://serpapi.com/demo) to get you started quickly.

## Installation

Clojure must be already installed:


## Quick start

```clojure
(require '[serpapi.core :as sc])
(def edn-results (sc/edn-search {:q "coffee", :api_key "secret_api_key"})
 ```

This example runs a search about "coffee" using your secret api key.

The SerpApi.com service (backend)
 - searches on Google using the search: q = "coffee"
 - parses the messy HTML responses
 - return a standardized JSON response
The function sc/edn-search
 - Format the request to SerpApi.com server
 - Execute GET http request
 - Parse JSON into EDN using the Cheshire library
Et voila..

Alternatively, you can search on other search engines by providing the `:engine` keyword to argument map on any of the provided functions.
The following values are supported

```
:bing :baidu :yahoo :yandex :ebay
```
All functions support both strings and keywords for map keys as well as any keyword sets mentioned in this document

For example the usage below is also supported


```clojure
(require '[serpapi.core :as sc])
(def edn-results (sc/edn-search {"q" "coffee", "api_key" "secret_api_key" "engine" "baidu"})
 ```



See the [playground to generate your code.](https://serpapi.com/playground)

# Summary
- [Google Search Results in Clojure](#google-search-results-in-clojure)
  - [Installation](#installation)
  - [Quick start](#quick-start)
- [Summary](#summary)
  - [Guide](#guide)
    - [How to set the private API key](#how-to-set-the-private-api-key)
    - [Search API capability for Google](#search-api-capability-for-google)
    - [Example by specification](#example-by-specification)
    - [Location API](#location-api)
    - [Search Archive API](#search-archive-api)
    - [Account API](#account-api)

## Guide
### How to set the private API key
The api_key can be set globally using the set-api-key function.
```clojure
(require '[serpapi.core :as sc])
(sc/set-api-key "secret-api-key")
(def result (sc/edn-search {:q "coffee"}))
```

or api_key can be provided for each search.
```clojure
(def edn-results (sc/edn-search {:q "coffee", :api_key "secret_api_key"})
```

To get the key simply copy/paste from [serpapi.com/dashboard](https://serpapi.com/dashboard).

### Search API capability for Google
```clojure
(def search-params  {
  :q "search",
  :google_domain "Google Domain", 
  :location "Location Requested", 
  :device "desktop", ;; #{"desktop" "mobile" "tablet"}
  :hl "Google UI Language",
  :gl "Google Country",
  :safe "Safe Search Flag",
  :num "Number of Results",
  :start "Pagination Offset",
  :api_key "private key", ;; copy paste from https://serpapi.com/dashboard
  :tbm "nws" ;; #{"nws" "isch" "shop"},
  :tbs "custom to be search criteria"
  :async true ;;  boolean, allow async
}

;; return search results in edn
(def search (sc/edn-search search-params))

;; return search results as raw html
(def html-search (sc/html-search search-params))


;; search as raw JSON format
(def json-search (sc/json-search search-params))

;; or use the base search function and set the options there
(def base-search (sc/search (assoc search-params :output "json")))
```

(the full documentation)[https://serpapi.com/search-api].

More search API are documented on [SerpApi.com](http://serpapi.com).

You will find more hands on examples below.

### Example by specification
We love true open source, continuous integration and Test Drive Development (TDD). 0
 
The directory test/ includes specification/examples.


To run the test:
```bash
bin/kaocha
```

### Location API

```clojure
(def location-list  (sc/locations {:q "Austin", :limit 3}))
```

it prints the first 3 location matching Austin (Texas, Texas, Rochester)
```clojure
[{:id "585069bdee19ad271e9bc072",
  :google_id 200635,
  :google_parent_id 21176,
  :name "Austin, TX",
  :canonical_name "Austin,TX,Texas,United States",
  :country_code "US",
  :target_type "DMA Region",
  :reach 5560000,
  :gps [-97.7430608, 30.267153],
  :keys ["austin", "tx", "texas", "united", "states"]},
  ...]
```

### Search Archive API
This API allows to retrieve previous search.
To do so run a search to save a search_id.
```clojure
(def search (sc/edn-search {:q "Coffee", :location "Portland"}))
(def search-id (get-in search [:search_metadata :id]))
```

Now let retrieve the previous search from the archive.

```clojure
(def archive-search (sc/search-archive search-id))
```
it prints the search from the archive.

### Account API
```clojure
(def account-search (account))
```
it prints your account information.
