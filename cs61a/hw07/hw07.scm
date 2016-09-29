(define (cddr s)
  (cdr (cdr s)))

(define (cadr s)
  ; YOUR-CODE-HERE
  (car (cdr s)) 
)

(define (caddr s)
  ; YOUR-CODE-HERE
  (car (cdr (cdr s)))
)

(define (sign x)
  (cond ((< x 0) -1)
        ((= x 0) 0)
        (else 1))
  
)

(define (square x) (* x x))

(define (pow b n)
  ; YOUR-CODE-HERE
  (cond ((zero? n) 1)
        ((zero? (- n 1)) b)
        (else (* (square b) (pow b (- n 2)))))
)

(define (ordered? s)
  (cond ((null? (cdr s)) #t)
        ((= (car s) (cadr s)) (ordered? (cdr s)))
        ((< (car s) (cadr s)) (ordered? (cdr s)))
        (else #f))
        

    
)

(define (nodots s)
 (cond ((atom? s) s)
       ((number? (cdr s)) (list (nodots (car s)) (cdr s)))
       (else (cons (nodots (car s)) (nodots (cdr s))))

))
; if cdr = number than change

; Sets as sorted lists


(define (empty? s) (null? s)0)

(define (contains? s v)
    (cond ((empty? s) false)
          ((> (car s) v) false)
          ((= (car s) v) true)
          (else (contains? (cdr s) v))
          ))

; Equivalent Python code, for your reference:
;
; def empty(s):
;     return len(s) == 0
;
; def contains(s, v):
;     if empty(s):
;         return False
;     elif s.first > v:
;         return False
;     elif s.first == v:
;         return True
;     else:
;         return contains(s.rest, v)

(define (add s v)
    (cond ((empty? s) (list v))
          ; YOUR-CODE-HERE
          ((= (car s) v) s)
          ((> (car s) v) (cons v s))
          ((not (list? (cdr s))) (if (= (cdr s) v)
                                          s
                                          (if (> (cdr s) v)
                                              (cons (car s) (cons v (cdr s)))
                                              (cons s v))))
          (else (if (= (car (cdr s)) v)
                    s
                    (if (> (car (cdr s)) v)
                        (cons (car s) (cons v (cdr s)))
                        (cons (car s) (cons (car (cdr s)) (add (cdr (cdr s)) v)))))))
          )


(define (intersect s t)
    (cond ((or (empty? s) (empty? t)) nil)
          (else 
                (cond((= (car s) (car t)) (cons (car s) (intersect (cdr s) (cdr t))))
                     ((< (car s) (car t)) (intersect (cdr s) t))
                     ((> (car s) (car t)) (intersect s (cdr t))))
          )
          ))

; Equivalent Python code, for your reference:
;
; def intersect(set1, set2):
;     if empty(set1) or empty(set2):
;         return Link.empty
;     else:
;         e1, e2 = set1.first, set2.first
;         if e1 == e2:
;             return Link(e1, intersect(set1.rest, set2.rest))
;         elif e1 < e2:
;             return intersect(set1.rest, set2)
;         elif e2 < e1:
;             return intersect(set1, set2.rest)

(define (union s t)
    (cond ((empty? s) t)
          ((empty? t) s)
          ((= (car s) (car t)) (cons (car s) (union (cdr s) (cdr t))))
          ((< (car s) (car t)) (cons (car s) (union (cdr s) t)))
          ((> (car s) (car t)) (cons (car t) (union s (cdr t))))
          (else nil)
          ))


; Binary search trees

; A data abstraction for binary trees where nil represents the empty tree
(define (tree entry left right) (list entry left right))
(define (entry t) (car t))
(define (left t) (cadr t))
(define (right t) (caddr t))
(define (empty? s) (null? s))
(define (leaf entry) (tree entry nil nil))

(define (in? t v)
    (cond ((empty? t) false)
          ((= (entry t) v) true)
          ((< (entry t) v) (in? (right t) v))
          ((> (entry t) v) (in? (left t) v))
          ))

; Equivalent Python code, for your reference:
;
; def contains(s, v):
;     if s.is_empty:
;         return False
;     elif s.entry == v:
;         return True
;     elif s.entry < v:
;         return contains(s.right, v)
;     elif s.entry > v:
;         return contains(s.left, v)

(define (as-list t)
    (cond ((empty? t) nil)
          (else (union (union (as-list (left t)) (list (entry t))) (as-list (right t))))
          ; (else (union (as-list (left t))(as-list (right t))))
    ))

