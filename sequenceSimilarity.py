import gzip
import multiprocessing
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns
from scipy.cluster.hierarchy import dendrogram, linkage

NUM_PROCESSES = 16


def calculate_ncd_row(data_row, seq_dict):
    i, seq = data_row
    row = [ncd(seq, seq_dict[seq_id]) for seq_id in seq_dict]
    return i, row


def ncd(x:str, x2:str) -> float:
    """
    A method which takes in two seqs and calculates the normalized compression distance (ncd)
    :param x: Sequence 1
    :param x2: Sequence 2
    :return: normalized compression dist
    """
    x_compressed = len(gzip.compress(x.encode())) 
    x2_compressed = len(gzip.compress(x2.encode()))
    xx2 = len(gzip.compress(("".join([x,x2])).encode()))

    return (xx2 - min(x_compressed, x2_compressed)) / max(x_compressed, x2_compressed)  


def read_seq_data(path_to_data):
    lines = []
    seq_dict = {}
    with open(path_to_data) as f:
        lines = f.readlines()

    for i in range(len(lines)):
        curr_line = lines[i]
        if curr_line[0] == ">":
            s_id = lines[i][2:].strip("\n")
            s_aa = lines[i+1][3:].strip("\n")
            s_ss = lines[i+2][3:].strip("\n")
            seq_dict[s_id] = s_aa + s_ss

    return seq_dict


# def calculate_ncd(data):
#     id, seq, seq_dict = data
#     return id, [ncd(seq, seq_dict[id_2]) for id_2 in seq_dict if id_2 ]

def calculate_ncd(data):
    id, seq, seq_dict = data
    return id, [ncd(seq, seq_dict[id_2]) for id_2 in seq_dict]  # get all scores for each seq


if __name__ == "__main__":
    seq_dict = read_seq_data("/home/malte/projects/blockgruppe3/cb513.db")
    # seq_dict = read_seq_data("/home/malte/temp/GOR_JARS/vali/validation/filtered_seclib_7k.db")

    score_dict = {}

    # calculate scores for each seq against all other seqs
    with multiprocessing.Pool(NUM_PROCESSES) as pool:
        data = [(id, seq, seq_dict) for id, seq in seq_dict.items()]
        results = pool.map(calculate_ncd, data)
        
        for id, scores in results:
            score_dict[id] = scores

    # create score matrix
    # clutser based on score matrix
    score_matrix = np.zeros((len(seq_dict.keys()), len(seq_dict.keys())))

    # fill matrix
    for i, id in enumerate(seq_dict.keys()):
        score_matrix[i] = score_dict[id]

    # sns.clustermap(score_matrix, method='average', figsize=(14, 10), row_cluster=True, col_cluster=True)

    cg = sns.clustermap(score_matrix, method='average', figsize=(14, 10))
    cg.ax_row_dendrogram.set_visible(False)
    cg.ax_col_dendrogram.set_visible(False)

    plt.title('Heatmap of NCD\nscores for each\nsequence in CB513')
    plt.xlabel('Sequence Index', fontsize=12)
    plt.ylabel('Sequence Index', fontsize=12)
    cg.cax.set_position([0.1, 0.1, 0.02, 0.6])  # Adjust position [left, bottom, width, height]
    plt.savefig('CB513_heatmap.png')
    plt.show()
