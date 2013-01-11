(ns type.enum
  (:import (clojure.lang ILookup IFn)
           (java.io Writer)))

(deftype EnumType [k->v v->k]
  IFn
  (invoke [this k]
    (if (number? k)
      (v->k k)
      (k->v k)))
  (invoke [this k not-found]
    (if (number? k)
      (v->k k not-found)
      (k->v k not-found)))

  ILookup
  (valAt [this k]
    (.invoke this k))
  (valAt [this k not-found]
    (.invoke this k not-found))

  Object
  (toString [this]
    (str "#type/enum ["
         (apply str (interpose ", "
                               (map (fn [[k v]]
                                      (str k " <=> "  v)) k->v)))
         "]")))

(defmethod print-method EnumType
  [^EnumType e ^Writer w]
  (.write w (str e)))

(declare enum*)
(defn read-enum [elems]
  (let [elems (partition 3 elems)]
    (apply enum* (reduce conj {} (map (juxt first last) elems)))))

(defn enum* [k->v]
  {:pre [(map? k->v)
         (every? keyword? (keys k->v))
         (every? integer? (vals k->v))]}
  (let [v->k (zipmap (vals k->v) (keys k->v))]
    (EnumType. k->v v->k)))

(defn enum
  [& args]
  {:pre [(every? #(or (keyword? %)
                      (integer? %)) args)]}
  (if (every? keyword? args)
    (enum* (apply hash-map (interleave args (range))))
    (if (and (even? (count args))
             (every? keyword? (take-nth 2 args))
             (every? integer? (take-nth 2 (rest args))))
      (enum* (apply hash-map args))
      (loop [i 0 args args k->v {}]
        (if-not args
          (enum* k->v)
          (let [e (second args)]
            (if (integer? e)
              (recur (inc e) (nnext args) (assoc k->v (first args) e))
              (recur (inc i) (next args) (assoc k->v (first args) i)))))))))
