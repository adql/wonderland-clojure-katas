(ns magic-square.puzzle)

(def values [1.0 1.5 2.0 2.5 3.0 3.5 4.0 4.5 5.0])

(defn- validate
  "Validates that values has 9 elements."
  [values]
  (= (count values) 9))

;; May be smart to replace with using subvec
(defn- remove-at-index
  "Utility function to remove an item at index i of a collection"
  [coll i]
  (concat (take i coll) (nthrest coll (+ 1 i))))

(defn- magic-number
  "Calculates the magic number (average sum of 3)"
  [values]
  (/ (reduce + values) 3))

(defn- center
  "Calculates the average value, hence the center of the magic square.
  Returns nil if the result is not in the list of numbers."
  [values]
  (let [center (/ (reduce + values) 9)]
    (some #(when (= center %) center)
          values)))

(defn- values->pairs
  "Returns the values in pairs of opposite positions (sum up to magic)
  Returns nil if not possible."
  [values magic]
  (loop [unpaired values
         paired []]
    (if (empty? unpaired)
      paired
      (let [pair-left (first unpaired)
            pair-right (- magic pair-left) ;doesn't necessarily exist
            rest-unpaired (rest unpaired)]
        (when-let ;Checks if pair-right actually exists
            [pair-right-index (some #(when (= pair-right %)
                                       (.indexOf rest-unpaired pair-right))
                                    rest-unpaired)]
          (let [;; [l r] (split-with #(not= pair-right %) rest-unpaired)
                ;; rest-unpaired-new-1 (concat l (rest r))
                rest-unpaired-new (remove-at-index rest-unpaired pair-right-index)
                paired-new (conj paired [pair-left pair-right])]
            (recur rest-unpaired-new paired-new)))))))

(defn- pairs->square
  "Builds a 3x3 square with four pairs in opposite directions and center
  in the... center."
  [pairs center]
  (let [[[tl br] [tc bc] [tr bl] [mr ml]] pairs]
    [[tl tc tr]
     [ml center mr]
     [bl bc br]]))

(defn- corner?
  "Test if the pair at index i can take the corner of the square.
  A number at the corner must have two couples of non-opposite numbers
  to sum up to magic-number with. By design of opposite-numbers, only
  one of them (here the left one) has to be tested if the square is
  solvable."
  [pairs i magic-number]
  (let [search-sum (- magic-number (first (pairs i)))
        search-nums (flatten (remove-at-index pairs i))
        complementaries (remove (partial = (/ search-sum 2))
                                (map #(- search-sum %) search-nums))]
    (= 4 (count (filter #(some (partial = %) complementaries) search-nums)))))

(defn magic-square [values]
  )
