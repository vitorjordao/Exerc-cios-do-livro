(ns financeiro.filtros-aceitacao-test
  (:require [midje.sweet :refer :all]
            [cheshire.core :as json]
            [financeiro.auxiliares :refer :all]
            [clj-http.client :as http]
            [financeiro.db :as db]))

(def transacoes-aleatorias
  '({:valor 7.0M :tipo "despesa"}
    {:valor 88.0M :tipo "despesa"}
    {:valor 106.0M :tipo "despesa"}
    {:valor 8000.0M :tipo "receita"}))

(def transacoes-aleatorias
  '({:valor 7.0M :tipo "despesa"
     :rotulos ["sorvete" "entretenimento"]}
    {:valor 88.0M :tipo "despesa"
     :rotulos ["livro" "educação"]}
    {:valor 106.0M :tipo "despesa"
     :rotulos ["curso" "educação"]}
    {:valor 8000.0M :tipo "receita"
     :rotulos ["salário"]}))

(against-background [(before :facts
                             [(iniciar-servidor porta-padrao)
                              (db/limpar)])
                     (after :facts (parar-servidor))]
                    
                    (fact "Não existem receitas" :aceitacao
                          (json/parse-string (conteudo "/receitas") true)
                          => {:transacoes '()})
                    
                    (fact "Não existem despesas" :aceitacao
                          (json/parse-string (conteudo "/despesas") true)
                          => {:transacoes '()})
                    
                    (fact "Não existem transações" :aceitacao
                          (json/parse-string (conteudo "/transacoes") true)
                          => {:transacoes '()})
                    
                    (against-background
                     [(before :facts (doseq [transacao transacoes-aleatorias]
                                       (db/registrar transacao)))
                      (after :facts (db/limpar))]

                     (fact "Existem 3 despesas" :aceitacao
                           (count (:transacoes (json/parse-string
                                                (conteudo "/despesas") true))) => 3)

                     (fact "Existe 1 receita" :aceitacao
                           (count (:transacoes (json/parse-string
                                                (conteudo "/receitas") true))) => 1)

                     (fact "Existem 4 transações" :aceitacao
                           (count (:transacoes (json/parse-string
                                                (conteudo "/transacoes") true))) => 4)

                     (fact "Existe 1 receita com rótulo 'salário'"
                           (count (:transacoes (json/parse-string
                                                (conteudo "/transacoes?rotulos=salário") true))) => 1)

                     (fact "Existem 2 despesas com rótulo 'livro' ou 'curso'"
                           (count (:transacoes (json/parse-string
                                                (conteudo "/transacoes?rotulos=livro&rotulos=curso")
                                                true))) => 2)

                     (fact "Existem 2 despesas com rótulo 'educação'"
                           (count (:transacoes (json/parse-string
                                                (conteudo "/transacoes?rotulos=educação") true))) => 2)))