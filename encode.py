import argparse

parser = argparse.ArgumentParser()
parser.add_argument('--input', type=str, help='Path to input-file')
parser.add_argument('--output', type=str, help='Path to output-directory')
args = parser.parse_args()

def dict_to_tsv(dictionary, output):
    with open(output, 'w') as file:
        for key, value in dictionary.items():
            file.write(f"{key}\t{value}\n")

def extype_count(lines):
    experiments = {}
    for line in lines:
        line = line.split(',')
        type = line[0];
        if type not in experiments:
            experiments[type] = 1
        else:
            experiments[type] += 1
    return experiments


def read_lines(file):
    lines = []
    with open(file, 'r') as file:
        for line in file:
            lines.append(line.strip('\n'))
    return lines



lines = read_lines(args.input)[1:-1]

dict_to_tsv(extype_count(lines), args.output+"/exptypes.tsv") # Aufgabe 9.1

celltypes = {}
for line in lines:
    exp = line.split(',')[0]
    cl_tp = line.split(',')[1]
    exp_fct = line.split(',')[2]
    if cl_tp not in celltypes:
        if line.split(',')[0] == 'ChIP-seq':
            antibody = (exp_fct.split(' ')[0]).split('=')[1]
            celltypes[cl_tp] = [antibody]
        else:
            celltypes[cl_tp] = []
    else:
        if line.split(',')[0] == 'ChIP-seq':
            antibody = (exp_fct.split(' ')[0]).split('=')[1]
            if antibody not in celltypes[cl_tp]:
                celltypes[cl_tp].append(antibody)



out = ''
for element in celltypes:
    out += element + '\t' + str(len(celltypes[element])) + '\n' # Aufgabe 9.1
with open(args.output + '/' + 'antibodies.tsv', 'w') as out2:
    out2.write(out)







#Programmierpraktikum WS2023/2024
# 240226
# Uebungsblatt 1, Aufgabe 9 (Encode Projekt), Teilaufgabe 3
# Julius J. Stein

import csv
import os
import re

# load data from csv file
def load_encode_data(input_file):
    encode_data = []
    with open(input_file, 'r') as file:
        reader = csv.DictReader(file)
        for row in reader:
            encode_data.append(row)
    return encode_data

# count the number of experiments for each data type using DCC_Accession
def filter_chip_rna_seq(encode_data):
    # Dictionary to store ChIP-seq and RNA-seq data for each cell type
    chip_rna_seq = {}
    # Iterate over each entry in the encode data
    for entry in encode_data:
        cell_type = entry['Cell_Type']
        data_type = entry['Data_Type']
        factors = entry['Experimental_Factors']
        dcc_accession = entry['DCC_Accession']
        # check If the data type is ChIP-seq and the used antibody is H3K27me3
        if data_type == 'ChIP-seq' and factors == 'Antibody=H3K27me3':
            # If the cell type is not already in the dictionary, add it
            if cell_type not in chip_rna_seq:
                chip_rna_seq[cell_type] = {'ChIP-seq': [], 'RNA-seq': []}
            # Add the DCC accession to the ChIP-seq list for the cell type
            chip_rna_seq[cell_type]['ChIP-seq'].append(dcc_accession)
        # check if the data type is RNA-seq
        elif data_type == 'RNA-seq':
            # If the cell type is not already in the dictionary, add it
            if cell_type not in chip_rna_seq:
                chip_rna_seq[cell_type] = {'ChIP-seq': [], 'RNA-seq': []}
            # Add the DCC accession to the RNA-seq list for the cell type
            chip_rna_seq[cell_type]['RNA-seq'].append(dcc_accession)

    # Remove entries where either RNA-seq or ChIP-seq data is missing
    chip_rna_seq = {cell_type: data for cell_type, data in chip_rna_seq.items() if data['ChIP-seq'] and data['RNA-seq']}

    return chip_rna_seq

# Write the ChIP-seq and RNA-seq data to a tsv file
def write_chip_rna_seq_table(chip_rna_seq, output_dir):
    output_file = os.path.join(output_dir, 'chip_rna_seq.tsv')
    with open(output_file, 'w') as file:
        # Write the header
        file.write("cell line\tRNAseq Accession\tChIPseq Accession\n")
        # Write the data
        for cell_type, data in sorted(chip_rna_seq.items()):
            # Sort the accessions and join them into a comma separated string
            chip_seq_accessions = ','.join(sorted(data['ChIP-seq'])) if data['ChIP-seq'] else ''
            rna_seq_accessions = ','.join(sorted(data['RNA-seq'])) if data['RNA-seq'] else ''
            rna_seq_accessions = re.sub(r',{2,}', ',', rna_seq_accessions).strip(',')
            chip_seq_accessions = re.sub(r',{2,}', ',', chip_seq_accessions).strip(',')
            file.write(f"{cell_type}\t{rna_seq_accessions}\t{chip_seq_accessions}\n")

encode_data = load_encode_data(args.input)
chip_rna_seq = filter_chip_rna_seq(encode_data)
write_chip_rna_seq_table(chip_rna_seq, args.output)


