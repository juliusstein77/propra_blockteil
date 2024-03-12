import gzip
import multiprocessing
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns
import argparse

NUM_PROCESSES = 8


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
    detailed_seq_dict = {}
    with open(path_to_data) as f:
        lines = f.readlines()

    for i in range(len(lines)):
        curr_line = lines[i]
        if curr_line[0] == ">":
            s_id = lines[i][2:].strip("\n")
            s_aa = lines[i+1][3:].strip("\n")
            s_ss = lines[i+2][3:].strip("\n")
            seq_dict[s_id] = s_aa + s_ss
            detailed_seq_dict[s_id] = (s_aa, s_ss)

    return seq_dict, detailed_seq_dict


def calculate_ncd(data):
    id, seq, seq_dict = data
    return id, [ncd(seq, seq_dict[id_2]) for id_2 in seq_dict]  # get all scores for each seq


def filter_similar_sequences(score_matrix, sequence_ids, threshold):
    similar_sequences = set()

    # Find pairs of sequences with similarity above the threshold
    for i in range(len(sequence_ids)):
        for j in range(i + 1, len(sequence_ids)):
            if score_matrix[i, j] < threshold:
                similar_sequences.add(sequence_ids[i])
                similar_sequences.add(sequence_ids[j])

    # Return the IDs of sequences to remove
    return similar_sequences

def remove_similar_sequences(score_matrix, similar_sequences):
    # get the index of the similar sequences
    similar_sequences = [list(seq_dict.keys()).index(seq_id) for seq_id in similar_sequences]

    # now we removed the ids from the dict, we need to remove the rows and columns from the score matrix
    score_matrix = np.delete(score_matrix, similar_sequences, axis=0)
    score_matrix = np.delete(score_matrix, similar_sequences, axis=1)

    return score_matrix


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument('-d', type=str, required=True)
    parser.add_argument('-t', type=float, default=0.5)
    args = parser.parse_args()
    

    data_path = args.d
    threshold = args.t
    plot_name = ""

    if "/" in data_path:
        plot_name = data_path.split(".")[-2].split("/")[-1]
    else:
        plot_name = data_path.split(".")[-2]

    # seq_dict = read_seq_data("/home/malte/projects/blockgruppe3/cb513.db")
    seq_dict, detailed_seq_dict = read_seq_data(data_path)

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

    cg = sns.clustermap(score_matrix, method='average', figsize=(12, 8), annot=False)
    cg.ax_row_dendrogram.set_visible(False)
    cg.ax_col_dendrogram.set_visible(False)
    cg.ax_heatmap.set_xticklabels([])
    cg.ax_heatmap.set_yticklabels([])


    cg.fig.suptitle(f'Heatmap of NCD scores for each sequence in {plot_name}', fontsize=20)
    cg.cax.set_position([0.1, 0.1, 0.02, 0.6])  # Adjust position [left, bottom, width, height]
    plt.savefig(f'{plot_name}.png')
    

    similar_sequences = filter_similar_sequences(score_matrix, list(seq_dict.keys()), threshold)
    
    score_matrix = remove_similar_sequences(score_matrix, similar_sequences)

    print(f"Number of sequences found in {plot_name}: {len(seq_dict)}")
    print(f"Removing {len(similar_sequences)} sequences from {plot_name} having a similarity score of < {threshold}")
    print(f"Remaining sequences: {len(seq_dict) - len(similar_sequences)}")

    # create new heatmap
    cg = sns.clustermap(score_matrix, method='average', figsize=(12, 8), annot=False)
    cg.ax_row_dendrogram.set_visible(False)
    cg.ax_col_dendrogram.set_visible(False)
    cg.ax_heatmap.set_xticklabels([])
    cg.ax_heatmap.set_yticklabels([])
    # increase fint size of labels on legend
    cg.cax.yaxis.set_tick_params(labelsize=10)

    cg.fig.suptitle(f'Heatmap of NCD scores for each sequence in {plot_name} with {threshold} as threshold', fontsize=20)
    cg.cax.set_position([0.1, 0.1, 0.02, 0.6])  # Adjust position [left, bottom, width, height]
    plt.savefig(f'{plot_name}_filtered.png')


    # now save all ids that are kept in the filtered dict in a file
    with open(f"filtered_sequences_{plot_name}.db", "w") as f:
        for id in seq_dict.keys():
            if id not in similar_sequences:
                f.write("> " + id + "\n")
                f.write("AS " + detailed_seq_dict[id][0] + "\n")
                f.write("SS " + detailed_seq_dict[id][1] + "\n")






