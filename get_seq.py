#!/usr/bin/python3
import mysql.connector
from mysql.connector import errorcode
import argparse


cnx = mysql.connector.connect(user='bioprakt3', password='$1$dXmWsf6J$rQWMUrRzyAhhqjPscdRbG.',
                              host='mysql2-ext.bio.ifi.lmu.de',
                              database='bioprakt3',
                              port='3306')
cursor = cnx.cursor()

parser = argparse.ArgumentParser()
parser.add_argument('--id', type=str, help='Id of Sequenc')
parser.add_argument('--source', type=str, help='Input Source of Sequence')
args = parser.parse_args()

select_string = "SELECT `id`, `sequence` FROM Sequences " \
                "WHERE `id` in ("
for id in args.id:
    select_string += id
    select_string += ","

select_string = select_string[0:len(select_string)-1]
select_string += ")"

try:
    # write in fasta file
    cursor.execute(select_string)
    with cnx.cursor() as cursor:
        cursor.execute(select_string)
        rows = cursor.fetchall()
        
finally:
    cursor.close()
    cnx.close()
for row in rows:
            print(">"+row[0])
            print(row[1])

