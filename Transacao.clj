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