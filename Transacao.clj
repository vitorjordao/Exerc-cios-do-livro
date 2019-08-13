(defn resumo [transacao] 
    (select-keys transacao [:valor :tipo :data]))

(def transacoes [
    {:valor 33.3 :tipo "despesa" :comentario "Almoço" :data "19/11/2016"}
    {:valor 2700.0 :tipo "receita" :comentario "Bico" :data "01/12/2016"}   
    {:valor 29.0 :tipo "despesa" :comentario "Livro de Clojure" :data "03/12/2016"}])

(map resumo transacoes)

(defn despesa? [transacao]
  (= (:tipo transacao) "despesa"))

(filter despesa? transacoes)

(defn so-valor [transacao]
  (:valor transacao))

;;thread-first
(-> (first transacoes)
    (so-valor))

;; Isso:
(reduce + (map so-valor (filter despesa? transacoes)))
;; é o mesmo que isso:
(reduce + 
        (map #(:valor %) 
             (filter #(= (:tipo %) "despesa") 
                     transacoes)))

;; Isso:
(filter (fn [transacao]
          (> (:valor transacao) 100))
        transacoes)
;; é o mesmo que isso:
(filter #(> (:valor %) 100) transacoes)

;; thread-last
(->> (filter despesa? transacoes)
     (map so-valor)
     (reduce +))

(def transacoes  [{:valor 33.0 :tipo "despesa" :comentario "Almoço" 
                   :moeda "R$" :data "19/11/2016"} 
                  {:valor 2700.0 :tipo "receita" :comentario "Bico" 
                   :moeda "R$" :data "01/12/2016"} 
                  {:valor 29.0 :tipo "despesa" :comentario "Livro de Clojure" 
                   :moeda "R$" :data "03/12/2016"}])

;; Isso:
(def ola (fn [nome] (str "Olá, " nome "!")))
(ola "mundo novo")
;; É o mesmo que isso:
(defn ola [nome] (str "Olá, " nome "!"))
(ola "mundo novo")
;; defn é uma macro de def + fn

;; Isso:
(defn valor-sinalizado [transacao] 
  (if (= (:tipo transacao) "despesa") 
    (str (:moeda transacao) " -" (:valor transacao))
    (str (:moeda transacao) " +" (:valor transacao))))
;; É o mesmo que isso:
(defn valor-sinalizado [transacao]
  (let [moeda (:moeda transacao)
        valor (:valor transacao)]
    (if (= (:tipo transacao) "despesa")
        (str moeda " -" valor)
        (str moeda " +" valor))))


(map valor-sinalizado transacoes)

;; First pega o primeiro elemento
(valor-sinalizado (first transacoes))
;; Second pega o segundo elemento
(valor-sinalizado (second transacoes))

(def transacao-aleatoria {:valor 9.0}) 

(valor-sinalizado transacao-aleatoria)
;; " +9.0"

(:moeda transacao-aleatoria)
;; nil

(:moeda transacao-aleatoria "R$")
;; "R$"

(defn valor-sinalizado [transacao]
  (let [moeda (:moeda transacao "R$")
        valor (:valor transacao)]
    (if (= (:tipo transacao) "despesa")
      (str moeda " -" valor)
      (str moeda " +" valor))))

(valor-sinalizado transacao-aleatoria)

(defn data-valor [transacao]
  (str (:data transacao) " => " (valor-sinalizado transacao)))

(map data-valor transacoes)

(defn transacao-em-yuan [transacao] 
  (assoc transacao :valor (* 2.15 (:valor transacao))
         :moeda "¥"))

(map transacao-em-yuan transacoes)

(def cotacoes 
  {:yuan {:cotacao 2.15 :simbolo "¥"}})

;; Isso
(defn transacao-em-yuan [transacao]
  (assoc transacao :valor (* (:cotacao (:yuan cotacoes))
                             (:valor transacao))
         :moeda (:simbolo (:yuan cotacoes))))
;; É o mesmo que isso:
(defn transacao-em-yuan [transacao]
  (assoc transacao :valor (* (get-in cotacoes [:yuan :cotacao])
                             (:valor transacao))
         :moeda (get-in cotacoes [:yuan :simbolo])))
;; Que é o mesmo que isso:
(defn transacao-em-yuan [transacao]
  (let [yuan (:yuan cotacoes)]
    (assoc transacao :valor (* (:cotacao yuan) (:valor transacao))
                               :moeda (:simbolo yuan))))


(transacao-em-yuan (first transacoes))

(data-valor (first transacoes))

(data-valor (transacao-em-yuan (first transacoes)))

(class 3.1)
;; java.lang.Double
(* 3.1 3.1)
;; 9.610000000000001
(class 3.1M)
;; java.math.BigDecimal
(* 3.1M 3.1)
;; 9.610000000000001
(* 3.1M 3.1M)
;; 9.61M
(class (* 3.1M 3.1M))
;; java.math.BigDecimal

;; Esse "M" na frente dos números transforma eles em BigDecimal 

(def cotacoes 
  {:yuan {:cotacao 2.15M :simbolo "¥"}})

(def transacoes
  [{:valor 33.0M :tipo "despesa" :comentario "Almoço" 
    :moeda "R$" :data "19/11/2016"}
   {:valor 2700.0M :tipo "receita" :comentario "Bico" 
    :moeda "R$" :data "01/12/2016"}
   {:valor 29.0M :tipo "despesa" :comentario "Livro de Clojure" 
    :moeda "R$" :data "03/12/2016"}])

;; Isso
(defn texto-resumo-em-yuan [transacao]
  (data-valor (transacao-em-yuan transacao)))
;; É o mesmo que isso:
(defn texto-resumo-em-yuan [transacao]
  (-> (transacao-em-yuan transacao)
      (data-valor)))
;; Que é o mesmo quee isso:
(def texto-resumo-em-yuan (comp data-valor transacao-em-yuan))

(map texto-resumo-em-yuan transacoes)

;; Isso
(defn transacao-em-yuan [transacao]
  (let [yuan (:yuan cotacoes)]
    (assoc transacao :valor (* (:cotacao yuan) (:valor transacao))
           :moeda (:simbolo yuan))))
;; É o mesmo que isso:
(defn transacao-em-yuan [transacao]
  (let [{yuan :yuan} cotacoes]
    (assoc transacao :valor (* (:cotacao yuan) (:valor transacao))
           :moeda (:simbolo yuan))))
;; Que é o mesmo que isso:
(defn transacao-em-yuan [transacao]
  (let [{{cotacao :cotacao simbolo :simbolo} :yuan} cotacoes]
    (assoc transacao :valor (* cotacao (:valor transacao))
           :moeda simbolo)))
(map transacao-em-yuan transacoes)

(def cotacoes
  {:yuan {:cotacao 2.15M :simbolo "¥"}
  :euro {:cotacao 0.28M :simbolo "€"}})

(defn transacao-em-outra-moeda [moeda transacao]
  (let [{{cotacao :cotacao simbolo :simbolo} moeda} cotacoes]
    (assoc transacao :valor (* cotacao (:valor transacao))
           :moeda simbolo)))

(defn transacao-em-outra-moeda [moeda transacao]
  (let [{{cotacao :cotacao simbolo :simbolo} moeda} cotacoes]
    (assoc transacao :valor (* cotacao (:valor transacao))
           :moeda simbolo)))

(transacao-em-outra-moeda :euro (last transacoes))

;;E é uma aplicação parcial porque declaramos parte dos argumentos, não todos. A outra parte do argumento virá em algum outro momento, quando a função for, de fato, aplicada
(def transacao-em-yuan (partial transacao-em-outra-moeda :yuan))

(def transacao-em-euro (partial transacao-em-outra-moeda :euro))

(transacao-em-euro (first transacoes))

(transacao-em-yuan (first transacoes))

(map transacao-em-yuan transacoes)

(clojure.string/join ", " (map transacao-em-yuan transacoes))

(def junta-tudo (partial clojure.string/join ", "))

(junta-tudo (map transacao-em-yuan transacoes))

(def de-para [{:de "a" :para "4"}
              :de "e" :para "3"
              :de "i" :para "1"
              :de "o" :para "0"])

(defn escrita-hacker [texto dicionario]
  (if (empty? dicionario)
    texto
    (let [conversao (first dicionario)]
      (escrita-hacker (clojure.string/replace texto
                                              (:de conversao)
                                              (:para conversao))
                      (rest dicionario)))))


(escrita-hacker "alameda" de-para)


(defn transacao-em-outra-moeda [cotacoes moeda transacao]
  (let [{{cotacao :cotacao simbolo :simbolo} moeda} cotacoes]
    (assoc transacao :valor (* cotacao (:valor transacao))
           :moeda simbolo)))

(transacao-em-outra-moeda cotacoes :euro (last transacoes))

(defn transacao-convertida [cotacoes moeda transacao]
  (let [{{cotacao :cotacao simbolo :simbolo} moeda} cotacoes] 
    (assoc transacao :valor (* cotacao (:valor transacao))
           :moeda simbolo)))

(def transacao-em-outra-moeda 
  (partial transacao-convertida cotacoes))

(transacao-em-outra-moeda :euro (last transacoes))


(defn transacao-em-outra-moeda 
  ([cotacoes moeda transacao]
   (let [{{cotacao :cotacao simbolo :simbolo} moeda} cotacoes]
     (assoc transacao :valor (* cotacao (:valor transacao))
            :moeda simbolo)))
  ([moeda transacao]
   (transacao-em-outra-moeda cotacoes moeda transacao)))

(transacao-em-outra-moeda :euro (last transacoes))