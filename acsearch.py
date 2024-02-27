#!/usr/bin/python3
# Programmierpraktikum WS2023/2024
# 240226
# Uebungsblatt 1, Aufgabe 5 (Swissprot Search)
# Julius J. Stein

import argparse
import sys
import requests

# function to fetch the sequence from the uniprot website
def fetch_swissprot_sequence(ac_num):
    try:
        # http get request to the uniprot website
        response = requests.get(f"https://www.uniprot.org/uniprot/{ac_num}.fasta")
        # status code 200: standard response for successful HTTP requests
        if response.status_code == 200:
            fasta_data = response.text
            return fasta_data
        # unsuccessful request
        else:
            return ""  # Return empty string if the accession number doesn't exist
    except Exception as e:
        return ""  # Return empty string if there's any exception

def main():
    # argparser to parse the accession number
    parser = argparse.ArgumentParser(description='Fetch sequence from UniProt.')
    # add the accession number as an argument
    parser.add_argument('--ac', metavar='ACCESSION_NUMBER', required=True,
                        help='Accession number of the UniProt sequence to fetch')
    # parse the arguments
    args = parser.parse_args()

    fasta_sequence = fetch_swissprot_sequence(args.ac)
    # print the fasta sequence
    print(fasta_sequence)

if __name__ == "__main__":
    main()
