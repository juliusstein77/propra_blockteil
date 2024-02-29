import visualize_mol
import argparse
import os
import math

# condtants
X_COORD=4
Y_COORD=5
Z_COORD=6
ALPHA_BETA=0


def calculate_distance(point1: tuple, point2: tuple):
    """
    for two coordinates a and b in r^3, calculate_distance
    """

    x1, y1, z1 = point1
    x2, y2, z2 = point2
    distance = math.sqrt((x2 - x1)**2 + (y2 - y1)**2 + (z2 - z1)**2)
    return distance


def main(pdb_ids: list, output_dir: str):
    """
    main function to pass all pdb_ids to visualize_mol and calculate_pdb
    """

    if output_dir != None:
        # init out_dir
        if not os.path.exists(output_dir):
            os.mkdir(output_dir)

        # plot results
        for pdb_id in pdb_ids:
            out_png_path = os.path.join(output_dir, f'{pdb_id}.png')
            visualize_mol.main(pdb_id, out_png_path, False, True)

    # retrieve files and store in datadir
    pdb_id_file_paths = []
    for pdb_id in pdb_ids:
        path_to_pbd = visualize_mol.retrive_pdb_id(pdb_id)
        pdb_id_file_paths.append(path_to_pbd)

    for pdb_id, pdb_path in zip(pdb_ids, pdb_id_file_paths):
        calculate_pdb(pdb_id, pdb_path)


def calculate_pdb(pdb_id: str, pdb_path: str):
    """
    calculate all relevant data for a given pdb_id
    """

    file_lines = []
    with open(pdb_path) as f:
        file_lines = f.readlines()

    # a dict storing atom pos: values of remaining column
    atom_dict = {}
    aas_in_sec_total = 0
    total_length = 0
    for line in file_lines:
        entries = line.split(" ")

        # here i need to filter out all "" in my entrie
        filtered_entrie = [elem for elem in entries if elem != "" and elem != "\n"]

        # grab all atom related information
        if filtered_entrie[0] == "ATOM":
            atom_dict[filtered_entrie[1]] = filtered_entrie[2:]
        
        # chech sec struct
        elif filtered_entrie[0] == "HELIX": 
            start_pos = int(filtered_entrie[5])
            end_pos = (int(filtered_entrie[8]))
            aas_in_sec = abs(end_pos - start_pos) + 1
            aas_in_sec_total+=aas_in_sec
        elif filtered_entrie[0] == "SHEET":
            start_pos = int(filtered_entrie[6])
            end_pos = (int(filtered_entrie[9]))
            aas_in_sec = abs(end_pos - start_pos) + 1
            aas_in_sec_total+=aas_in_sec
        
        # get total length
        elif filtered_entrie[0] == "DBREF":
            total_length = int(filtered_entrie[4])

    # retreve all x,y,z coords
    x_coords = []
    y_coords = []
    z_coords = []

    # stores all entries of alpha and beta atoms
    alpha_atoms = []
    beta_atoms = []

    for _, atom_data in atom_dict.items():
        # get all coords
        x_coords.append(float(atom_data[X_COORD]))
        y_coords.append(float(atom_data[Y_COORD]))
        z_coords.append(float(atom_data[Z_COORD]))

        # get x,y,z for all alpha and beta
        if atom_data[ALPHA_BETA] == "CA":
            alpha_atoms.append((float(atom_data[X_COORD]),float(atom_data[Y_COORD]),float(atom_data[Z_COORD])))
        elif atom_data[ALPHA_BETA] == "CB": 
            beta_atoms.append((float(atom_data[X_COORD]),float(atom_data[Y_COORD]),float(atom_data[Z_COORD])))


    # get relevant x,y,z tupels
    alpha_f = alpha_atoms[0]
    alpha_l = alpha_atoms[-1]
    beta_f = beta_atoms[0]
    beta_l = beta_atoms[-1]

    # calculate_distance
    alpha_dist = calculate_distance(alpha_f, alpha_l)
    beta_dist = calculate_distance(beta_f, beta_l)

    min_x, max_x = min(x_coords), max(x_coords)
    min_y, max_y = min(y_coords), max(y_coords)
    min_z, max_z = min(z_coords), max(z_coords)

    abs_x = (max_x - min_x)
    abs_y = (max_y - min_y)
    abs_z = (max_z - min_z)
    
    volume = round(abs_x * abs_y * abs_z, 4)


    # std out
    print(f"{pdb_id}\tAnteil AS in Sekundaerstruktur\t{'{:.4f}'.format(round(aas_in_sec_total/total_length, 4))}")
    print(f"{pdb_id}\tAbstand C_alpha\t{'{:.4f}'.format(round(alpha_dist, 4))}")
    print(f"{pdb_id}\tAbstand C_beta\t{'{:.4f}'.format(round(beta_dist, 4))}")
    print(f"{pdb_id}\tX-Groesse\t{'{:.4f}'.format(round(abs_x, 4))}")
    print(f"{pdb_id}\tY-Groesse\t{'{:.4f}'.format(round(abs_y, 4))}")
    print(f"{pdb_id}\tZ-Groesse\t{'{:.4f}'.format(round(abs_z, 4))}")
    print(f"{pdb_id}\tVolumen\t{'{:.4f}'.format(volume)}")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Visualize all pdb_ids and calculate relevant data")
    parser.add_argument('--id', type=str, nargs="+", required=True, help="pdb_ids to calculate")
    parser.add_argument('--output', type=str, required=False, default=None, help="visualize all pdb_ids and store in output dir") 
    
    args = parser.parse_args()
    
    main(args.id, args.output)

