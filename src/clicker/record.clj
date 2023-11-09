(ns clicker.record
  (:require [java-time.api :as jt]
            [clicker.config :as config])
  (:import [java.time Instant]))

(def ^:const no-limit-per-slot nil)

(defonce state (atom {}))

(defn time-slot []
  (.getEpochSecond ^Instant
   (jt/truncate-to (jt/instant) config/slot-unit)))

(defn find-tally [id]
  (or (get @state id)
      (-> (swap! state assoc id (atom {}))
          (get id))))

(defn limit [x bound]
  (if (nil? bound) x (min x bound)))

(defn save-clicks [tally slot-limit amnt]
  (when (pos? amnt)
    (let [slot (time-slot)]
      (-> tally
          (swap! (fn [{:as tally :keys [active-slot slot-total]}]
                   (if (not= active-slot slot)
                     (recur (assoc tally
                                   :active-slot slot
                                   :slot-total 0))
                     (let [new-slot-total (limit (+ slot-total amnt) slot-limit)
                           to-add (- new-slot-total slot-total)]
                       (-> tally
                           (update :slot-total + to-add)
                           (update :lifetime-total (fnil + 0) to-add)
                           (assoc :latest-change to-add))))))
          (:latest-change)))))

(defn save-local-clicks [user ip amnt]
  (let [ip-tally (find-tally ip)
        user-tally (find-tally user)
        user-amnt (save-clicks user-tally config/user-limit-per-slot amnt)
        ip-amnt (save-clicks ip-tally config/ip-limit-per-slot user-amnt)]
    {:amnt ip-amnt
     :user-rate-limited? (not= amnt user-amnt)
     :ip-rate-limited? (not= amnt ip-amnt)}))

(defn save-global-clicks [region clan amnt]
  (let [global-tally (find-tally "global")
        region-tally (find-tally region)
        clan-tally (find-tally clan)]
    (->> amnt
         (save-clicks global-tally no-limit-per-slot)
         (save-clicks region-tally no-limit-per-slot)
         (save-clicks clan-tally no-limit-per-slot))))

(defn save-clicks [region clan user ip amnt]
  (let [{:as state rate-limited-amnt :amnt} (save-local-clicks user ip amnt)]
    ))

(defn user-clicks [user ip amnt]
  (let [ip-tally (find-tally ip)
        user-tally (find-tally user)
        global-tally (find-tally "global")

        new-amnt (save-clicks user-tally config/user-limit-per-slot amnt)
        user-limited? (not= amnt new-amnt)

        new-amnt (save-clicks ip-tally config/ip-limit-per-slot new-amnt)
        ip-limited? (not= amnt new-amnt)]
    (->> new-amnt
         (save-clicks global-tally no-limit-per-slot))))

(user-clicks "")

(user-clicks "isaac" 123)
@state
