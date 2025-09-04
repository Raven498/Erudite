import random
class Monomial:
    a = 0
    x = ""
    n = 0

    def __init__(self, a, x, n):
        self.a = a
        self.x = x
        self.n = n

    def __eq__(self, other):
        if isinstance(other, Monomial) and self.a == other.a and self.x == other.x and self.n == other.n:
            return True
        return False

def optimal_policy(mono):
    a = 0
    x = ""
    n = 0
    if mono.a < 10:
        n = 3 * mono.a
        a = mono.a * (3 * mono.a)
    elif mono.a < 30:
        n = 6 * mono.a
        a = mono.a * (6 * mono.a)
    else:
        n = 9 * mono.a
        a = mono.a * (9 * mono.a)
    x = mono.x
    return Monomial(a, x, n)

def base_policy(mono):
    a = mono.a * (3 * mono.a)
    n = 3 * mono.a
    x = mono.x
    return Monomial(a, x, n)

inputs = []
truths = []
outputs = []
ry = 100
rn = 50
rewards = []
def gen(n):
    for i in range(n):
        mono = Monomial(random.randint(0, 100), "x", random.randint(0, 100))
        inputs.append(mono)
        truth = optimal_policy(mono)
        output = base_policy(mono)
        truths.append(truth)
        outputs.append(output)
        if truth == output:
            rewards.append(ry)
        else:
            rewards.append(rn)

def analyze():
    regions = []
    for i in range(len(rewards)):
        
            
def corr_exp():
    
    '''
input_monos = [
    Monomial(5, x, 2),
    Monomial(4, x, 1),
    Monomial(9, x, 5),
    Monomial(3, x, 10),
    Monomial(20, x, 7),
    Monomial(35, x, 7),
    Monomial(27, x, 6),
    Monomial(5, x, 1),
    Monomial(5, x, 5),
    Monomial(7, x, 10)]

truth_monos = [
    Monomial(75, x, 15),
    Monomial(
    '''
