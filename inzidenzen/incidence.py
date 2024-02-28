import argparse
import re


def read_genome(path):
    with open(path) as genome:
        genome_seq = ""
        genome_lines = genome.readlines()
        for line in genome_lines:
            if line[0] != ">" and line != "":
                genome_seq+=line.strip("\n")
    return genome_seq
                

def main(path_to_fasta: str, sequences: list):
    genome_seq = read_genome(path_to_fasta)
    for seq in sequences:
        print(seq)
    # pattern = r'(?=())'
    # match = re.search(pattern, text)


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Parse the genome file path and the patterns to search")
    parser.add_argument('--sequence', type=str, nargs="+", required=True, help="One or multiple sequences to search")
    parser.add_argument('--genome', type=str, required=True, help="Path to genome file")
    args = parser.parse_args()

    main(args.genome, args.sequence)
