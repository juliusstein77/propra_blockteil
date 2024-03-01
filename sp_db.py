#!/usr/bin/python3
import mysql.connector
from mysql.connector import errorcode
import argparse
import re
import sys
import functools
from functools import reduce
from collections import defaultdict
import pprint


cnx = mysql.connector.connect(user='bioprakt3', password='$1$dXmWsf6J$rQWMUrRzyAhhqjPscdRbG.',
                              host='mysql2-ext.bio.ifi.lmu.de',
                              database='bioprakt3',
                              port='3306')
cursor = cnx.cursor()
parser = argparse.ArgumentParser()
parser.add_argument('--input', type=str, help='Input Swissprot file', required=True)
args = parser.parse_args()

swissprots = []
file = args.input

def get_swissprot(filename):
    # set to store the results
    results = set()
    
    with open(filename, 'r') as file:    
        swissprot_protein = {}
        seq_state = False
        for line in file:
            if seq_state:
                if line.startswith(' '):
                    sq = line.split(" ", 1)[1].strip()
                    if 'strct' in swissprot_protein['seq']:
                        swissprot_protein['seq']['strct'] += sq
                    else:
                        swissprot_protein['seq']['strct'] = sq
                elif line.startswith('//'):
                    seq_state = False
                
            elif line.startswith('ID'): 
                if swissprot_protein: # reinitialize dictionary for new protein entry
                    #print(swissprot_protein)
                    swissprots.append(swissprot_protein)
                    swissprot_protein = {}
                id = line.split("ID", 1)[1].strip().split(" ")[0].strip()
                swissprot_protein['id'] = id
            
            elif line.startswith('AC'):
                ac = line.split("AC", 1)[1].strip().split(";")
                ac = [ac.strip() for ac in ac if ac.strip()]
                swissprot_protein['ac'] = ac
            
            elif line.startswith('DE'):
                de = line.split("DE", 1)[1].strip()
                if de[-1] == '.' and 'de' in swissprot_protein:
                    swissprot_protein['de'] += de
                else:
                    swissprot_protein['de'] = de
            
            elif line.startswith('RN'):
                rn = line.split("RN", 1)[1].strip().split("[")[1].strip().split("]")[0].strip()
                if 'ref' in swissprot_protein:
                    swissprot_protein['ref'].append({'rn': rn})
                else:
                    swissprot_protein['ref'] = [{'rn': rn}]
            
            elif line.startswith('RA'):
                ra = line.split("RA", 1)[1].strip()
                if ra[-1] == ';' and 'ra' in swissprot_protein.get('ref')[-1]:
                    ra = ra.split(";")[0]
                    swissprot_protein['ref'][-1]['ra'] += ra
                else:
                    swissprot_protein['ref'][-1]['ra'] = ra
            
            elif line.startswith('RT'):
                rt = line.split("RT", 1)[1].strip()
                if rt[-1] == ';' and 'rt' in swissprot_protein.get('ref')[-1]:
                    rt = rt.split("\";")[0]
                    swissprot_protein['ref'][-1]['rt'] += rt
                else:
                    if rt[0] == "\"":
                        rt = rt.split("\"")[1]
                    swissprot_protein['ref'][-1]['rt'] = rt

            elif line.startswith('RL'):
                rl = line.split("RL", 1)[1].strip()
                if rl[-1] == '.' and 'rl' in swissprot_protein.get('ref')[-1]:
                    swissprot_protein['ref'][-1]['rl'] += rl
                else:
                    swissprot_protein['ref'][-1]['rl'] = rl

            elif line.startswith('DR'):
                dr = line.split("DR", 1)[1].strip()
                if 'dr' in swissprot_protein:
                    swissprot_protein['dr'].append(dr)
                else:
                    swissprot_protein['dr'] = [dr]

            elif line.startswith('FT'):
                ft = line.split("FT", 1)[1].strip()
                if 'ft' in swissprot_protein:
                    swissprot_protein['ft'].append(ft)
                else:
                    swissprot_protein['ft'] = [ft]
            
            elif line.startswith('SQ'):
                sq = line.split("SQ", 1)[1].strip()
                swissprot_protein['seq'] = {'sq': sq}
                seq_state = True
    

           
                    
            


            
            
                

                

               
                

                    
                    
            
                
                

                
        
     

            
            
            
    """
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
                        results.update(entry_ac)"""

    # Sort the results and return them
    #return sorted(results)

get_swissprot(file)
test = swissprots[:2]
print(test)
cursor.close()
cnx.close()
