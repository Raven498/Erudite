x = []
y = []
with open("data-normal.txt", "r") as f:
    i = " "
    xMode = False
    adjMode = False
    while i != '':
        i = f.readline()
        if "X:" in i:
            xMode = True
            continue
        if "ADJ Y:" in i:
            adjMode = True
            continue
        if "Y:" in i or i == "\n" or i == '':
            xMode = False
            adjMode = False
        if xMode:
            x.append(i.strip("\n"))
        if adjMode:
            y.append(i.strip("\n"))
print(x)
with open("data-normal-adj.csv", "w") as o:
    for j in range(len(x)):
        o.write(x[j] + "," + y[j] + "\n")

