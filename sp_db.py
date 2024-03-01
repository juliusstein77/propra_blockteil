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


def insert_keywords(sequence_id, type, keyword):
    cursor.execute("INSERT INTO Keywords (seq_id, type, value) VALUES (%s, %s, %s)", (sequence_id, type, keyword))

def insert_swissprot(swissprot):
    seq = swissprot['seq']['strct']
    source_id = swissprot['id']
    source = 'swissprot'
    type = 'protein'

    cursor.execute("SELECT id FROM Sequences WHERE source_id = %s", (source_id,))
    sequence_row = cursor.fetchone()

    
    existing_keywords = {}

    if not sequence_row:
        cursor.execute("INSERT INTO Sequences (type, sequence, source, source_id) VALUES (%s, %s, %s, %s)", (type, seq, source, source_id))
        cursor.execute("SELECT LAST_INSERT_ID()")
        sequence_id = cursor.fetchone()[0]

    else:
        sequence_id = sequence_row[0]
        cursor.execute("SELECT type, value FROM Keywords WHERE seq_id = %s", (sequence_id,)) 
        existing_keywords = []
        for keyword in cursor.fetchall():
            existing_keywords.append({keyword[0]: keyword[1]})
    
    if 'ac' in swissprot:
        for ac in swissprot['ac']:
            if {'ac': ac} not in existing_keywords:
                if debug:
                    print(f'inserting ac for {sequence_id} with value {ac}')
                insert_keywords(sequence_id, 'ac', ac)

    if 'de' in swissprot:
        if {'de': swissprot['de']} not in existing_keywords:	
            if debug:
                print(f'inserting de for {sequence_id} with value {swissprot["de"]}')    
            insert_keywords(sequence_id, 'de', swissprot['de'])

    if 'ref' in swissprot:
        for ref in swissprot['ref']:
            if {'rn': ref['rn']} not in existing_keywords:
                if debug:
                    print(f'inserting rn for {sequence_id} with value {ref["rn"]}')
                insert_keywords(sequence_id, 'rn', ref['rn'])
            if 'ra' in ref and {'ra': ref['ra']} not in existing_keywords:
                if debug:
                    print(f'inserting ra for {sequence_id} with value {ref["ra"]}')
                insert_keywords(sequence_id, 'ra', ref['ra'])
            if 'rt' in ref and {'rt': ref['rt']} not in existing_keywords:
                if debug:
                    print(f'inserting rt for {sequence_id} with value {ref["rt"]}')
                insert_keywords(sequence_id, 'rt', ref['rt'])
            if 'rl' in ref and {'rl': ref['rl']} not in existing_keywords:
                if debug:
                    print(f'inserting rl for {sequence_id} with value {ref["rl"]}')
                insert_keywords(sequence_id, 'rl', ref['rl'])
            
    
    if 'dr' in swissprot:
        for dr in swissprot['dr']:
            if {'dr': dr} not in existing_keywords:
                if debug:
                    print(f'inserting dr for {sequence_id} with value {dr}')
                insert_keywords(sequence_id, 'dr', dr)

    if 'ft' in swissprot:
        for ft in swissprot['ft']:
            if {'ft': ft} not in existing_keywords:
                if debug:
                    print(f'inserting ft for {sequence_id} with value {ft}')
                insert_keywords(sequence_id, 'ft', ft)

    
    if {'sq': swissprot['seq']['sq']} not in existing_keywords:
        if debug:
            print(f'inserting sq for {sequence_id} with value {swissprot["seq"]["sq"]}')
        insert_keywords(sequence_id, 'sq', swissprot['seq']['sq'])


                    
                

def get_swissprot(filename):    
    with open(filename, 'r') as file:    
        swissprot_protein = {}
        seq_state = False
        for line in file:
            if seq_state:
                if line.startswith(' '):
                    sq = line.split(" ", 1)[1].strip()
                    sq = re.sub(r'\s+', '', sq)
                    if 'strct' in swissprot_protein['seq']:
                        swissprot_protein['seq']['strct'] += sq
                    else:
                        swissprot_protein['seq']['strct'] = sq
                elif line.startswith('//'):
                    seq_state = False
                
            elif line.startswith('ID'): 
                if swissprot_protein: # reinitialize dictionary for new protein entry
                    #print(swissprot_protein)
                    insert_swissprot(swissprot_protein)
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
    

cnx = mysql.connector.connect(user='bioprakt3', password='$1$dXmWsf6J$rQWMUrRzyAhhqjPscdRbG.',
                              host='mysql2-ext.bio.ifi.lmu.de',
                              database='bioprakt3',
                              port='3306')
cursor = cnx.cursor()
parser = argparse.ArgumentParser()
parser.add_argument('--input', type=str, help='Input Swissprot file', required=True)
parser.add_argument('--debug', action='store_true', help='Print debug information', required=False)
args = parser.parse_args()
debug = args.debug
swissprots = []
file = args.input           
get_swissprot(file)
test = swissprots[:1]
print(test)
cursor.close()
cnx.close()
