(ns stopper74.pg2-hugsql-adapter
  (:require [hugsql.adapter :as adapter]
            [pg.jdbc :as jdbc]))

(defn query-transformation [s]
  (let [a (atom 0)]
    (apply str (mapv (fn [k]
                       (let [s (str k)]
                         (if (= s "?")
                           (str "$" (swap! a inc))
                           s))) (seq s)))))

(deftype HugsqlAdapterPG2 [default-command-options]
  adapter/HugsqlAdapter
  (execute [_ db sqlvec {:keys [command-options]
                         :or   {command-options default-command-options}
                         :as   options}]
    (let [sqlvec-transformed (query-transformation (first sqlvec))]
      (jdbc/with-transaction [tx db {:isolation :serializable
                                     :read-only false
                                     :rollback-only false}]
            ;;(println (query-transformation (first sqlvec)))
        (jdbc/execute! tx (apply conj [sqlvec-transformed] (rest sqlvec))
                       (if (some #(= % (:command options)) [:insert :i!])
                         (assoc command-options :return-keys true)
                         command-options)))))

  (query [_ db sqlvec options]
    (jdbc/execute! db sqlvec (or (:command-options options) default-command-options)))

  (result-one [_ result _]
    (first result))

  (result-many [_ result _]
    result)

  (result-affected [_ result _]
    (first result))

  (result-raw [_ result _]
    result)

  (on-exception [_ exception]
    (throw exception)))


(defn hugsql-adapter-pg2
  ([]
   (hugsql-adapter-pg2 {}))
  ([default-command-options]
   (->HugsqlAdapterPG2 default-command-options)))