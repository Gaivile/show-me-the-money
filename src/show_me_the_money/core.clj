(ns show-me-the-money.core)

;; No taxes on personal allowance
(def personal-allowance 11000)

;; Earnings from 11,001 to 43,000
(def income-tax-rate-basic 20/100)

;; Earnings from 43,001 to 150,000
(def income-tax-rate-high 40/100)

;; Earnings over 150,001
(def income-tax-rate-additional 45/100)

;; todo later :)
(def national-insurance-rate 11/100)

;; calculate salary after basic tax
(defn basic-tax
  [full-salary]
  (let [taxable-salary (- full-salary personal-allowance)]
     (- full-salary (* taxable-salary income-tax-rate-basic))))

;; calculate salary after higher tax
(defn high-tax
  [full-salary]
  (if (<= full-salary 100001)
    (let [taxable-salary (- full-salary personal-allowance 32000)]
       (- full-salary (* taxable-salary income-tax-rate-high) 6400))
  (let [allowance (- personal-allowance (/ (- full-salary 100000) 2))
        taxable-salary (- full-salary (if (<= allowance 0) 0 allowance) 32000)]
       (- full-salary (* taxable-salary income-tax-rate-high) 6400))))

;; calculate salary after additional tax
(defn additional-tax
  [full-salary]
  (let [taxable-salary (- full-salary 150000)]
    (- full-salary (* taxable-salary income-tax-rate-additional) 6400 47200)))

;; put it all together
(defn apply-tax
  [income]
  (if (<= income personal-allowance)
    income
    (if (and (> income personal-allowance) (<= income 43000))
      (basic-tax income)
      (if (and (> income 43000) (<= income 150000))
        (high-tax income)
        (additional-tax income)))))

;; tests
(apply-tax 10000.0)
;; => 10000.0
(apply-tax 15000.0)
;; => 14200.0
(apply-tax 25000.0)
;; => 22200.0
(apply-tax 35000.0)
;; => 30200.0
(apply-tax 40000.0)
;; => 34200.0
(apply-tax 45000.0)
;; => 37800.0
(apply-tax 65000.0)
;; => 49800.0
(apply-tax 95000.0)
;; => 67800.0
(apply-tax 115000.0)
;; => 76800.0
(apply-tax 140000.0)
;; => 90400.0
(apply-tax 155000.0)
;; => 99150.0
(apply-tax 190000.0)
;; => 118400.0
(apply-tax 225000.0)
;; => 137650.0


;; calculate monthly, weekly and hourly salary after taxes
(defn monthly [income]
  (float (/  (apply-tax income) 12)))

(defn weekly [income]
  (float (/ (apply-tax income) 52)))

(defn hourly [income]
  (float (/ (weekly income) 40)))

;; tests
(monthly 50000)
;; => 3400.0
(weekly 50000)
;; => 784.61536
(hourly 50000)
;; => 19.615383
