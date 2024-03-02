#!/usr/bin/python3
# Programmierpraktikum WS2023/2024
# 240302
# Uebungsblatt 2, Aufgabe 6 (spkeyword in SQL)
# Julius J. Stein

import argparse
import mysql.connector

# search for sequences in the database
def search_sequences_db(db, keywords):
    matches = set()
    # Create cursor
    cursor = db.cursor()
    # Prepare SQL query to select sequences matching the pattern
    for keyword in keywords:
        sql_query = f"""
                SELECT k1.value AS ac
                FROM Keywords k1
                JOIN Keywords k2 ON k1.seq_id = k2.seq_id
                WHERE k2.type = 'kw' AND k2.value = 'Germination' AND k1.type = 'ac' ORDER BY ac ASC;
            """
        # Execute SQL query
        cursor.execute(sql_query)
        ac_values = {row[0] for row in cursor.fetchall()}  # Set comprehension to extract values from tuples
        matches.update(ac_values)
    # Close cursor
    cursor.close()
    return sorted(matches)

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
                ac_line = line.split("AC", 1)[1].strip()  # Split the line and remove leading/trailing whitespace
                ac_ids = ac_line.split(";")  # Split the accession numbers separated by ';'
                # Remove leading/trailing whitespace from each accession number and add it to the entry AC set
                ac_ids = [ac.strip() for ac in ac_ids if ac.strip()]
                entry_ac.update(ac_ids)
            # If inside an entry and the line starts with 'KW', check for keywords
            elif entry_ac and line.startswith('KW'):
                kw_temp = line.split("KW", 1)[1].strip()  # Split the line and remove leading/trailing whitespace
                kw_temp = kw_temp.split(";")  # Split the keywords separated by ';'
                keywords_list = [kw.strip().rstrip('.') for kw in kw_temp]
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
    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument('--swissprot', metavar='swissprot_file', type=str,
                        help='SwissProt data file')
    group.add_argument("--db", action="store_true", help="Search for pattern in the database")

    args = parser.parse_args()

    keywords = args.keyword

    if args.swissprot:
        filename = args.swissprot

    if args.db:
        db = mysql.connector.connect(
            host="localhost",
            user="bioprakt3",
            password="$1$dXmWsf6J$rQWMUrRzyAhhqjPscdRbG.",
            database="bioprakt3",
            port="3307"
        )

    if args.db:
        found_entries = search_sequences_db(db, keywords)
        for entry in found_entries:
            print(entry)

    if args.swissprot:
        found_entries = search_swissprot(keywords, filename)
        for entry in found_entries:
            print(entry)

if __name__ == "__main__":
    main()
