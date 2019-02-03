```clojure
(ns clojure-friendly
    "A functional friendly Introduction to Clojure: a lispy Java"
  ; (:require [high.confusion.threshold]
  ;           [some.programming.experience])
  (:require [clojure.spec.alpha :as s]
            [clojure.core.async :as async]))
```
_Andrea Amantini_ ([@usenextjournal](https://twitter.com/usenextjournal))
- tw: [@lo_zampino](https://twitter.com/lo_zampino)
- gh: [@zampino](https://github.com/zampino) ([zampino/clojure-friendly](https://github.com/zampino/clojure-friendly))

## Why Clojure?

* A dynamic language/environment ([REPL driven dev](https://clojure.org/about/dynamic))

* a [LISP dialect](https://clojure.org/about/lisp) (code as data)

* for [functional Programming](https://clojure.org/about/functional_programming), immutable Data Structures

* symbiotic with [an established Platform](https://clojure.org/about/jvm_hosted) (JVM)

* designed for Concurrency

Rich Hickey, _[Clojure rationale](https://clojure.org/about/rationale)_ or _[Simple made easy](https://www.infoq.com/presentations/Simple-Made-Easy)_ for inspiration

## LIS(t) P(rocessor)
> LISP has been developed for the IBM 704 computer by the Artificial Intelligence group at M.I.T. to facilitate experiments with a proposed system called the Advice Taker, whereby a machine could be instructed to handle sentences and could exhibit “common sense” in carrying out its instructions...

John McCarthy, [_Recursive Functions of Symbolic Expressions and Their Computation by Machine_, 1960](http://www-formal.stanford.edu/jmc/recursive.pdf) (see also Paul Graham, [_Roots of Lisp_](http://paulgraham.com/rootsoflisp.html))


> we never formally teach the language, because we don’t have to. We just use it! The great advantage of Lisp-like languages is its very few ways of forming compound expressions, and almost no syntactic structure...

Abelson, Sussman, [_SICP_, book/course 6.037 MIT](https://web.mit.edu/alexmv/6.037/sicp.pdf)

...give _common sense_ to expressions like

```clojure

((a b) (c (d e) f))

(f arg_1 arg_2 ... arg_n) ; apply a function f to args
```

Very close to lambda Calculus

```clojure
((fn [x] (+ x 1))  y)   ; --β--> (+ x y)
```

Defining named functions `def` + `fn` in a namespace

```clojure
(defn say [x] (str "say: " x))

(def hi "Hello Clojure 👋")

(say hi)

(clojure-friendly/say hi)
```

Code is Data, Data is Code

```clojure
(+ 1 2 3)

; quote to make code inert
(list?  '(+ 1 2 3))

(1 2 3)

(eval '(+ 1 2 3))

(eval (cons '+ '(1 2 3)))

;; In Python this would be eval(compile("1 + 2 + 3", '<input>', 'eval'))
```
See also https://clojure.org/reference/evaluation and https://clojure.org/reference/reader

* Macros (functions returning inert code)

```clojure

(defmacro λ [x term] `(fn [~x] ~term))

((λ x (+ x 1)) 2)
```

* Functions are first class

```clojure

(defn adder [x]
  (fn [y] (+ x y)))

((adder 41) 1)

((partial + 41) 1)

(def p (every-pred odd?
                   (comp ; is a multiple of 3?
                     (partial = 0)
                     (fn [n] (mod n 3)))))
(p 2)
(p 5)
(p 9)
```

* Immutability / Navigation / Transformations of Data Structures

```clojure
; sequential colls
(def a-vector [:a :b :c 1 "foo"])

(def a-list '(:a :b :c 1 "foo"))

; hashed colls
(def a-map {:a 1 :b 2 "foo" 3})

(def a-set #{:a :b :c :d :e})

(:a a-map)

(a-map :a)

(assoc a-map :d 4)
; 🚨  immutably 🚨

a-map
; i.e. original left intact

(let [m  (assoc a-map :a 3)
      m' (assoc m :b 0)]
  (assoc m' :c 2))

(-> a-map
    (assoc :a 3)
    (assoc :b 0)
    (assoc :c 2))

(def m {:a [1 2 {:foo 3} 4]})

(get-in    m [:a 2 :foo])

(update-in m [:a 2 :foo] + 100)

(conj a-vector "bar")
(conj a-list "foo")
(conj a-set 1)

(filter (comp odd? :a)
        [{:a 1} {:a 2} {:a 3}])

(keep a-set [1 :d 3 :e :f :g])
```

* Operation on Collections / Fold Fusion (Transducers)
See also https://nextjournal.com/zampino/fold

```clojure

(def coll
     [{:a 1}
      {:a 2}
      {:a 3}
      {:a 4}
      {:a 5}
      {:a 6}
      {:a 7}])

(filter odd? (map :a coll))

(reduce + 0 (filter odd? (map :a coll)))

(reduce + 0 [1 2 3 4])

(let [xf (comp
          (map :a)
          (filter odd?))]

  (reduce (xf +) 0 coll))
```

* Control Flow, Conditionals

```clojure
(if true :ok :ko)

(let [x 4]
  (cond
    (odd? x)  (-> x (+ 1) (/ 2))
    (even? x) (/ x 2)))

(let [p1 false p2 true]
  (cond-> {:a 1}
    p1 (assoc :b 2)
    p2 (assoc :c 3)))
```

* Polymorphism (multi-methods, records, protocols)

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

;; context (map) overloading
(-> context
    (ns1/transform input-1)
    (ns2/transform input-2)

    ...

    (ns-n/transform input-n))

```

* Metadata

```clojure

(def a (with-meta [1 2 3] {:safe true}))
(def b {:a a :b 1})
(-> b
    (update :a conj 4)
    (get :a)
    meta)
```

* Java Interop

```clojure
(java.util.Date.)
(java.util.UUID/randomUUID)

;  🤭  mutable?! 🤭
(def ja (java.util.ArrayList.))
(.add ja 1)
ja
(type ja)
```

* Safe State Mutation (Vars, Atoms, Refs, Agents)

```clojure
(def store (atom []))
(deref store) ; (@store)

(defn add [store input]
  ;; 🚨 Don't do side effects here
  (println "trying insert value: " input)
  (Thread/sleep 500)
  (conj store input))

(async/thread
  (println "Thread 1 sees: " @store)
  (swap! store add :x)
  (println "Thread 1 Succeds"))

(async/thread
  (Thread/sleep 501)
  (println "Thread 2 sees: " @store)
  (swap! store add :y)
  (println "Thread 2 Succeds"))

(async/thread
  (println "Thread 3 sees: " @store)
  (swap! store add :z)
  (println "Thread 3 Succeds"))


(deref store)
```

* Concurrency (Delays, Futures, Promises, core.async CSP)
```clojure

(def c (async/chan))
(async/go
  (loop [state []]
    (if-some [val (async/<! c)]
      (do (println "Message In: " val)
          (recur (conj state val)))
      (println "Go Loop Done! " state))))

(async/go (async/>! c :there))
(async/close! c)
```

### Extras

* Clojure Spec Alpha

```clojure
(s/def ::name (s/and string?
                     (s/conformer clojure.string/reverse
                                  clojure.string/reverse)))

(s/def ::code (s/and integer? odd?))

(s/def ::seq (s/+ ::code))

(s/def ::object (s/keys :req [::name ::seq]))

(def object {::name "a@b.c"
             ::seq (list 3 15 1 3])

(s/valid? ::object object)

(s/def ::object.new (s/keys :req [::name
                                  ::seq
                                  ::code]))

(s/def ::object.now (s/or :v2 ::object.new
                          :v1 ::object))

(def object' (assoc object ::code 2))

(s/conform ::object.now object)
(s/conform ::object.now object')
(s/explain-data ::object.now object')

```


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

* Lazyness

```clojure

(defn log-inc [x] (println (str "increasing: " x)) (inc x))

(let [v (list 1 2 3)
      w (map log-inc v)]
  (println "printing is realising")
  w)
```
