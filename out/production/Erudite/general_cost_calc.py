import matplotlib.pyplot as plt
x_train_temp = []
y_train_temp = []
y_train = []
x_train = []
w = 0
b = 0
m = 0
cost = 0
get_y = False
preds = []
def model(x):
    return (w * x) + b

def calc_cost():
    cost = 0
    for i in range(m):
        y_hat = model(x_train[i])
        preds.append(y_hat)
        cost += (y_hat - y_train[i])**2
    cost /= (2 * m)
    return cost

file = open('data-upgrade5.txt', 'r')
for line in file:
    if "[Y]" in line:
        get_y = True
        continue
    if get_y == True:
        y = float(line.replace("\n", ""))
        y_train_temp.append(y)
    elif not "[X]" in line:
        x = float(line.replace("\n", ""))
        x_train_temp.append(x)
        
m = len(x_train_temp)
w = float(input("Enter w: "))
b = float(input("Enter b: "))

for i in range(m):
    if y_train_temp[i] <= 25000:
        y_train.append(y_train_temp[i])
        x_train.append(x_train_temp[i])
m = len(x_train)
calc_cost()

preds.sort()
x_train.sort()
plt.scatter(x_train, y_train)
plt.plot([x_train[0], x_train[m-1]], [preds[0], preds[m-1]])
plt.show()

costs = []
w_list = []
for i in range(10):
    w += 0.1
    b += 10
    w_list.append(w)
    costs.append(calc_cost())
plt.scatter(w_list, costs)
plt.plot()
plt.show()

print(min(costs))
print(w_list[costs.index(min(costs))])
