(ns advent.day03.day03
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [advent.util :as util]))

(def input
  (map #(str/split % #",")
       (util/read-lines "src/advent/day03/input.txt")))

(defn x [point]
  (point 0))

(defn y [point]
  (point 1))

;; [0 0], R8 -> [0, 8], [0, 7] ... [0, 1]
(defn points-between [point instruction]
  (let [direction (first instruction)
        magnitude (->> instruction rest (apply str) util/parse-int)]
    (case direction
      \U (map vector
              (repeat magnitude (x point))
              (range (+ magnitude (y point)) (y point) -1))
      \D (map vector
              (repeat magnitude (x point))
              (range (- (y point) magnitude) (y point) 1))
      \L (map vector
              (range (- (x point) magnitude) (x point) 1)
              (repeat magnitude (y point)))
      \R (map vector
              (range (+ magnitude (x point)) (x point) -1)
              (repeat magnitude (y point))))))

(def initial-acc {:points #{} :terminal [0 0]})

;; Given a terminal position, a set of seen points, and an instruction,
;; add all points along the instruction to the set, then move the terminal.
;; Is a reducer.
(defn add-points-along-instruction [{points :points terminal :terminal} instruction]
  (let [added-points (points-between terminal instruction)]
    {:terminal (first added-points) ; points-between generates in reverse order; first point is new terminal
     :points (set/union points (set added-points))}))

(defn points-in-wire [wire]
  (:points (reduce add-points-along-instruction initial-acc wire)))

(defn intersections [wire-a wire-b]
  (set/intersection (points-in-wire wire-a) (points-in-wire wire-b)))

(defn manhattan-distance [a b]
  (+ (Math/abs (- (x a) (x b)))
     (Math/abs (- (y a) (y b)))))

(defn part-1 []
  (apply min
         (map #(manhattan-distance [0 0] %)
              (intersections (first input) (second input)))))
