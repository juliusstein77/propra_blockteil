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

