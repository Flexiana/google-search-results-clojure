(ns serpapi.test-helpers
  (:require [clojure.test :as t :refer [do-report]]))

(defmethod t/assert-expr 'thrown-with-data? [msg form]
  (let [data (second form)
        body (nthnext form 2)]
    `(try ~@body
          (do-report {:type :fail, :message ~msg
                      :expected '~form, :actual nil})
          (catch clojure.lang.ExceptionInfo e#
            (let [expected# ~data
                  actual# (ex-data e#)]
              (if (= expected# actual#)
                (do-report {:type :pass, :message ~msg
                            :expected expected#, :actual actual#})
                (do-report {:type :fail, :message ~msg
                            :expected expected#, :actual actual#})))
            e#))))