(ns advent.day01.day01
  (:require [advent.util :as util]))

(defn parse-int [s]
  (Integer/parseInt s))

(def input (util/read-lines "src/advent/day01/input.txt"))

(def weights (map parse-int input))

(defn fuel [mass]
  (- (int (/ mass 3)) 2))

(defn part-1 [] (apply + (map fuel weights)))

(defn tsiolkovsky-stage [mass]
  (let [stage-fuel (fuel mass)]
    (if (not (pos? stage-fuel))
      mass
      (+ mass (tsiolkovsky-stage stage-fuel)))))

(def tsiolkovsky-fuel (comp tsiolkovsky-stage fuel))

(defn part-2 [] (apply + (map tsiolkovsky-fuel weights)))
