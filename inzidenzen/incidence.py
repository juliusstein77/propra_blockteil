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
    print("Tatsächliches vorkommen der seqs")
    for seq in sequences:
        pattern = rf'(?=({seq}))'
        matches = re.finditer(pattern, genome_seq)
        m_count = 0
        for _ in matches:
            m_count+=1
            
        print(f'{seq}: {m_count}')
    ###################################################
    # CALCULATIONS                                    #
    ###################################################
    base_content = {}
    for base in ['A', 'C', 'G', 'T']:
        m_count=0
        pattern = rf'{base}'
        matches = re.finditer(pattern, genome_seq)
        for _ in matches:
            m_count+=1
            
        print("Relative Häufigkeit der basen")
        # base_content[base] = f'{m_count/len(genome_seq)*100, 2}%'
        print(f'{base}: {m_count/len(genome_seq)*100}')
        base_content[base] = m_count/len(genome_seq)

    relative_prob = {} 
    print("1/4**len(seq)*len(genome)")
    for seq in sequences:
        relative_prob[seq] = 0.25**len(seq)*len(genome_seq)
        print(seq, 0.25**len(seq)*len(genome_seq))
    
    print("ac_prob")
    for seq in sequences:
        prob = 1
        for base in seq:
            prob*=base_content[base]
        print(prob*len(genome_seq))


            
            
   

        





if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Parse the genome file path and the patterns to search")
    parser.add_argument('--sequence', type=str, nargs="+", required=True, help="One or multiple sequences to search")
    parser.add_argument('--genome', type=str, required=True, help="Path to genome file")
    args = parser.parse_args()

    main(args.genome, args.sequence)
