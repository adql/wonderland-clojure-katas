(ns alphabet-cipher.coder
  (:require [clojure.string :as s]))

(defn- str->num
  "Translates a string into a numeric representation of the letters"
  [s]
  (map #(- % 97) (.getBytes (s/lower-case s))))

(defn- execute
  "One function does it all by taking :encode or :decode as operation"
  [keyword message operation]
  (let [keys         (cycle (str->num keyword))
        byte-message (str->num message)
        op           (operation {:encode #(char (+ 97 (mod (+ %1 %2) 26)))
                                 :decode #(char (+ 97 (mod (+ 26 (- %1 %2)) 26)))})]
    (apply str (map op byte-message keys))))

(defn encode [keyword message]
  (execute keyword message :encode))

(defn decode [keyword message]
  (execute keyword message :decode))
  
;; The nice trick here is that using the message as a keyword to
;; decode the cipher provides the original keyword in repetition, from
;; which the single keyword is extracted
(defn decipher [cipher message]
  (let [repeated-keyword (decode message cipher)]
    (loop [cand-len 1]
      (if (let [cand-repetitions (for [i (range cand-len)]
                                   (take-nth cand-len (nthrest repeated-keyword i)))]
            (every? #(apply = %) cand-repetitions))
        (subs repeated-keyword 0 cand-len)
        (recur (inc cand-len))))))
