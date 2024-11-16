import os
import matplotlib.pyplot as plt
import numpy as np
import math
r = 0.80
filenames = ["data-normal.txt", "data-reward.txt"]
curr_list = "x"
for filename in filenames:
    print(filename)
    x = []
    y = []
    y_adj = []
    with open(filename, "r") as file:
        i = 0
        for line in file:
            data = line.replace("\n", "")
            if data == "Y:":
                curr_list = "y"
                continue
            if data == "X:":
                curr_list = "x"
                continue
            if data == "\n" or data == '' or data == "ADJ Y:":
                continue
            if curr_list == "x":
                x.append(float(data))
            if curr_list == "y":
                y_curr = float(data)
                y.append(y_curr)
                y_adj.append((x[i] * r) + (y_curr * (1 - (r ** 2)) ** (1 / 2)))
                i += 1

    with open(filename, "a") as file:
        for y_ind in y_adj:
            file.write(str(y_ind) + "\n")
    print(x)
    print(y)
    plt.plot(x, y_adj, 'o')
    '''

with open("Data.txt", "a") as file:
    file.write("\nADJ Y:\n")
    for i in range(len(y) - 1):
        y_adj = (x[i] * r) + (y[i] * (1 - (r ** 2)) ** (1 / 2))
        y[i] = y_adj
        file.write(str(y[i]) + "\n")
'''



plt.show()