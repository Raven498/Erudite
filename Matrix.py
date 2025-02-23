import random
'''
PROPOSED ARCHITECTURE:
Operations:
(Inputs)    (Outputs)
Numeric ---> Numeric  (ex. scaling, corr. adj.)
Numeric ---> File     (ex. generation, organization/labelling)
File    ---> Numeric  (ex. data retrieval, statistics)
File    ---> File     (ex. conversion, scaling, corr. adj)
'''
#GENERAL MODULE: FILE MANAGEMENT

#MODULE 1: PRL
#Numeric --> Numeric

#Numeric --> File
x = []
y = []

#INPUTS
n = 1000
x_max = 100 
x_min = 0
y_max = 100
y_min = 0
r = 0.75
for i in range(n):
    x.append(random.randint(x_min, x_max))
    y.append(random.randint(y_min, y_max))

#INPUTS
x = []
y = []
name = "data.csv"
f = open("data.csv", "w")
for i in range(n):
    x.append(random.randint(x_min, x_max))
    y.append((x[i] * r) + (random.randint(y_min, y_max) * (1 - (r ** 2)) ** (1 / 2)))
    f.write(str(x[i]) + "," + str(y[i]))
