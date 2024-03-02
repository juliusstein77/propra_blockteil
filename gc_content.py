#!/usr/bin/python3
# Programmierpraktikum WS2023/2024
# 240229
# Uebungsblatt 2, Aufgabe 5 (Finding ORFs)
# Julius J. Stein

import sys

# Calculate the GC content of a DNA string
def calculate_gc_content(dna_string):
    total_bases = len(dna_string)
    gc_bases = dna_string.count('G') + dna_string.count('C')
    gc_content = (gc_bases / total_bases) * 100
    return gc_content

# Parse a FASTA file and return a list of tuples containing the label and the DNA sequence
def parse_fasta_file(file_path):
    strings = []
    current_label = ""
    current_sequence = ""

    with open(file_path, 'r') as file:
        for line in file:
            line = line.strip()
            if line.startswith(">"):
                if current_label != "" and current_sequence != "":
                    strings.append((current_label, current_sequence))
                current_label = line[1:]
                current_sequence = ""
            else:
                current_sequence += line

    if current_label != "" and current_sequence != "":
        strings.append((current_label, current_sequence))

    return strings

# Find the DNA string with the highest GC content in a FASTA file
def find_highest_gc_content(file_path):
    strings = parse_fasta_file(file_path)
    highest_gc_content = 0
    highest_gc_string = ""

    for label, dna_string in strings:
        gc_content = calculate_gc_content(dna_string)
        if gc_content > highest_gc_content:
            highest_gc_content = gc_content
            highest_gc_string = label

    return highest_gc_string, highest_gc_content

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python3 gc_content.py <input_fasta_file>")
        sys.exit(1)

    input_fasta_file = sys.argv[1]
    result = find_highest_gc_content(input_fasta_file)

    print("Label with the highest GC content:", result[0])
    print("GC content:", result[1])
