#!/usr/bin/python3
# Programmierpraktikum WS2023/2024
# 240229
# Uebungsblatt 2, Aufgabe 5 (Finding ORFs)
# Julius J. Stein

import argparse
import random

# Generate a random DNA sequence with the specified length and GC content
def generate_random_sequence(length, gc_content):
    # Calculate the needed number of GC and AT bases
    num_gc = int(length * gc_content)
    num_at = length - num_gc
    sequence = ''
    # Generate the sequence by adding random GC and AT bases (with the correct ratio)
    for i in range(num_gc):
        # random.choice selects a random element from the given sequence
        sequence += random.choice('GC')
    for i in range(num_at):
        sequence += random.choice('AT')
    # Shuffle the sequence to make it random
    # random.sample selects a random sample of the given length from the sequence
    sequence = ''.join(random.sample(sequence, min(length, len(sequence))))  # Shuffle the sequence
    return sequence

def main():
    parser = argparse.ArgumentParser(description="Generate a random DNA sequence with specified length and GC content")
    parser.add_argument("--length", type=int, required=True, help="Length of the DNA sequence")
    parser.add_argument("--gc", type=float, required=True, help="GC content of the DNA sequence (between 0 and 1)")
    parser.add_argument("--name", help="Name of the sequence (optional)")
    args = parser.parse_args()

    # check if GC content is between 0 and 1
    if args.gc < 0 or args.gc > 1:
        print("Error: GC content must be between 0 and 1")
        return

    # generate the random sequence
    sequence = generate_random_sequence(args.length, args.gc)

    # print the sequence with an optional name
    if args.name:
        print(f">{args.name}")
    print(sequence)

if __name__ == "__main__":
    main()
