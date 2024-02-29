#!/usr/bin/python3

import argparse
import requests
import os
import subprocess

OUTDIR="out/"
DATADIR="data/"
SCRIPTPATH="jmol_script.txt"

def retrive_pdb_id(pdb_id):
    """Retrives pdb_id as file into an out.pdb.

    :pdb_id: id of pdb to look up in db
    :returns: the path to out.pdb

    """
    # check if OUTDIR exists 
    if not os.path.exists(OUTDIR):
            os.makedirs(OUTDIR)

    # check if DATADIR exists 
    if not os.path.exists(DATADIR):
            os.makedirs(DATADIR)

    #  FIX: implement Julius version of this code
    url = f"https://files.rcsb.org/download/{pdb_id}.pdb"
    out_pdb_path = f"{DATADIR}{pdb_id}.pdb"
    response = requests.get(url)

    if response.status_code == 200:
        with open(out_pdb_path, 'wb') as f:
            f.write(response.content)
    else:
        print(f"Failed to retrieve PDB file for ID {pdb_id}.\nStatus code: {response.status_code}")

    return out_pdb_path


def make_script(path_to_pdb: str, color: bool, png_out: str):
    """writes the jmol script into stript.txt.

    :path_to_pdb: 
    :color: a bool which if True adds an additional line to the script
    """
    print(png_out)
    
    with open(SCRIPTPATH, "w") as f:
        f.write(f'load {path_to_pdb}\n')
        f.write('select all\n')         
        f.write('wireframe off\n')
        f.write('spacefill off\n')      
        if color:  # only colorize if necessary
            f.write('color structure\n')
        f.write('cartoon on\n')
        f.write(f'write PNG {OUTDIR}{png_out}.png\n')
        f.write('exit')


def run_jmol(script_file):
    """run jmol with the specified script file.

    :script_file: path to the created script 
    """
    # Command to run Jmol with the specified script file
    command = ["java", "-jar", "Jmol.jar", "-s", script_file, "--exit"]

    # Run the command
    subprocess.run(command)


def main(id: str, output: str, is_html: bool, is_color: bool):
    """Manages all inputs of program.

    :id: pdb id to download
    :output: dir for .pngs
    :is_html: flag to switch to html output
    :is_color: colorize secondary structure
    """
    
    path_to_pdb = retrive_pdb_id(id)
    if output == "":
        output = id
    make_script(path_to_pdb, is_color, output)
    run_jmol(SCRIPTPATH)


if __name__ == "__main__":
    # init parser
    parser = argparse.ArgumentParser(description="description")
    parser.add_argument('--id', type=str, required=True)
    parser.add_argument('--output', type=str, required=False, default="")
    parser.add_argument('--html', type=str, required=False)
    parser.add_argument('--colourized', type=str, required=False)

    args = parser.parse_args()

    main(args.id, args.output, args.html, args.colourized)
