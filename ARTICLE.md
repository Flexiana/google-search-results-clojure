# Google Search Results With SerpApi and Clojure
## SERP

SERP stands for Search Engine Results Page. 
People scrape search engines for various reasons

- Search Engine Optimization,

- Analytics (when the Search Engines analytics don't cover your use case),

- etc

You can roll your own SERP scraper but for various reasons detailed [elsewhere](https://stackoverflow.com/questions/22657548/is-it-ok-to-scrape-data-from-google-results) on [the](https://geekflare.com/serp-api/) web, you may choose to go with a SERP API

This article will focus on [SerpAPI](https://serpapi.com/) and [google-search-results-clojure](https://github.com/Flexiana/google-search-results-clojure/tree/main), it's clojure SDK.

You can use http libraries directly with SerpApi. There is plenty of [documentation,](http://serpapi.com)  [a script builder](https://serpapi.com/demo) and [a playground to generate your code.](https://serpapi.com/playground) 

However, the SDK wraps much of the boilerplate for you and provides nice input validation for your functions and meaningful error messages

## Prerequisites

You must have Clojure installed on your machine. Follow [these instructions](https://clojure.org/guides/getting_started) to set up Clojure 

Confirm your setup was successful by running

```bash
clojure -version
```

## Project Setup

Create your project directory `serpapi_example` to match the structure below

```bash
serpapi_example
├── deps.edn
└── src
    └── serpapi_example
        └── search.clj
```

In the `deps.edn` file, add the following

```clojure
{:paths ["src"]
 :deps {io.github.Flexiana/google-search-results-clojure
        {:sha "6a8aa887c89ff98a0554939a680f6d4f82a4a050"
         :git/url "https://github.com/Flexiana/google-search-results-clojure.git"}}}
```

In the `search.clj` file add the following
```clojure
(ns serpapi-example.search
  (:require [serpapi.core :as sc] ))

(comment

 "")
```
Start a Clojure REPL and load the `search.clj` file. 

Some code editors allow you to send forms from your file straight to the Clojure REPL. More information [here](https://www.youtube.com/watch?v=gIoadGfm5T8). 

If you are using VSCode and Calva VSCode plugin, this is where you [jack-in](https://calva.io/jack-in-guide/)

You can type the subsequent code samples directly into the REPL to evaluate them. Or copy into the comment form in `search.clj` and send the forms to the REPL for evaluation.

Register an account on serpapi.com and get your api_key from the dashboard.

Set a global API Key for all search requests.

```clojure
(sc/set-api-key "your-api-key")
```

You can override the global API Key by including the `api_key` key and value in the map passed to the search function 

Run a search on google

```clojure
(def google-coffee-results (sc/edn-search {:q "coffee"}))
(println google-coffee-results)
```

You should get results like

```clojure
{:search_metadata
 {:id "60eaceb5f21801fd93e84011",
  :status "Success",
  :json_endpoint "https://serpapi.com/searches/60494c3263f61f95/60eaceb5f21801fd93e84011.json",
  :created_at "2021-07-11 10:57:57 UTC",
  :processed_at "2021-07-11 10:57:57 UTC",
  :google_url "https://www.google.com/search?q=coffee&oq=coffee&sourceid=chrome&ie=UTF-8",
  :raw_html_file "https://serpapi.com/searches/60494c3263f61f95/60eaceb5f21801fd93e84011.html",
  :total_time_taken 2.95},
 :local_map
 {:link
  "https://www.google.com/search?q=coffee&npsic=0&rflfq=1&rldoc=1&rllag=18426031,-64619606,644&tbm=lcl&sa=X&ved=2ahUKEwiDlq2F7trxAhU9RUEAHevmDWQQtgN6BAgUEAQ",
  :image "https://serpapi.com/searches/60eaceb5f21801fd93e84011/images/138160cf56e903e0526b91de747b7147.png",
  :gps_coordinates {:latitude 18.426031, :longitude -64.619606, :altitude 644}},
 :organic_results
 [{:displayed_link "https://en.wikipedia.org › wiki › Coffee",
   :snippet
   "Coffee is a brewed drink prepared from roasted coffee beans, the seeds of berries from certain Coffea species. All fruit must be further processed from a raw​ ...",
   :title "Coffee - Wikipedia",
   :rich_snippet
   {:bottom
    {:extensions
     ["Region of origin: Horn of Africa and ‎South Arabia‎"
      "Color: Black, dark brown, light brown, beige"
      "Introduced: 15th century"],
     :detected_extensions {:introduced_th_century 15}}},
   :thumbnail
   "https://serpapi.com/searches/60eaceb5f21801fd93e84011/images/ba03583ee6fab58deb5d4a7cf858d848e5e0bce5f17f86e476e118d8eb8deaf1.jpeg",
   :related_pages_link
   "https://www.google.com/search?q=related:https://en.wikipedia.org/wiki/Coffee+coffee&sa=X&ved=2ahUKEwiDlq2F7trxAhU9RUEAHevmDWQQH3oECAQQCQ",
   :link "https://en.wikipedia.org/wiki/Coffee",
   :cached_page_link
   "https://webcache.googleusercontent.com/search?q=cache:U6oJMnF-eeUJ:https://en.wikipedia.org/wiki/Coffee+&cd=12&hl=en&ct=clnk&gl=vg",
   :position 1,
   :sitelinks
   {:inline
    [{:title "History", :link "https://en.wikipedia.org/wiki/History_of_coffee"}
     {:title "Coffee bean", :link "https://en.wikipedia.org/wiki/Coffee_bean"}
     {:title "Coffee roasting", :link "https://en.wikipedia.org/wiki/Coffee_roasting"}
     {:title "Instant coffee", :link "https://en.wikipedia.org/wiki/Instant_coffee"}]}}

     ...]
     ...}

```

The map supplied to the search function is converted to query parameters for the SerpAPI. Check the SerpAPI docs for more options

You can also get a raw JSON response

```clojure
(def google-json-coffee-results (sc/json-search {:q "coffee"}))
(println google-json-coffee-results)
```

Or request an HTML response

```clojure
(sc/html-search {:q "coffee"})
```

Or directly control the output options 

```clojure
(sc/search {:q "coffee" :output "html"})
```

The default search engine is google. To use another search engine, supply the engine key and value to the argument map

```clojure
(def baidu-coffee-results (sc/edn-search {:q "coffee" :engine "baidu"}))
(println baidu-coffee-results)
```






