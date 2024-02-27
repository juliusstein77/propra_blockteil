#!/usr/bin/python3
# Programmierpraktikum WS2023/2024
# 240226
# Uebungsblatt 1, Aufgabe 7 (Prosite-Pattern)
# Julius J. Stein

import re
import argparse
import sys
import requests

def prosite_to_regex(prosite_pattern):
    # Convert Prosite pattern to a regular expression pattern
    regex_pattern = prosite_pattern.replace('[', '[').replace(']',']').replace('{', '[^').replace('}', ']').replace('(', '{').replace(')', '}').replace('<', '').replace('>', '')

    # Replace individual characters with regular expression equivalents
    regex_pattern = regex_pattern.replace('x', '.').replace('-', '').replace('*', '.*')
    regex_pattern = '(?=(' + regex_pattern + '))'
    #print(regex_pattern)
    return regex_pattern

def find_pattern_matches(pattern, sequence, sequence_id):
    # Convert Prosite pattern to a regular expression pattern
    regex_pattern = prosite_to_regex(pattern)

    # Find matches in the sequence using regular expressions
    matches = re.finditer(regex_pattern, sequence)
    results = []
    for match in matches:
        matched_sequence = match.group(1)
        start_position = match.start() + 1
        results.append((sequence_id, start_position, matched_sequence))
    return results

def load_sequence_from_fasta(fasta_file):
    sequences = {}
    current_id = ''
    source = sys.stdin if fasta_file == "-" else open(fasta_file, 'r')

    for line in source:
        line = line.strip()
        if line.startswith('>'):
            current_id = line.strip()[1:]
            sequences[current_id] = ''
        else:
            sequences[current_id] += line.strip()

    if fasta_file != "-":
        source.close()

    return sequences


def load_prosite_pattern(prosite_id):
    try:
        # HTTP GET request to retrieve the Prosite pattern from the Prosite website
        # add .txt to get website in text format
        response = requests.get(f"https://prosite.expasy.org/{prosite_id}.txt")
        #print(response.text)
        # Check if the request was successful
        if response.status_code == 200:
            # Extract the Prosite pattern from the text file
            pattern_match = re.search(r'-Consensus pattern:\s*(\[.*?\].*?)(?=\n-)', response.text, re.DOTALL)
            if pattern_match:
                prosite_pattern = pattern_match.group(1)
                prosite_pattern = re.sub(r'\s+', '', prosite_pattern)
                #print(prosite_pattern.strip())
                return prosite_pattern.strip()
            else:
                # Check for pattern format after "PA" at the beginning of a line followed by a tab
                for line in response.text.split('\n'):
                    if line.startswith('PA'):  # Check if the line starts with "PA" followed by a tab
                        # Extract the pattern
                        prosite_pattern = line.split(' ', 1)[1].strip().rstrip('.')
                        #print(prosite_pattern)
                        return prosite_pattern
                else:
                    print(f"No Prosite pattern found in the response for{prosite_id}.")
                    return None
        else:
            print(f"Error retrieving Prosite pattern {prosite_id}. Status code: {response.status_code}")
            return None
    except Exception as e:
        print(f"Error retrieving Prosite pattern {prosite_id}: {e}")
        return None

def main():
    # Argument parser to parse command line arguments
    parser = argparse.ArgumentParser(description='Find occurrences of a Prosite pattern in sequences from a FASTA file.')
    # Add arguments for Prosite pattern and FASTA file

    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument('--pattern', metavar='pattern', type=str, help='Prosite pattern to search for')
    group.add_argument('--web', metavar='prosite_id', type=str, help='PROSITE ID to load the pattern from the Prosite website')
    parser.add_argument('--extern', action='store_true', help='Scan von der Prosite Webseite durchführen lassen (Bonus)')

    #parser.add_argument('--pattern', metavar='pattern', type=str, required=True, help='Prosite pattern to search for')
    parser.add_argument('--fasta', metavar='fasta_file', type=str, required=True, help='FASTA file containing the sequences')
    #parser.add_argument('--web', metavar='prosite_id', type=str, help='PROSITE ID to load the pattern from the Prosite website')
    args = parser.parse_args()

    pattern = args.pattern

    #print(pattern)
    fasta_file = args.fasta

    if args.web:
        # If the --web option is specified, load the Prosite pattern from the Prosite website
        pattern = load_prosite_pattern(args.web)
        if not pattern:
            return

    # Load sequences from the FASTA file
    sequences = load_sequence_from_fasta(fasta_file)

    # Find matches for the pattern in each sequence
    all_matches = []
    for sequence_id, sequence in sequences.items():
        matches = find_pattern_matches(pattern, sequence, sequence_id)
        all_matches.extend(matches)

    # Print the found matches
    for match in all_matches:
        print(f"{match[0]}\t{match[1]}\t{match[2]}")


if __name__ == "__main__":
    main()
