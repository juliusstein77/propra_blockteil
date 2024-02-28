#!/usr/bin/python3
import argparse

def main():
    parser = argparse.ArgumentParser(
        description="Search for fully sequenced genomes based on organism names and regex patterns.")
    parser.add_argument("--input", nargs="+", help="path to encode.csv", required=True)
    parser.add_argument("--output", nargs="+", help="path to output directory", required=True)
    args = parser.parse_args()

    input_path = args.input[0]
    output_path = args.output[0]

    print("Input path: ", input_path)

if __name__ == '__main__':
    main()
