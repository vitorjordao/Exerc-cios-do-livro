(ns conversor.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [cheshire.core :refer [parse-string]]
            [clj-http.client :as http-client])
  (:gen-class))
"""
(defn valores-em [argumentos]
  (cond
    (.startsWith argumentos \"--de=\")
    {:de (.substring argumentos 5)}
    
    (.startsWith argumentos \"--para=\")
    {:para (.substring argumentos 7)}
    :else {}))

(defn -main
  \"Exercício do livro\"
  [& args]
  (println \"Temos \" (count args) \" argumentos\")
  (println \"Eles são: \" (map valores-em args)))

"""

(def opcoes-do-programa
  [["-d" "--de moeda base" "moeda base para conversão"
    :default "eur"]
   ["-p" "--para moeda destino"
         "moeda a qual queremos saber o valor"]])

(def chave (System/getenv "CHAVE_API"))

(def api-url
  "https://free.currencyconverterapi.com/api/v6/convert")

(defn parametrizar-moedas [de para]
  (str de "_" para))

(defn obter-cotacao [de para]
  (let [moedas (parametrizar-moedas de para)]
    (-> (:body (http-client/get api-url
                                {:query-params {"q" moedas
                                                "apiKey" chave}}))
               (parse-string)
               (get-in ["results" moedas "val"]))))

(defn- formatar [cotacao de para]
  (str "1 " de " equivale a " cotacao " em " para))

(defn -main
  [& args]
  (let [{:keys [de para]} (:options
                        (parse-opts args opcoes-do-programa))]
  (-> (obter-cotacao de para)
      (formatar de para)
      (prn))))
