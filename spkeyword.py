#!/usr/bin/python3
# Programmierpraktikum WS2023/2024
# 240226
# Uebungsblatt 1, Aufgabe 6 (Swissprot-Keyword)
# Julius J. Stein

import argparse

# function to search for keywords in the swissprot file
def search_swissprot(keywords, filename):
    # set to store the results
    results = set()
    with open(filename, 'r') as file:
     # Set to store the AC numbers of the current entry
        for line in file:
            # If the line starts with 'ID', it is a new entry
            if line.startswith('ID'):
                entry_ac = set()  # Reset entry AC when encountering a new entry
            # If inside an entry and the line starts with 'AC', store the AC numbers
            elif line.startswith('AC'):
                #print(line)

                ac_line = line.split("AC", 1)[1].strip()  # Split the line and remove leading/trailing whitespace
                ac_ids = ac_line.split(";")  # Split the accession numbers separated by ';'
                ac_ids = [ac.strip() for ac in ac_ids if ac.strip()]

                #entry_ac_temp = line.split("AC", 1)[1].split(";")
                #entry_ac_temp = [e.strip() for e in entry_ac if e.strip()]  # Strip whitespace from each entry and filter out empty entries

                #print(ac_ids)
                entry_ac.update(ac_ids)
            # If inside an entry and the line starts with 'KW', check for keywords
            elif entry_ac and line.startswith('KW'):
                #print(line)
                kw_temp = line.split("KW", 1)[1].strip()  # Split the line and remove leading/trailing whitespace
                kw_temp = kw_temp.split(";")  # Split the keywords separated by ';'
                keywords_list = [kw.strip().rstrip('.') for kw in kw_temp]
                #print(keywords_list)
                for keyword in keywords:
                    # If the whole keyword is found in the line, add the entry AC to the results set
                    if keyword in keywords_list:
                        results.update(entry_ac)

    # Sort the results and return them
    return sorted(results)

def main():
    # Argparser to parse the keywords and the swissprot file
    parser = argparse.ArgumentParser(description='Search SwissProt entries by keyword.')
    # Add the keywords and the swissprot file as arguments (multiple keywords possible)
    parser.add_argument('--keyword', metavar='keyword', type=str, nargs='+', required=True,
                        help='Keywords to search for')
    # Add the swissprot file as an argument
    parser.add_argument('--swissprot', metavar='swissprot_file', type=str, required=True,
                        help='SwissProt data file')
    # Parse the arguments
    args = parser.parse_args()

    keywords = args.keyword
    filename = args.swissprot

    found_entries = search_swissprot(keywords, filename)

    # Print the results
    for entry in found_entries:
        print(entry)

if __name__ == "__main__":
    main()
