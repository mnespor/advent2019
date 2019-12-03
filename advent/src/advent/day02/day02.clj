(ns advent.day02.day02
  (:require [clojure.string :as str]
            [advent.util :as util]
            [clojure.math.combinatorics :as combo]))

(def input (mapv util/parse-int (-> "src/advent/day02/input.txt"
                                   slurp
                                   (str/split #","))))

(def test-input [1,9,10,3,2,3,11,0,99,30,40,50])

(def instruction-size 4)

(defn op-and-store [f]
  (fn [eip memory]
    (let [eax (memory (memory (inc eip)))
          edx (memory (memory (-> eip inc inc)))
          dest (memory (-> eip inc inc inc))]
      (assoc memory dest (f eax edx)))))

(def add-and-store (op-and-store +))

(def mul-and-store (op-and-store *))

(defn step [{eip :eip memory :memory halted :halted result :result}]
  (if halted
    {:halted true :result result}
    (case (memory eip)
      1 {:eip (+ instruction-size eip) :memory (add-and-store eip memory)}
      2 {:eip (+ instruction-size eip) :memory (mul-and-store eip memory)}
      99 {:halted true :result (first memory) :noun (memory 1) :verb (memory 2)}
      {:halted true :result "error"})))

(defn twelve-oh-two [memory]
  (assoc memory 1 12 2 2))

(defn part-1 []
  (first (drop-while #(nil? (:halted %))
                     (iterate step {:eip 0 :memory (twelve-oh-two input)}))))

(defn run [[noun verb]]
  (first (drop-while #(nil? (:halted %))
                     (iterate step {:eip 0 :memory (assoc input 1 noun 2 verb)}))))

(defn run-combinations [limit]
  (map run (combo/selections (range limit) 2)))

(defn brute-force-noun-verb [limit target]
  (first (filter #(= target (:result %)) (run-combinations limit))))

;; 206 ms for brute force
(defn part-2 [limit]
  (brute-force-noun-verb limit 19690720))
