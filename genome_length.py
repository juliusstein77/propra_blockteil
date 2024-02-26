#!/usr/bin/python3
import argparse
from urllib.request import urlopen
import regex as re


# Downloading of Genome Report
def download_report():
    url = "ftp://ftp.ncbi.nlm.nih.gov/genomes/GENOME_REPORTS/prokaryotes.txt"
    genome_report = urlopen(url)
    return genome_report.read().decode('utf-8')


def get_genome_info(genome_report, regex_list):
    genome_info = []
    lines = genome_report.split('\n')
    for line in lines:
        if line.startswith('#'):
            continue

        columns = line.split("\t")
        if len(columns) >= 15:
            # Check, if its completely sequenced
            if columns[15] != 'Complete Genome':
                continue

            # Extract Organism Name and Genome_length in Mb
            organism_name = columns[0]
            genome_length = float(columns[6])

            for regex in regex_list:
                if re.search(regex, organism_name):
                    genome_info.append((organism_name, genome_length))
                    break
    return genome_info


def main():
    # Argument-Parsing
    parser = argparse.ArgumentParser(
        description="Search for fully sequenced genomes based on organism names and regex patterns.")
    parser.add_argument("--organism", nargs="+", help="Given Regex patterns", required=True)
    args = parser.parse_args()

    genome_report = download_report()

    regex_list = args.organism
    genome_info = get_genome_info(genome_report, regex_list)

    for organism, length in genome_info:
        print(f"{organism}\t{length}")


if __name__ == '__main__':
    main()
