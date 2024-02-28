#!/usr/bin/python3
import argparse
import re
from collections import defaultdict


def load_sequence_from_fasta(fasta_file):
    """
    read in the genome fasta
    """
    sequences = defaultdict(str)
    current_id = ''
    with open(fasta_file, 'r') as file:
        for line in file:
            if line.startswith('>'):  # Header line
                current_id = line[1:].rstrip()
            else:  # Sequence line
                sequences[current_id] += line.strip()
    return sequences


def main(path_to_fasta: str, sequences: list):
    genome_seq = load_sequence_from_fasta(path_to_fasta)
    print("Tatsächliches vorkommen der seqs")
    seq_cont_dict = defaultdict(int)
    for fasta_id, fasta_seq in genome_seq.items():
        print(fasta_id)
        for seq in sequences:
            pattern = rf'(?=({seq}))'
            matches = re.finditer(pattern, fasta_seq)
            for _ in matches:
                seq_cont_dict[seq]+=1

                
    for seq, count in seq_cont_dict.items():
        print(seq, count)
    
    ###################################################
    # CALCULATIONS                                    #
    ###################################################
    # base_content = {}
    # print("Relative Häufigkeit der basen")
    # for fasta_id, fasta_seq in genome_seq.items():
    #     for base in ['A', 'C', 'G', 'T']:
    #         m_count=0
    #         pattern = rf'{base}'
    #         matches = re.finditer(pattern, fasta_seq)
    #         for _ in matches:
    #             m_count+=1
    #         base_content[base] = m_count/len(genome_seq)*100
    #     # base_content[base] = f'{m_count/len(genome_seq)*100, 2}%'
    #     # print(f'{base}: {m_count/len(genome_seq)*100}')
    #     # base_content[base] = m_count/len(genome_seq)
    #
    # relative_prob = {}
    # print("1/4**len(seq)*len(genome)")
    # for seq in sequences:
    #     relative_prob[seq] = 0.25**len(seq)*len(genome_seq)
    #     print(seq, 0.25**len(seq)*len(genome_seq))
    #
    # print("ac_prob")
    # for seq in sequences:
    #     prob = 1
    #     for base in seq:
    #         prob*=base_content[base]
    #     print(prob*len(genome_seq))

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Parse the genome file path and the patterns to search")
    parser.add_argument('--sequence', type=str, nargs="+", required=True, help="One or multiple sequences to search")
    parser.add_argument('--genome', type=str, required=True, help="Path to genome file")
    args = parser.parse_args()

    main(args.genome, args.sequence)
