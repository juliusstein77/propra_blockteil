import os
from itertools import combinations

def process_ali_file(file_path):
    sequences = {}
    with open(file_path, 'r') as file:
        current_id = None
        current_sequence = ""
        for line in file:
            if line.startswith("C"):
                continue
            if line.startswith(">"):
                if current_id:
                    sequences[current_id] = current_sequence.strip()
                    current_sequence = ""
                current_id = line.split(";")[1].strip()
            elif line.startswith("structureX:"):
                continue
            else:
                current_sequence += line.strip().rstrip("*").replace("/", "")

    sequences[current_id] = current_sequence.strip()

    keys = list(sequences.keys())
    permutations = list(combinations(keys, 2))

    return permutations, sequences

def main():
    homstrad_folder_path = "/Users/juliusstein/Desktop/Studium/lmu_tum_bioinfo/ws2324/ProPra_ws2324/blockteil/ueb3/HOMSTRAD"
    output_file1 = "vali.pairs"
    output_file2 = "vali.seqlib"

    with open(output_file1, 'w') as pairings_file, open(output_file2, 'w') as sequences_file:
        for root, _, files in os.walk(homstrad_folder_path):
            for file in files:
                if file.endswith(".ali"):
                    file_path = os.path.join(root, file)
                    print(f"Processing {file_path}")
                    pairings, sequences = process_ali_file(file_path)
                    for pairing in pairings:
                        pairings_line = ' '.join(pairing) + '\n'
                        pairings_file.write(pairings_line)

                    for structure, sequence in sequences.items():
                        sequences_line = f"{structure}:{sequence}\n"
                        sequences_file.write(sequences_line)

if __name__ == "__main__":
    main()
