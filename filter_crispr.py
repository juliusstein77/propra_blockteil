#!/usr/bin/python3
import argparse
import sys
import regex as re

# From SAMv11 Documentation, p. 6
SAM_COLS = {"HEADER": 0,
            "CIGAR": 5,
            "SEQ": 9,
            "MD": 12}


def parse_sam_file(sam):
    sam_data = []
    with open(sam, 'r') as f:
        for line in f:
            # Skip SAM Headers
            if not line.startswith('@'):
                columns = line.strip().split('\t')
                header = columns[SAM_COLS['HEADER']]
                cigar = columns[SAM_COLS['CIGAR']]
                seq = columns[SAM_COLS['SEQ']]
                if len(columns) > SAM_COLS['MD']:
                    md = columns[SAM_COLS['MD']]
                else:
                    md = None
                sam_data.append((header, cigar, seq, md))
    return sam_data


def filter_with_mismatch(sam_data, with_mismatch):
    for data in sam_data:
        if data[3] is None:
            continue
        if re.search(r'[^8-9][01]$', data[3]):
            with_mismatch.write(f'>{data[0]}\n{data[2]}\n')


def filter_no_off_targets(sam_data, no_off_targets):
    for data in sam_data:
        if data[1] == '*':
            no_off_targets.write(f'>{data[0]}\n{data[2]}\n')


def main():
    parser = argparse.ArgumentParser(description='Filter aligned sequences based on criteria.')
    parser.add_argument('--sam', help='Input SAM file', required=True, type=argparse.FileType('r'))
    parser.add_argument('--no-off-targets', help='Output file for sequences with no off-targets', required=True, type=argparse.FileType('w'))
    parser.add_argument('--with-mismatch', help='Output file for sequences with mismatches in GG suffix', required=True,type=argparse.FileType('w'))
    args = parser.parse_args()

    #sam_input = args.sam
    #if sam_input == '-':
    #    sam_input = sys.stdin

    sam_data = parse_sam_file(args.sam)

    filter_no_off_targets(sam_data, args.no_off_targets)

    filter_with_mismatch(sam_data, args.with_mismatch)


if __name__ == "__main__":
    main()
