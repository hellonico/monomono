(require '[clj-time.coerce :as c] '[clj-time.local :as l])

(def constants {
  :currencies   ["EUR" "JPY" "USD" "AUD"]
  :notionals    [100000000 10000000000 1000000000000 100000000000000]})

(defn mapping-fn []
   { :currency1 (rand-nth (constants :currencies))
     :currency2 (rand-nth (constants :currencies))
     :initialValue (rand-nth (constants :notionals))})

(defn naming-fn [output-folder mappings]
  (str
    output-folder
    "/xs_swap_"
    (mappings :currency1) "_"
    (mappings :currency2) "_"
    (mappings :initialValue) "_"
    (c/to-long (l/local-now)) ".xml"))

[10 "xs_swap.xml" naming-fn mapping-fn]
