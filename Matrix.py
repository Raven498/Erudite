import random
import csv
import os

class Numeric:
    def __init__(self, x, y, x_min, x_max, y_min, y_max, r, n):
        self.x = x
        self.y = y
        self.x_min = x_min
        self.x_max = x_max
        self.y_min = y_min
        self.y_max = y_max
        self.r = r
        self.n = n

    def toString(self):
        print(self.x)
        print(self.y)
        print(self.x_min)
        print(self.x_max)
        print(self.y_min)
        print(self.y_max)
        print(self.r)
        print(self.n)


def numeric_numeric(numeric, **kwargs):
    out_x = []
    out_y = []
    x_min = kwargs.get("x_min")
    x_max = kwargs.get("x_max")
    y_min = kwargs.get("y_min")
    y_max = kwargs.get("y_max")
    r = kwargs.get("r")
    n = kwargs.get("n")
    s = kwargs.get("s")
    if numeric.x == [""] and numeric.y == [""]:
        for i in range(int(n)):
            out_x.append(random.randint(x_min, x_max))
            out_y.append((out_x[i] * r) + (
                    random.randint(y_min, y_max) * (1 - (r ** 2)) ** (1 / 2)))
        return Numeric(out_x, out_y, x_min, x_max, y_min, y_max, r, n)
    else:
        for i in range(int(n)):
            out_x.append(numeric.x[i] * s)
            out_y.append((out_x[i] * r) + (
                    random.randint(y_min, y_max) * (1 - (r ** 2)) ** (1 / 2)))
        return Numeric(out_x, out_y, numeric.x_min, numeric.x_max, y_min, y_max, r, n)

def numeric_file(in_numeric, name):
    with open(name, "w") as f:
        f.write("[X]:\n")
        for num in in_numeric.x:
            f.write(str(num) + "\n")
        f.write("[Y]:\n")
        for num in in_numeric.y:
            f.write(str(num) + "\n")
        return f


def file_numeric(name, s_mode):
    x = []
    y = []
    with open(name, "r") as f:
        if s_mode:  # STATS - implement for V1
            pass
        else:
            x_mode = False
            y_mode = False
            while True:
                l = f.readline().rstrip("\n")
                if l == "":
                    break
                if "[X]" in l:
                    x_mode = True
                    y_mode = False
                    continue
                elif "[Y]" in l:
                    y_mode = True
                    x_mode = False
                    continue
                elif l == " " or l == "\n":
                    continue
                if x_mode:
                    x.append(float(l))
                if y_mode:
                    y.append(float(l))
        return Numeric(x, y, 0, 0, 0, 0, 0, len(x))


def file_file(name1, name2):
    with open(name2, 'w') as f_c:
        writer = csv.writer(f_c)
        writer.writerow(["X", "Y"])
        numeric = file_numeric(name1, False)
        for i in range(len(numeric.x)):
            writer.writerow([numeric.x[i], numeric.y[i]])
    return f_c


def parseInputNumeric(params):
    x = []
    y = []
    a = x
    cursor = 0
    for i in range(2):
        while True:
            a.append(float(params[cursor].strip().removeprefix("[").rstrip(")").rstrip("]")))
            if "]" in params[cursor]:
                break
            cursor += 1
        a = y
        cursor += 1
    return Numeric(x, y, 0, 0, 0, 0, 0, len(x))

'''
PROPOSED ARCHITECTURE:
Operations:
(Inputs)    (Outputs)
Numeric ---> Numeric  (ex. scaling, corr. adj.)
Numeric ---> File     (ex. generation, organization/labelling)
File    ---> Numeric  (ex. data retrieval, statistics)
File    ---> File     (ex. file conversion, renaming)
'''

# GENERAL MODULE: INPUT
op_str = input("Enter all operations delineated with vert bar: ")
ops = op_str.split("|")
cache = None

for op in ops:
    comps = op.split("->")
    if "Numeric" in comps[0] and "Numeric" in comps[1]:
        f_params = []
        out_numeric = None
        in_numeric = None
        in_params = comps[0].split("(")[1].split(",")
        out_params = comps[1].split("(")[1].split(",")
        cursor = 0
        if "prev" in in_params[0] and cache is not None:
            in_numeric = cache
        else:
            in_numeric = parseInputNumeric(in_params)

        for i in range(len(out_params)):
            f_params.append(float(out_params[i].strip().rstrip(")")))

        if in_numeric.x == [""] and in_numeric.y == [""]:
            out_numeric = numeric_numeric(in_numeric,
                                          x_min=f_params[0],
                                          x_max=f_params[1],
                                          y_min=f_params[2],
                                          y_max=f_params[3],
                                          r=f_params[4],
                                          n=f_params[5])
        else:
            out_numeric = numeric_numeric(in_numeric,
                                          y_min=f_params[0],
                                          y_max=f_params[1],
                                          r=f_params[2],
                                          n=len(in_numeric.x),
                                          s=f_params[3])
        cache = out_numeric
        cache.toString()

    elif "Numeric" in comps[0] and "File" in comps[1]:
        f_params = []
        out_file = None
        in_numeric = None
        in_params = comps[0].split("(")[1].split(",")
        out_params = comps[1].split("(")[1].rstrip(")")
        cursor = 0
        if "prev" in in_params[0] and cache is not None:
            in_numeric = cache
        else:
            in_numeric = parseInputNumeric(in_params)
        out_file = numeric_file(in_numeric, f_params[0])
        cache = out_file

    elif "File" in comps[0] and "Numeric" in comps[1]:
        out_numeric = None
        params = comps[0].split("(")[1].split(",")
        in_name = params[0]
        if params[0] == "prev" and cache is not None:
            in_name = os.path.basename(cache.name)
        out_numeric = file_numeric(in_name, eval(params[1].rstrip().rstrip(")")))
        cache = out_numeric

    elif "File" in comps[0] and "File" in comps[1]:
        in_name = comps[0].split("(")[1].strip().rstrip(")")
        out_name = comps[1].split("(")[1].strip().rstrip(")")
        out_file = None
        if in_name[0] == "prev" and cache is not None:
            in_name = os.path.basename(cache.name)
        out_file = file_file(in_name, out_name)
        cache = out_file

    else:
        print("Invalid Input, try again")
        break
