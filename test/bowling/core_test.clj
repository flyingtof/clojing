(ns bowling.core-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [bowling.core :refer :all]))

(fact "is frame"
      (frame? [8 2])   => truthy
      (frame? [8 ])    => falsey
      (frame? [8 1 1]) => falsey)

(fact "sum as expected"
      (sum [8 2]) => 10
      (sum [2 4]) => 6)

(fact "is strike"
      (strike? [8  2]) => falsey
      (strike? [10 0]) => truthy)

(fact "is spare"
      (spare? [8  2]) => truthy
      (spare? [8  1]) => falsey
      (spare? [0 10]) => truthy
      )

(fact "is hole"
      (hole? [8  2]) => falsey
      (hole? [10 0]) => falsey
      (hole? [8  1]) => truthy)


(def bad-game-1
      [[0  0] [0 0]])
(def bad-game-2
      [[0  0] [0   ] [0  0] [0  0] [0  0] [0  0] [0  0] [0  0] [0  0] [0  0] ])
(def only0-game
      [[0  0] [0  0] [0  0] [0  0] [0  0] [0  0] [0  0] [0  0] [0  0] [0  0] ])
(def only1-game
      [[1  1] [1  1] [1  1] [1  1] [1  1] [1  1] [1  1] [1  1] [1  1] [1  1] ])
(def with-spare-in-frame-10-game
      [[0  0] [0  0] [0  0] [0  0] [0  0] [0  0] [0  0] [0  0] [0  0] [8  2] [5 0]])
(def with-strike-in-frame-10-game
      [[0  0] [0  0] [0  0] [0  0] [0  0] [0  0] [0  0] [0  0] [0  0] [10 0] [5 2]])
(def with-hole-in-frame-10-game-and-11
      [[0  0] [0  0] [0  0] [0  0] [0  0] [0  0] [0  0] [0  0] [0  0] [3  4] [5 2]])
(def all-strike-game
      [[10 0] [10 0] [10 0] [10 0] [10 0] [10 0] [10 0] [10 0] [10 0] [10 0] [10 0] [10 0]])

(fact "is game"
      (game? only0-game) => truthy
      (game? bad-game-1) => falsey
      (game? bad-game-2) => falsey
      (game? all-strike-game) => truthy
      (game? with-spare-in-frame-10-game) => truthy
      )

(def spare-first-game
  [[8 2] [1 0] [0 0] [0 0] [0 0] [0 0] [0 0] [0 0] [0 0] [0 0] ])
(def strike-first-game
  [[10 0] [1 1] [0 0] [0 0] [0 0] [0 0] [0 0] [0 0] [0 0] [0 0] ])
(fact "frame score"
      (frame-score-with-spare spare-first-game 0) => 11
      (frame-score-with-spare spare-first-game 1) => 1
      (frame-score-with-spare strike-first-game 0) => 12
      (frame-score-with-spare strike-first-game 1) => 2
      )

(fact "2 next lauch"
      (sum-of-2-next-launch all-strike-game 1) => 20)

(fact "game score"
      (game-score-with-spare only0-game) => 0
      (game-score-with-spare only1-game) => 20
      (game-score-with-spare bad-game-1) => nil
      (game-score-with-spare with-spare-in-frame-10-game) => 15
      (game-score-with-spare with-strike-in-frame-10-game) => 17
      (game-score-with-spare with-hole-in-frame-10-game-and-11) => 7
      (game-score-with-spare all-strike-game) => 300)
