# Sequence Similarity

For a given input `.db` file, calculates the pairwise `ncd` for each sequence and filter
out the ones that are below a given threshold. This way we can filter out the sequences that
are too similar to each other.
The script will output two plots and a `.db` file with the filtered sequences.
The first plot is a heatmap of the `ncd` values before filtering, and the second plot is a
heatmap of the `ncd` values after filtering.

# Usage

```bash
python3 sequence_similarity.py -d input.db -t 0.5
```

# Output

- `input_filtered.db`: The filtered `.db` file.
- `input.png`: The heatmap of the `ncd` values before filtering.
- `input_filtered.png`: The heatmap of the `ncd` values after filtering.
