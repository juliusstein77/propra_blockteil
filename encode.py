#!/usr/bin/python3
import argparse
import pandas as pd

from utils import create_dataframe_from_csv, read_lines, dict_to_tsv, strip_exp_factor

# line in lines: Data_Type,Cell_Type,Experimental_Factors,Treatment,Lab,PI,Assembly,Status,GEO_Accession,DCC_Accession,Date_Unrestricted
import pandas as pd



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


def main():
    parser = argparse.ArgumentParser(
        description="Search for fully sequenced genomes based on organism names and regex patterns.")
    parser.add_argument("--input", nargs="+", help="path to encode.csv", required=True)
    parser.add_argument("--output", nargs="+", help="path to output directory", required=True)
    args = parser.parse_args()


    lines = read_lines(args.input[0])
    
    dict_to_tsv(extype_count(lines[1:-1]), args.output[0]+"/exptypes.tsv") # Aufgabe 9.1

    df = create_dataframe_from_csv(args.input[0])


    df = df[df['Experimental_Factors'].str.contains('Antibody')]
    df['Experimental_Factors'] = df['Experimental_Factors'].apply(lambda x: strip_exp_factor(x, 'antibody', True)) #alle records mit antikörper
    
    chip_df = df[df['Data_Type'].str.contains('ChIP-seq', case=False, na=False)]
    antibodies_per_cell_line = chip_df.groupby('Cell_Type')['Experimental_Factors'].nunique().reset_index()

    print(antibodies_per_cell_line)
    

    





    








if __name__ == '__main__':
    main()
