#!/usr/bin/python3
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
    # print(genome_seq)
    # print(set(genome_seq))
    # print(len(set(genome_seq)))
    for seq in sequences:
        pattern = rf'(?=({seq}))'
        matches = re.finditer(pattern, genome_seq)
        m_count = 0

        for m in matches:
            m_count+=1
            
        print(f'{seq}: {m_count}')



if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Parse the genome file path and the patterns to search")
    parser.add_argument('--sequence', type=str, nargs="+", required=True, help="One or multiple sequences to search")
    parser.add_argument('--genome', type=str, required=True, help="Path to genome file")
    args = parser.parse_args()

    main(args.genome, args.sequence)
