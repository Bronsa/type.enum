# type.enum

Enum types in clojure.


## Installation

In Leiningen:

```clojure
:dependencies [[bronsa/type.enum "0.0.1"]]
```

## Usage

```clojure
user=> (require '[type.enum :refer [enum]])
nil
user=> (enum :a :b :c)
#type/enum [:a <=> 0, :b <=> 1, :c <=> 2]
user=> (*1 :a)
0
user=> (*2 0)
:a
user=> (enum :a 3 :b 5 :d :e)
#type/enum [:a <=> 3, :b <=> 5, :d <=> 6, :e <=> 7]
```

## License

Copyright © 2013 Bronsa

Distributed under the Eclipse Public License, the same as Clojure.
