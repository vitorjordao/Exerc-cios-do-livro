(ns conversor.formatador)

(defn formatar [cotacao de para]
  (str "1 " de " equivale a " cotacao " em " para))