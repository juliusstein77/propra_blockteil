#!/usr/bin/python3
# Programmierpraktikum WS2023/2024
# 240229
# Uebungsblatt 2, Aufgabe 8 (Get PDB)
# Julius J. Stein

import argparse
import requests

# download_pdb: Download PDB file for given PDB ID
def download_pdb(pdb_id, fasta):
    if fasta:
        url = f'https://www.rcsb.org/fasta/entry/{pdb_id}'
    else:
        url = f'https://files.rcsb.org/download/{pdb_id}.pdb'
    response = requests.get(url)
    if response.status_code == 200:
        return response.text
    else:
        print(f"Failed to download PDB file for ID {pdb_id}.")
        return None

# save_to_file: Save content to file
def save_to_file(content, output_file):
    with open(output_file, 'w') as file:
        file.write(content)
    return output_file

def print_to_console(content):
    print(content)

def main():
    parser = argparse.ArgumentParser(description='Download PDB file and optionally convert to FASTA format.')
    parser.add_argument('--id', metavar='pdb_id', type=str, required=True, help='PDB ID to download')
    parser.add_argument('--output', metavar='output_file', type=str, help='Output file path. If not specified, print to console.')
    parser.add_argument('--fasta', action='store_true', help='Convert to FASTA format')

    args = parser.parse_args()

    pdb_id = args.id
    output_file = args.output

    fasta = False
    if args.fasta:
        fasta = True

    pdb_content = download_pdb(pdb_id, fasta)

    if pdb_content:
        if output_file=="-":
            print_to_console(pdb_content)
        else:
            save_to_file(pdb_content, f'{output_file}/{pdb_id}_output.fasta' if fasta else f'{output_file}/{pdb_id}_output.pdb')

if __name__ == "__main__":
    main()
