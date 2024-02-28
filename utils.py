
import pandas as pd
def create_dataframe_from_csv(file_path):
    with open(file_path, 'r') as f:
        ls = f.readlines()
        h = ls[0].strip().split(',')
        rs = [l.strip().split(',') for l in ls[1:]]
        df_bin = {c: [] for c in h}
        for r in rs:
            for c, v in zip(h, r):
                df_bin[c].append(v)
    return pd.DataFrame(df_bin)

def read_lines(file):
    lines = []
    with open(file, 'r') as file:
        for line in file:
            lines.append(line.strip('\n'))
    return lines


def dict_to_tsv(dictionary, output):
    with open(output, 'w') as file:
        for key, value in dictionary.items():
            file.write(f"{key}\t{value}\n")

def strip_exp_factor(exp_factor, tgt_exp_factor_type):
    print(exp_factor)   
    exp_factor = (exp_factor.split(' ')[0]).split('=')
    exp_factor_type = exp_factor[0]
    exp_factor_value = exp_factor[1]
    if exp_factor_type.lower() == tgt_exp_factor_type.lower():
        return exp_factor_value
    else:
        return None