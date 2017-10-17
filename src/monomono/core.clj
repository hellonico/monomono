(ns monomono.core
  (:gen-class)
  (:require
    [clj-time.local :as l]
    [clojure.tools.cli :refer [parse-opts]]
    [clj-time.coerce :as c]
    [progrock.core :as pr]
    [com.climate.claypoole :as cp]
    [selmer.parser :as sl] ))

(defn new-generator [ template output-folder naming-fn mapping-fn  ]
  (fn [ _ ]
   (let [ mappings (mapping-fn) ]
   (spit
    (naming-fn output-folder mappings)
    (sl/render-file
     template
     mappings)))))

(defn clean-output [folder]
 ; (println "cleaning : " folder)
 (doseq [f
  (.listFiles (clojure.java.io/as-file folder))]
  (.delete f)))

(defn generate [ [output-folder x template naming-fn mapping-fn] ]
  ; (println "linear gen")
  (let [ generator (new-generator template output-folder naming-fn mapping-fn)]
  (dotimes [ i x ]
    (generator i))))

(defn generate-with-pool
  [ [output-folder x template naming-fn mapping-fn] ]
  ; (println "pooled gen")
  (let [ generator (new-generator template output-folder naming-fn mapping-fn) ]
    (let [pool (cp/threadpool 6)]
      (doall (cp/pmap pool generator (range x)))
      (cp/shutdown pool))))

(def cli-options
  [["-p" "--pool" "Generate Using Pool" :default false :parse-fn #(Boolean/valueOf %) ]
   ["-c" "--clean" "Clean Output Folder First" :default false :parse-fn #(Boolean/valueOf %)]
   ["-r" "--resources FILE" "resources folder for templates" :default "resources" ]
   ["-g" "--generator FILE" "gen file" :required true :validate [#(not (nil? %)) "Gen file not valid"]]
   ["-t" "--time" "Display time to generate files" :default false :parse-fn #(Boolean/valueOf %)]
   ["-o" "--output FILE" "output folder" :default "gen" ]])

(defn run
  [ {:keys [options] } ]
  ; (clojure.pprint/pprint options)
  ; (when-not *compile-files*
  ; (ns monomono.core))
  (let [

    generator (options :generator)
    target (load-file generator)
    output-folder (options :output)
    is-pool? (options :pool)
    is-clean? (options :clean)
    gen-fn (if is-pool? generate-with-pool generate)
  ]
  (if is-clean? (clean-output output-folder))
  (sl/cache-on!)
  (sl/set-resource-path! (options :resources))
  (clojure.java.io/make-parents (str output-folder "/" "run.tmp"))

  (if (vector? (first target))
  ; many
  (let [con (count target) bar (atom (pr/progress-bar con))]
  (doseq [[x template naming-fn mapping-fn] target ]

    (reset! bar (pr/tick @bar 1))
    (pr/print @bar)
    (apply
       gen-fn
      [[output-folder x template naming-fn mapping-fn]])


    )

    )
  ; one
  (let [
    [x template naming-fn mapping-fn] target ]
  (apply
     gen-fn
    [[output-folder x template naming-fn mapping-fn]]))

  )))

(defn -main [& args]
  (if (empty? args)
  (println "Specify generator file")
  (do
  (run (parse-opts args cli-options))
  (System/exit 0))))
