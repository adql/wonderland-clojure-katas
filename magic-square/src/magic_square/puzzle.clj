(ns magic-square.puzzle)

(def values [1.0 1.5 2.0 2.5 3.0 3.5 4.0 4.5 5.0])

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
          (let [rest-unpaired-new (remove-at-index rest-unpaired pair-right-index)
                paired-new (conj paired [pair-left pair-right])]
            (recur rest-unpaired-new paired-new)))))))

(defn order-and-verify-sides
  "Given two corner and two side pairs as vectors, and magic number,
  determine the position of the sides (taking the position of the
  corners as given) and tests that all sides sum up to magic
  number. Returns the side pairs in right order as a vector"
  [corners sides magic-number]
  (let [corner-l (corners 0)
        corner-r (corners 1)
        [up left right down] (for [l (range 2)
                                   r (range 2)]
                               (- magic-number (corner-l l) (corner-r r)))
        sides-should [[up down] [left right]]
        sides-permutations (for [l [< >]
                                 r [< >]
                                 pl [first last]
                                 :let [pr (get {first last last first} pl)]]
                             [(sort l (pl sides)) (sort r (pr sides))])]
    (some #(when (= sides-should %) sides-should)
          sides-permutations)))

(defn- pairs->square
  "Builds a 3x3 square from corners, sides and center, ordered
  top-left-right-bottom precedence."
  [corners sides center]
  (let [[[tl br] [tr bl]] corners
        [[tc bc] [ml mr]] sides]
    [[tl tc tr]
     [ml center mr]
     [bl bc br]]))

(defn- corner?
  "Test if the pair can be the corner of a magic square with the other pairs.
  A number at the corner must have two couples of non-opposite numbers
  to sum up to magic-number with. By design of opposite-numbers, only
  one of them (here the left one) has to be tested if the square is
  solvable."
  [pair pairs magic-number]
  (let [search-sum (- magic-number (first pair))
        search-nums (flatten pairs)
        complementaries (remove (partial = (/ search-sum 2))
                                (map #(- search-sum %) search-nums))]
    (= 4 (count (filter #(some (partial = %) complementaries) search-nums)))))

(defn magic-square [values]
  (when-let [center (center values)]
    (let [magic-number (magic-number values)]
      (when-let [pairs (values->pairs   ;first, find opposite pairs
                        (remove-at-index values (.indexOf values center))
                        (- magic-number center))]
        (let [corners (vec (for [i (range 4) ;determine which are at the corners
                                 :let [pair (pairs i)]
                                 :when (corner? pair (remove-at-index pairs i) magic-number)]
                             pair))
              sides (vec (remove #(some (partial = %) corners) pairs))]
          (when (= (count corners) (count sides))
            (when-let [ordered-sides (order-and-verify-sides corners sides magic-number)]
              (pairs->square corners ordered-sides center))))))))
