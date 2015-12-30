(ns bowling.core)

(defn frame? [frame]
  (= 2 (count frame)))

(defn strike? [frame]
    (and
      (not (nil? frame))
      (= 10 (frame 0))))

(defn sum [frame]
  (if (frame? frame)
    (+ (frame 0) (frame 1))))

(defn spare? [frame]
  (and
    (not (strike? frame))
    (= 10 (sum frame))))

(defn hole? [frame]
  (and
    (not (spare? frame))
    (not (strike? frame))))


(defn- next-frame [game rank]
  (get game (inc rank)))

(defn sum-of-2-next-launch [game rank]
  (let [-next-frame (next-frame game rank)]
    (if (strike? -next-frame)
      (let [next-rank (inc rank)
            next-next-frame (next-frame game next-rank)]
        (+ (sum -next-frame) (sum next-next-frame)))
      (sum -next-frame))))

(defn frame-score-with-spare [game rank]
  (let [frame (get game rank)]
    (if (hole? frame)
      ;; for a hole, only sum frame members
      (sum frame)
      ;; for spare or strike, need next frame
      (let [-next-frame (next-frame game rank)]
        (if (spare? frame)
          ;; for a spare add score of the frame and the score of the next launch
          (+ (sum frame) (get -next-frame 0))
          (if (strike? frame)
            ;; for a spare add score of the frame and the score of the 2 next launch
            (+ (sum frame) (sum-of-2-next-launch game rank))))))))

(defn game? [game]
  (and
    ;; all elements are frame
    (true? (reduce #(and %1 %2) (vec (map frame? game))))
    (or
      (= 10 (count game)) ;; 10 elements
      (and
        (= 11 (count game))
        ;; the 10nth is not a hole (is a strike or a spare)
        (not (hole? (get game 9))))
      (and
        (= 11 (count game))
        ;; the 10nth is not a hole (is a strike or a spare)
        (not (hole? (get game 9))))
      (and
        (= 12 (count game))
        ;; the 10nth 11nth and 12nth is a strike
        (strike? (get game 10)) (strike? (get game 11))))))

(defn game-score-with-spare [game]
  (if (not (game? game))
    nil)
  (loop [frame-index 0
         score 0]
    (if (= 10 frame-index)
      score
      (recur
        (inc frame-index)
        ;; compute score for actual frame
        (let [score-for-frame (frame-score-with-spare game frame-index) ]
          ;; can have nil score ????
          (if (not (nil? score-for-frame))
            ;; add score-for-frame to global score
            (+ score score-for-frame)))))))
