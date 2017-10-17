
(defn mapping-fn []
  {:currency1 "JPY"
   :currency2 "JPY"
   :initialValue
   (rand-nth [100000000 10000000000 1000000000000 100000000000000])})

(defn naming-fn [output-folder mappings]
  (str
    output-folder
    "/xs_swap_"
    "JPY"
    (mappings :initialValue) "_"
    (System/currentTimeMillis) ".xml"))

[20 "xs_swap.xml" naming-fn mapping-fn ]
