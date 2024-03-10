import gzip
import multiprocessing
import tqdm

NUM_PROCESSES = 10


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


def calculate_ncd(data):
    id, seq, seq_dict = data
    return id, [ncd(seq, seq_dict[id_2]) for id_2 in seq_dict if id_2 != id]


import tqdm

if __name__ == "__main__":
    seq_dict = read_seq_data("seclib_file_7k.db")

    score_dict = {}

    # calculate scores for each seq against all other seqs
    with multiprocessing.Pool(NUM_PROCESSES) as pool:
        data = [(id, seq, seq_dict) for id, seq in seq_dict.items()]

        # Initialize tqdm progress bar
        progress_bar = tqdm.tqdm(total=len(data), desc="Calculating scores")

        results = []
        for result in pool.imap_unordered(calculate_ncd, data):
            results.append(result)
            # Update progress bar
            progress_bar.update(1)

        # Close the progress bar
        progress_bar.close()

        for id, scores in results:
            score_dict[id] = scores

    # compute sum of scores for each ID
    sum_scores = {id: sum(scores) for id, scores in score_dict.items()}

    # normalize
    min_score = min(sum_scores.values())
    max_score = max(sum_scores.values())

    norm_scores_dict = {}
    for id, score in sum_scores.items():
        norm = (score - min_score) / (max_score - min_score)
        norm_scores_dict[id] = norm

    for id, norm in norm_scores_dict.items():
        print(id, norm)

    with open("seq_similarity.txt", "w") as f:
        for id, norm in norm_scores_dict.items():
            f.write(f"{id} {norm}\n")

        f.write(f"\nMax value: {max_score}\n")
        f.write(f"Normalize-trust-score: {1 - (min_score / max_score)}\n")
        f.write(f"Seqs looked at: {len(seq_dict.keys())}\n")



    

