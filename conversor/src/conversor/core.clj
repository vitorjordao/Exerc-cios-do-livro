(ns conversor.core
  (:require [conversor.formatador :refer [formatar]]
            [conversor.interpretador-de-opcoes :refer
              [interpretar-opcoes]]
            [conversor.cambista :refer [obter-cotacao]]))

(defn -main
  [& args]
  (let [{de :de para :para} (interpretar-opcoes args)]
  (-> (obter-cotacao de para)
      (formatar de para)
      (prn))))
