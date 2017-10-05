(ns specific.sample
  (:require [clojure.spec.alpha :as s]
            [clojure.java.shell :as shell]
            [clojure.string :as string]))

(defn greet [pre sufs]
  (string/join ", " (cons pre sufs)))

(defn cowsay [msg]
  (shell/sh "cowsay" msg)) ; Fails in some environments

(defn some-fun [greeting & names]
  (:out (cowsay (greet greeting names))))

(s/def ::exit (s/and integer? #(>= % 0) #(< % 256)))
(s/def ::out string?)
(s/def ::fun-greeting string?)
(s/fdef greet :ret ::fun-greeting)
(s/fdef cowsay
                   :args (s/cat :fun-greeting ::fun-greeting)
                   :ret (s/keys :req-un [::out ::exit]))
(s/fdef some-fun
                   :args (s/cat :greeting ::fun-greeting
                                           :names (s/* string?))
                   :ret string?)
