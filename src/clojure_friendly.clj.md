```clojure
(ns clojure-friendly
  (:require [clojure.core.match :refer [match]]
            [clojure.core.async :as async]))
```
## Introduction to Clojure, the lispy Java
_Andrea Amantini_
- tw: lo_zampino
- gh: zampino (zampino/clojure-friendly)

## Why Clojure

* A dynamic language/environment (REPL driven dev)

* a Lisp dialect

* for Functional Programming, immutable Data Structures

* symbiotic with an established Platform (JVM)

* designed for Concurrency

[Rich Hickey, clojure rationale](https://clojure.org/about/rationale)
[Rich Hickey, Simple made Easy](https://www.infoq.com/presentations/Simple-Made-Easy)

----
### LIS(t) P(rocessor)
> A programming system called LISP (for LISt Processor) has been developed for the IBM 704 computer by the Artificial Intelligence group at M.I.T. The system was designed to facilitate experiments with a proposed system called the Advice Taker, whereby a machine could be instructed to handle declarative as well as imperative sentences and could exhibit “common sense” in carrying out its instructions...

John McCarthy, [_Recursive Functions of Symbolic Expressions and Their Computation by Machine_, 1960](http://www-formal.stanford.edu/jmc/recursive.pdf)

Paul Graham, [Roots of Lisp](http://paulgraham.com/rootsoflisp.html)

Try to give _common sense_ to expressions as

```clojure

((a b) (c (d e) f))

(f arg_1 arg_2 ... arg_n)
```

Very close to Church's lambda Calculus

```clojure

  ((fn [x] (+ x 1))  2)
```

Defining functions `def` + `fn` in ns

```clojure

(defn say [x] (str "say: " x))
(def hi "Hello Clojure λ")

(say hi)
(clojure-friendly/say hi)
```

___Clojure is omoiconic i.e. Data is Code is Data___

```clojure
(+ 1 2 3)

(list? '(+ 1 2 3))

(1 2 3)
```

* Macros

```clojure

(defmacro λ [x term] `(fn [~x] ~term))

((λ x (+ x 1)) 5)
```

* Functions are first class

```clojure

(defn adder [x]
  (fn [y] (+ x y)))

((adder 41) 1)

((partial + 41) 1)
(defn fivetuple
  (comp (partial)))

(every-pred odd? (partial = 0))

(def mod-3-or-5 (some-fn
                       (partial mod 3)))
```

* Immutability / Navigation / Transformations of Data Structures

```clojure
; sequential colls
(def a-vector [:a :b :c 1 "foo"])
(def a-list (list :a :b :c))
; hashed colls
(def a-map {:a 1 :b 2 :c 3})
(def a-set #{:a :b :c :d :e})

(:a a-map)
(a-map :a)
(:d a-map 4)
(get a-map :a)

(a-vector 0)
(a-vector 5)

(conj m [:d 4] [:e 5])
; show-docs
(assoc m :d 4 :e)
; immutably
m

(keep a-set [1 :d 3 :e :f :g])

(conj a-vector "bar")
(conj a-list "foo")
(conj a-set 1)

(filter (comp odd? :a)
        [{:a 1} {:a 2} {:a 3}])

(let [m  (assoc a-map :a 3)
      m' (assoc m' :b 0)]
  (assoc m' :c 2))

(-> a-map
    (assoc :a 3)
    (assoc :b 0)
    (assoc :c 2))

```

* Polymorphism (multimethods, records, protocols)

```clojure
(defmulti handle :event)

(defmethod handle "up"    [event]
  (update event :value inc))
(defmethod handle "down"  [event]
  (update event :value dec))
(defmethod handle "clear" [event]
  (assoc event :value 0))

(def context
  {:event "up"
   :ns2/type "section"
   :content "hello"
   :value 1})

(handle context)

```


* IFn interface

```clojure

(def a [1 2 3])

(conj a 4)

a

```
* Lazyness

```clojure

(defn log-inc [x] (println (str "increasing: " x)) (inc x))

(let [v (list 1 2 3)
      w (map log-inc v)]
  (println "printing is realising")
  w)
```

* Composing Folds (Transducers)


* Java Interop


* Mutation (Java, Vars, Refs, Agent, Atoms)

```clojure

(def ja (java.util.ArrayList.))

(.add ja 1)

ja

```

* Concurrency



### Extras

* Fn arity overloading / destructuring.

```clojure

(defn do-this
  ([arg]
   (do-this arg {}))
  ([arg opts]
   (let [{p :pref e :extra} opts]
     (str "<<"
          (when p (str p ":"))
          arg
          (when e e)
          ">>")))
  ([arg opts & extra]
   (do-this arg (assoc opts :extra extra))))

(do-this "HI")
(do-this "HI" {:pref "Here"})
(do-this "HI" {:pref "Here"} :a :b :c)
```
