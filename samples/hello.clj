(def count (atom 0))

(defn mapping-fn []
  (swap! count inc)
  {:person (rand-nth ["nico" "chris" "jb" "taka"])
   :id @count})

(defn naming-fn [output-folder mappings]
  (str output-folder "/" (mappings :person) "_" (mappings :id) ".txt"))

[10 "hello.md" naming-fn mapping-fn ]
