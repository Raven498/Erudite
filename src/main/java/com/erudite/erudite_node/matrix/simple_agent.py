import math
import random
import matplotlib.pyplot as plt
import numpy as np

import heapq
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
    truth_a = []
    truth_x = []
    truth_n = []
    # Step 1: Graph R vs input a, x, n
    for i in range(len(rewards)): # Populate input a respective to rewards
        input_a.append(inputs[i].a)
        input_n.append(inputs[i].n)
        input_x.append(inputs[i].x)
        truth_a.append(truths[i].a)
        truth_n.append(truths[i].n)
        truth_x.append(truths[i].x)
        
    print(input_n)
    print(input_x)
    print(rewards)
    
    plt.plot(np.array(input_a), np.array(rewards))
    plt.show()
    labels = ["a", "x", "n"]
    in_label_reg = [input_a, input_x, input_n]
    tru_label_reg = [truth_a, truth_x, truth_n]
    regs = [in_label_reg, tru_label_reg]
    targets = []
    for i in range(len(labels)):
        conds = []
        for j in range(len(rewards)):
            if j > 0 and (rewards[j] == rn and rewards[j-1] == ry):
                conds.append(labels[i] + "<=" + str(in_label_reg[i][j]))
        targets.append(conds)
    print(targets)

    for i in range(len(tru_label_reg)):
        for j in range(len(in_label_reg)):
            print("T." + labels[i] + " vs I." + labels[j])
            #plt.plot(np.array(in_label_reg[j]), np.array(tru_label_reg[i]))
            #plt.show()

    corr_exp(truth_a, input_a)
    
    '''
    cs = CubicSpline(np.array(input_a), np.array(truth_a))
    d = cs.derivative()(input_a)
    print("D" + str(d))
    
    '''
    '''
    poly = sklearn.preprocessing.PolynomialFeatures(degree=2)
    X_poly = poly.fit_transform(np.array(input_a).reshape(-1, 1))

    poly.fit(X_poly, truth_a)
    lin2 = sklearn.linear_model.LinearRegression()
    lin2.fit(X_poly, truth_a)

    plt.scatter(input_a, truth_a, color='blue')

    plt.plot(input_a, lin2.predict(poly.fit_transform(np.array(input_a).reshape(-1, 1))), color='red')
    plt.title('Linear Regression')
    plt.xlabel('Temperature')
    plt.ylabel('Pressure')

    plt.show()
    '''
    
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
def sort(nums):
   return heapq.heapify(nums.copy()) 
 
def corr_exp(outputs, inputs):
    # Calculate diffs between all outputs to get final differentiation
    # TODO: Need to identify each unique function and its derivative - how to do this with final diff list?
        # Potential solution: Use relative percent change in output values to detect higher changes at points across all diff lists
            # Wherever these spikes occur, that's where functions change
            # Monitor each range for each function identified this way, and if each list only has 1 element, that's the final diff list
            # Use final diff list to get derivative, calculate original function
            # TODO: WRITE A SCRIPT TO TEST THIS
    
    diffs = outputs.copy()
    rel_change = []
    print("REL CHANGE: " + str(rel_change))
    print("TRUTH A: " + str(outputs))
    print("INPUT A: " + str(inputs))
    j = 0
    while len(set(diffs)) >= 10:
        o_diffs = diffs.copy()
        o = o_diffs[0]
        for i in range(len(diffs)): 
            if i > 0:
                diffs[i] = diffs[i] - o
                o = o_diffs[i]
        j += 1
    print("BITCH: " + str(np.argsort(diffs)))
    # If any rel changes become undefined (ex. index zero, division by zero, etc.), these changes won't be added to rel change list
    # This creates index discrepancy between rel change, diff lists --> this counter is used to adjust for that.
    for i in range(len(diffs)):
        if i > 0 and outputs[i-1] != 0:
            rel_change.append(((diffs[i] - diffs[i-1]) / diffs[i-1]) * 100)
        else:
            rel_change.append(False)

    br = []
    print(np.argsort(diffs)[-4:])
    for n in np.argsort(diffs)[-4:]:
        r = rel_change[n]
        print(r)
        if r != False and r >= 100:
            br.append(inputs[n])

    print("BITCH ASS BR: " + str(br))
    print(j)

    corrs = []
    for b in br:
        d = diffs[b+2]
        corr = [d, 1]
        for i in range(j-1):
            corr[1] += 1
            corr[0] /= corr[1]
            print(corr)
        corrs.append(corr)
    print(corrs)
    print("TRUTH A: " + str(outputs))
    print("INPUT A: " + str(inputs))
    print("DIFFS: " + str(diffs))
    print("REL CHANGE: " + str(rel_change))
    print("CUTOFF 1 REL CHANGE: " + str(rel_change[8]))
    print("CUTOFF 2 REL CHANGE: " + str(rel_change[28]))
    print(rel_change[8] == max(rel_change))
    plt.plot(np.array(inputs), np.array(rel_change))
    plt.show()
    return corrs
gen(100)
analyze()

'''
CORRELATION EXTRACTION ALGORITHM:
1. Exit Condition for Diffs Extraction - Ideas:
- First list with less than 10 unique elements
- First list with >~90% of elements being equal to < 3 elements
2. Extracting Breakpoint Indexes
- Idea 1:
    - Take last 5 indexes in argsort of diff list, compare with corresponding rel change indexes
    - Only recognize indexes that are >~100% rel change as breakpoints
'''