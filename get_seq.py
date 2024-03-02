#!/usr/bin/python3
import mysql.connector
from mysql.connector import errorcode
import argparse
def to_fasta(seq):
    return ">"+str(list(seq.keys())[0])+"\n"+str(list(seq.values())[0])


def get_sequence(source_id, source):
    cursor.execute("SELECT `sequence` FROM Sequences WHERE `source_id` = %s AND `source` = %s", (source_id, source))
    return {source_id:cursor.fetchone()[0]}


cnx = mysql.connector.connect(user='bioprakt3', password='$1$dXmWsf6J$rQWMUrRzyAhhqjPscdRbG.',
                              host='mysql2-ext.bio.ifi.lmu.de',
                              database='bioprakt3',
                              port='3306')
cursor = cnx.cursor()

parser = argparse.ArgumentParser()
parser.add_argument('--id', type=str, help='Id of Sequence', nargs='+')
parser.add_argument('--source', type=str, help='Input Source of Sequence')
args = parser.parse_args()


if args.id and args.source:
    seqs = []
    for id in args.id:
        seqs.append(get_sequence(id, args.source))
    for seq in seqs:
        print(to_fasta(seq))
    
