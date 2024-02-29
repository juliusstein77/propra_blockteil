import visualize_mol
import argparse
import os

def main(pdb_ids, output_dir):

    if output_dir != None:
        # init out_dir
        if not os.path.exists(output_dir):
            os.mkdir(output_dir)

        # plot results
        for pdb_id in pdb_ids:
            out_png_path = os.path.join(output_dir, f'{pdb_id}.png')
            visualize_mol.main(pdb_id, out_png_path, False, True)


def calculate_pbd(pb):
    pass

# first aa: Ca
# ATOM      2  CA  VAL A   1      -3.600  16.400  15.300  1.00  0.00           C  
# last aa: Cb
# ATOM   1208  CB  GLN A 152       0.300  25.000  -3.400  1.00  0.00           C  

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="description")
    parser.add_argument('--id', type=str, nargs="+", required=True)
    parser.add_argument('--output', type=str, required=False, default=None) 
    
    args = parser.parse_args()
    
    main(args.id, args.output)

