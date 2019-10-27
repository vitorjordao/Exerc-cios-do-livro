(ns conversor.interpretador-de-opcoes
    (:require [clojure.tools.cli :refer [parse-opts]]))
 
(def opcoes-do-programa
  [["-d" "--de moeda base" "moeda base para conversão"
    :default "eur"]
   ["-p" "--para moeda destino"
        "moeda a qual queremos saber o valor"]])

(defn interpretar-opcoes [argumentos]
  (:options (parse-opts argumentos opcoes-do-programa)))