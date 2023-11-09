(ns clicker.config)

(def slot-unit :minutes)
(def user-limit-per-slot (* 10 60))
(def ip-limit-per-slot (* user-limit-per-slot 20))
