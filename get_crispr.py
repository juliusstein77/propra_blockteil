#!/usr/bin/python3
import argparse

CRISPR_LENGTH = 23
MOTIF = 'GG'


def read_crispr_out_of_fasta(fasta_file):
    with open(fasta_file, 'r') as f:
        record_name = ''
        seq = ''
        crispr_seqs = []
        for line in f:
            if line.startswith('>'):
                if seq:  # Skip the get_crispr for first line
                    crispr_seqs.extend(get_crispr(seq, record_name))
                    seq = ''
                record_name = line.strip()
            else:
                seq += line.strip()
            if seq:  # Check for the last sequence
                crispr_seqs.extend(get_crispr(seq, record_name))
        return crispr_seqs


def get_crispr(seq, record_name):
    crispr_seqs = []
    n = CRISPR_LENGTH - len(MOTIF)
    for i in range(len(seq) - len(MOTIF) - n + 1):
        if seq[i: i + n] + MOTIF == seq[i: i + n + len(MOTIF)]:
            start = i + n + 1
            crispr_seqs.append((record_name, start, seq[i: i + n + len(MOTIF)]))
    return crispr_seqs


def main():
    parser = argparse.ArgumentParser(description='Find CRISPR/Cas recognition sequences in a FASTA file.')
    parser.add_argument('--fasta', dest='fasta_file', help='Path to FASTA file', required=True)
    args = parser.parse_args()

    crispr_seqs = read_crispr_out_of_fasta(args.fasta_file)

    for record_name, start, crispr_seq in crispr_seqs:
        print("{}\t{}".format(record_name, start))
        print(crispr_seq)


if __name__ == '__main__':
    main()
