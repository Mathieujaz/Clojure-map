(ns food
  (:require [clojure.string :as str]))

(defn ValidateMap [map-content]
  (let [lines (str/split-lines map-content)
        lengths (map count lines)]
    (if (apply = lengths)
      lines
      nil)))

(defn findfood [map x y visited]
  (let [rows (count map)
        cols (count (first map))]
    (cond
      ;; Out of bounds or already visited
      (or (< x 0) (>= x rows) (< y 0) (>= y cols)) nil
      (visited [x y]) nil
      ;; Hit a wall
      (= (get-in map [x y]) \#) nil
      ;; Found food
      (= (get-in map [x y]) \@) [[x y]]
      ;; Explore neighbors
      :else
      (let [new-visited (conj visited [x y])
            directions [[0 1] [1 0] [0 -1] [-1 0]] ;; Right, Down, Left, Up
            paths (map #(findfood map
                                  (+ x (first %))
                                  (+ y (second %))
                                  new-visited)
                       directions)
            valid-path (first (filter some? paths))]
        (if valid-path
          (cons [x y] valid-path)
          nil)))))

(defn updatemap [map path]
  (reduce (fn [m [x y]]
            (assoc-in m [x y] \+))
          map
          path))
