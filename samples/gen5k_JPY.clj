(load-file "samples/gen2.clj")

(defn mapping-fn-2 []
  { :currency1 "JPY"
    :currency2 "JPY"
    :initialValue (rand-nth (constants :notionals))})

[
[1000 "xs_swap.xml" naming-fn mapping-fn-2]
[5000 "xs_swap.xml" naming-fn mapping-fn-2]
]
