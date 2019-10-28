(ns financeiro.handler-test
  (:require [midje.sweet :refer :all]
            [ring.mock.request :as mock]
            [financeiro.handler :refer :all]))

(facts "Dá um 'Olá, mundo!' na rota raiz"
             (let [response (app (mock/request :get "/"))]
               (fact "o stats da resposta é 200"
                     (:status response) => 200)
               (fact "o texto do corpo é 'Hello World'"
                     (:body response) => "Olá, mundo!")))

(facts "Rota inválida não existe"
       (let [response (app (mock/request :get "/invalid"))]
         (fact "o código de erro é 404"
               (:status response) => 404)
         (fact "o texto do corpo é 'Recurso não encontrado'"
               (:body response) => "Recurso não encontrado")))
