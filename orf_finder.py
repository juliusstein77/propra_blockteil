#!/usr/bin/python3
# Programmierpraktikum WS2023/2024
# 240229
# Uebungsblatt 2, Aufgabe 5 (Finding ORFs)
# Julius J. Stein

import argparse
import re

# Reverse complement of a DNA sequence
def reverse_complement(seq):
    complement = {'A': 'T', 'C': 'G', 'G': 'C', 'T': 'A'}
    return ''.join(complement[base] for base in reversed(seq))

# Find all open reading frames in a DNA sequence
def find_orfs(sequence):
    orfs = []
    start_codon = 'ATG'
    stop_codons = ['TAA', 'TAG', 'TGA']
    seq_len = len(sequence)

    # Iterate over all three reading frames
    for frame in range(3):
        # boolean to keep track of whether a start codon has been found
        found_start = False
        # Iterate over the sequence in steps of 3
        for i in range(frame, seq_len, 3):
            # If a start codon is found and no start codon has been found before, set found_start to True and store the start index
            if sequence[i:i + 3] == start_codon and not found_start:
                found_start = True
                start_index = i
            # If a stop codon is found and a start codon has been found before, store the stop index and add the ORF to the list
            if sequence[i:i + 3] in stop_codons and found_start:
                stop_index = i
                # Check if the start index lies within the boundaries of an existing ORF
                if not any(start_index > start and start_index < stop and ((start_index-start)%3 == 0) for start, stop in orfs):
                        orfs.append((start_index, stop_index + 3))
                found_start = False

    return [sequence[start:stop] for start, stop in orfs]

# Extract sequences from a FASTA file
def extract_sequences_from_fasta(fasta_file):
    # Dictionary to store the sequences and their names
    sequences = {}
    with open(fasta_file, 'r') as f:
        current_sequence = None
        for line in f:
            if line.startswith('>'):
                current_sequence = line.strip()[1:]
                sequences[current_sequence] = ''
            else:
                sequences[current_sequence] += line.strip()
    return sequences

# Write sequences to a FASTA file
def write_to_fasta(sequences, output_file):
    with open(output_file, 'w') as f:
        for seq_name, seq in sequences.items():
            f.write(f'>{seq_name}\n{seq}\n')

def main():
    parser = argparse.ArgumentParser(description="Find ORFs in given FASTA sequences")
    parser.add_argument("--fasta", required=True, help="Input FASTA file containing sequences")
    parser.add_argument("--output", help="Output file to write the results in FASTA format")
    args = parser.parse_args()

    sequences = extract_sequences_from_fasta(args.fasta)
    output_file = args.output

    output_sequences = {}

    # Iterate over all sequences and find ORFs
    for seq_name, seq in sequences.items():
        # Find ORFs in the sequence and its reverse complement
        orfs = find_orfs(seq)
        orfs_reverse = find_orfs(reverse_complement(seq))

        # Store the ORFs in a list
        all_orfs = []
        for orf in orfs:
            all_orfs.append(orf)
        for orf in orfs_reverse:
            all_orfs.append(orf)

        for i, orf in enumerate(all_orfs):
            output_sequences[f'{seq_name}_{i}'] = orf

    if args.output:
        write_to_fasta(output_sequences, f'{output_file}/orfs.fasta')
    else:
        for seq_name, seq in output_sequences.items():
            print(f'>{seq_name}\n{seq}')


if __name__ == "__main__":
    main()

