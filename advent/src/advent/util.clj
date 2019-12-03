(ns advent.util
  (:require [clojure.string :as str]))

(defn read-lines [path]
  (-> path
      slurp
      str/split-lines))

(defn parse-int [s]
  (Integer/parseInt (str/trim s)))
