#!/usr/bin/python3
# Programmierpraktikum WS2023/2024
# 240229
# Uebungsblatt 2, Aufgabe 5 (Finding ORFs)
# Julius J. Stein

import argparse
import re
import matplotlib.pyplot as plt

# Count all ORFs in a file with lengths between lower and upper
def count_orfs_in_range(orf_file, lower, upper):
    orf_lengths = []

    with open(orf_file, 'r') as file:
        current_orf = ''
        for line in file:
            if line.startswith('>'):
                if current_orf:
                    # multiply by 3 to get the length in nucleotides
                    # (since the length of the ORF is given in amino acids)
                    if lower*3 <= len(current_orf) <= upper*3:
                        # append the length of the ORF to the list
                        orf_lengths.append(len(current_orf))
                    current_orf = ''
            else:
                current_orf += line.strip()

        # Check length of last sequence
        if current_orf:
            if lower <= len(current_orf) <= upper:
                orf_lengths.append(len(current_orf))

    count = len(orf_lengths)
    return count, orf_lengths

# Plot a histogram of ORF lengths
def plot_histogram(orf_lengths, lower, upper, bins, outfile=None):
    # histogram of ORF lengths with specified bins and range
    plt.hist(orf_lengths, bins=bins, range=(lower, upper), color='skyblue', edgecolor='black')
    # add title and labels
    plt.title('Histogram of ORF Lengths')
    plt.xlabel('ORF Length')
    plt.ylabel('Frequency')

    # save the plot to a file or display it
    if outfile:
        plt.savefig(outfile, dpi=300)
    else:
        plt.show()

def main():
    parser = argparse.ArgumentParser(description="Count ORFs in a file and optionally plot histogram")
    parser.add_argument("--fasta", required=True, help="Input ORF file containing sequences")
    parser.add_argument("--histogram", help="Output file to save histogram plot")
    parser.add_argument("--lower", type=int, required=True, help="Lower bound of ORF length range")
    parser.add_argument("--upper", type=int, required=True, help="Upper bound of ORF length range")
    parser.add_argument("--bins", type=int, default=5, help="Number of bins for histogram")
    args = parser.parse_args()

    # return the number of ORFs and their lengths
    orf_count, orf_lengths = count_orfs_in_range(args.fasta, args.lower, args.upper)
    # convert the lengths to protein length
    for i in range(len(orf_lengths)):
        orf_lengths[i] = orf_lengths[i] / 3  # Update the value by dividing by 3
    print(f"Number of ORFs between {args.lower} and {args.upper} Amino Acids: {orf_count}")

    # plot the histogram if requested
    if args.histogram:
        plot_histogram(orf_lengths, args.lower, args.upper, args.bins, args.histogram)
    else:
        print("Histogram plot not requested.")

if __name__ == "__main__":
    main()