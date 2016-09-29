# CS 61A Fall 2014
# Name: Jaewon Lee 
# Login: cs61a-aks

from operator import add, sub, mul

def a_plus_abs_b(a, b):
    """Return a+abs(b), but without calling abs.

    >>> a_plus_abs_b(2, 3)
    5
    >>> a_plus_abs_b(2, -3)
    5
    """
    if b < 0:
        f = sub
    else:
        f = add
    return f(a, b)

def two_of_three(a, b, c):
    """Return x*x + y*y, where x and y are the two largest members of the
    positive numbers a, b, and c.

    >>> two_of_three(1, 2, 3)
    13
    >>> two_of_three(5, 3, 1)
    34
    >>> two_of_three(10, 2, 8)
    164
    >>> two_of_three(5, 5, 5)
    50
    """    
    return add(mul(max(a,b,c),max(a,b,c)), mul(min(max(a,b), max(b,c), max(a,c)),min(max(a,b),max(b,c),max(a,c))))

def if_function(condition, true_result, false_result):
    """Return true_result if condition is a true value, and
    false_result otherwise.

    >>> if_function(True, 2, 3)
    2
    >>> if_function(False, 2, 3)
    3
    >>> if_function(3==2, 3+2, 3-2)
    1
    >>> if_function(3>2, 3+2, 3-2)
    5
    """
    if condition:
        return true_result
    else:
        return false_result

def with_if_statement():
    """
    >>> with_if_statement()
    1
    """
    if c():
        return t()
    else:
        return f()

def with_if_function():
    return if_function()

def c():
    return 3 == 3

def t():
    return 1
def f():
    return 1/0

def hailstone(n):
    """Print the hailstone sequence starting at n and return its
    length.

    >>> a = hailstone(10)
    10
    5
    16
    8
    4
    2
    1
    >>> a
    7
    """
    print(n)
    k = 1 
    while n != 1: 
        if n % 2 == 0:
            print (int(n / 2))
            n = n / 2

        else:
            print (int(n * 3 + 1))
            n = n * 3 + 1
        k = k + 1
    return k
    
    


    

challenge_question_program = """
"*** YOUR CODE HERE ***"
"""



