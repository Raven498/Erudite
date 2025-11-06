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
def optimal_policy2(mono):
    a = 0
    x = ""
    n = 0
    if mono.a < 10:
        n = 3 * mono.a
        a = mono.a * (3 * mono.a)
    else:
        n = 100 * mono.a
        a = mono.a * (100 * mono.a)
    x = mono.x
    return Monomial(a, x, n)

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

    corr_reg = get_corrs(truth_a, input_a)
    print("FINAL CORRS: ", corr_reg)
    print(targets)
    #exp(corr_reg[0], corr_reg[1], )


 
def get_corrs(outputs, inputs):
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
    print("ARGSORT: " + str(np.argsort(diffs)))

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

    print("BR: " + str(br))
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
    corr_reg = [corrs, br]
    return corr_reg
gen(100)
analyze()



'''
MOST MAJOR CONCERNS:
1. Accounting for pre-existing base policy: How will this algorithm maximize efficiency by working off of a correct base policy?
    - What about when the base policy is wrong?
2. When input delta != 1: How to adjust index tracking across lists when the difference across test inputs != 1?
'''