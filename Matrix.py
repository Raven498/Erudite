import random


# File(baoisdumb.txt, False) -> Numeric | Numeric(prev) -> Numeric (0, 100, 0.7, 1) | Numeric(prev) -> File(baoissmart.txt)

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


def numeric_numeric(numeric, s, **kwargs):
    out_x = []
    out_y = []
    x_min = kwargs.get("x_min")
    x_max = kwargs.get("x_max")
    y_min = kwargs.get("y_min")
    y_max = kwargs.get("y_max")
    r = kwargs.get("r")
    n = kwargs.get("n")
    if numeric.x == [] and numeric.y == []:
        for i in range(n):
            out_x.append(random.randint(x_min, x_max))
            out_y.append((out_x[i] * r) + (
                    random.randint(y_min, y_max) * (1 - (r ** 2)) ** (1 / 2)))
        return Numeric(out_x, out_y, x_min, x_max, y_min, y_max, r, n)
    else:
        for i in range(n):
            out_x.append(numeric.x[i] * s)
            out_y.append((out_x[i] * r) + (
                    random.randint(y_min, y_max) * (1 - (r ** 2)) ** (1 / 2)))
        return Numeric(out_x, out_y, numeric.x_min, numeric.x_max, y_min, y_max, r, n)

def numeric_file(x, y, name):
    with open(name, "w") as f:
        f.write("[X]:\n")
        for num in x:
            f.write(str(num) + "\n")
        f.write("[Y]:\n")
        for num in y:
            f.write(str(num) + "\n")
        return f


def file_numeric(name, s_mode):
    x = []
    y = []
    r = 0
    with open(name, "r") as f:
        if s_mode:  # STATS
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
                    x.append(int(l))
                if y_mode:
                    y.append(int(l))
        return Numeric(x, y, 0, 0, 0, 0, 0, len(x))


def file_file(name1, name2):
    pass


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
        # Numeric([5, 3, 3], [4, 5, 6], 0, 100, 0, 100, 0.5, 1000, 1)
        # Numeric(prev, 0, 100, 0.7, 1) -> Numeric --> FIRST TEST CASE
        # Numeric(0, 100, 0, 100, 0.3, 1000, 1) --> LAST TEST CASE
        # input numeric: x (yes), y (no), x_min (no), x_max (no), y_min (yes), y_max (yes), r (no), n (yes):
        # FINAL: Numeric(prev OR [5, 3, 3], [4, 5, 6], ...) -> Numeric(0, 100, 0.7, 1 OR 0, 100, 0.5, 1)
        f_params = []
        out_numeric = None
        in_numeric = None
        in_params = comps[0].split("(")[1].split(",")
        out_params = comps[1].split("(")[1].split(",")
        cursor = 0
        if "prev" in in_params[0] and cache is not None:
            for i in range(len(out_params)):
                f_params.append(float(out_params[i].strip().rstrip(")")))
            in_numeric = cache
            out_numeric = numeric_numeric(in_numeric, f_params[3],
                                          y_min=f_params[0],
                                          y_max=f_params[1],
                                          r=f_params[2],
                                          n=in_numeric.n)
        else:
            # X/Y PARSE
            x = []
            y = []
            a = x
            print(in_params)
            for i in range(2):
                while True:
                    a.append(in_params[cursor].strip().removeprefix("[").rstrip("]").rstrip(")"))
                    if "]" in in_params[cursor]:
                        break
                    cursor += 1
                a = y
                cursor += 1
            for i in range(len(out_params)):
                f_params.append(int(out_params[i].strip().rstrip(")")))
            in_numeric = Numeric(x, y, 0, 0, 0, 0, 0, len(x))
            out_numeric = numeric_numeric(in_numeric, f_params[3],
                                          y_min=f_params[0],
                                          y_max=f_params[1],
                                          r=f_params[2],
                                          n=in_numeric.n)
        cache = out_numeric
        cache.toString()

    elif "Numeric" in comps[0] and "File" in comps[1]:
        #Numeric(prev) --> File(baoissmart.txt)
        #Numeric(prev OR [5, 3, 3], [4, 5, 6])
        f_params = []
        out_file = None
        in_numeric = None
        in_params = comps[0].split("(")[1].split(",")
        out_params = comps[1].split("(")[1].rstrip(")")
        print(out_params)
        cursor = 0
        if "prev" in in_params[0] and cache is not None:
            in_numeric = cache
            out_file = numeric_file(cache.x, cache.y, out_params)
        else:
            # X/Y PARSE
            x = []
            y = []
            a = x
            print(in_params)
            for i in range(2):
                while True:
                    a.append(in_params[cursor].strip().removeprefix("[").rstrip("]").rstrip(")"))
                    if "]" in in_params[cursor]:
                        break
                    cursor += 1
                a = y
                cursor += 1
            for i in range(len(out_params)):
                f_params.append(int(out_params[i].strip().rstrip(")")))
            in_numeric = Numeric(x, y, 0, 0, 0, 0, 0, len(x))
            out_file = numeric_file(in_numeric.x, in_numeric.y, out_params[0])
        cache = out_file

    elif "File" in comps[0] and "Numeric" in comps[1]:
        out_file = None
        params = comps[0].split("(")[1].split(",")
        if params[0] == "prev" and cache is not None:
            pass
        else:
            # File(baoisdumb.txt, False)
            out_file = file_numeric(params[0], eval(params[1].rstrip().rstrip(")")))
        cache = out_file

    elif "File" in comps[0] and "File" in comps[1]:
        pass
    else:
        print("Invalid Input, try again")
        break

    # MODULE 1: PRL
    # Numeric --> Numeric

    '''
    3 OBJs:
    1. Generation (w/ or w/o corr adjustment)
    2. Corr adjustment (needs full list)
    3. Scaling (needs full list)
    '''
