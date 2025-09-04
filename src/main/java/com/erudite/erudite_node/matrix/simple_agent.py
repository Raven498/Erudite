import random
import matplotlib.pyplot as plt
import numpy as np

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

inputs = []
truths = []
outputs = []
ry = 100
rn = 50
rewards = []

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

def gen(n):
    for i in range(n):
        mono = Monomial(i, "x", i) # Can also do random numbers instead
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
    input_a = []
    input_n = []
    input_x = []
    # Step 1: Graph R vs input a, x, n
    for i in range(len(rewards)): # Populate input a respective to rewards
        input_a.append(inputs[i].a)
        input_n.append(inputs[i].n)
        input_x.append(inputs[i].x)
    print(input_n)
    print(rewards)
    plt.plot(np.array(input_a), np.array(rewards))
    plt.show()
    j = 0

    '''
    Algorithm Draft for Corr Detection (with R vs I.a, I.x, I.n):
    For each input attr:
        Iterate through rewards:
            If current reward is rn and previous reward is ry (reward switches from ry -> rn):
                Save input attr value corresponding to previous reward
                (Target condition = x <= i, where i = previous input attr value, x = label for input attr)
                OR (Target condition = x < y, where y = current input attr value, x = label for input attr)

    Need to investigate what happens when I.n follows different target conditions in optimal policy than I.a --> what will reward graphs be?
    '''
    
def corr_exp():
    pass

gen(100)
analyze()
