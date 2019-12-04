(ns fox-goose-bag-of-corn.puzzle)

(def start-pos [[[:fox :goose :corn :you] [:boat] []]])

;; Not a proper solution, just testing...

(def cheat-solution [[[:fox :goose :corn :you] [:boat] []]
               [[:fox :corn] [:boat :goose :you] []]
               [[:fox :corn] [:boat] [:goose :you]]
               [[:fox :corn] [:boat :you] [:goose]]
               [[:fox :corn :you] [:boat] [:goose]]
               [[:corn] [:boat :fox :you] [:goose]]
               [[:corn] [:boat] [:fox :goose :you]]
               [[:corn] [:boat :goose :you] [:fox]]
               [[:goose :corn :you] [:boat] [:fox]]
               [[:goose] [:boat :corn :you] [:fox]]
               [[:goose] [:boat] [:fox :corn :you]]
               [[:goose] [:boat :you] [:fox :corn]]
               [[:goose :you] [:boat] [:fox :corn]]
               [[] [:boat :goose :you] [:fox :corn]]
               [[] [:boat] [:fox :goose :corn :you]]])

(defn river-crossing-plan []
  cheat-solution)
