#!/usr/bin/python3
import mysql.connector
from mysql.connector import errorcode


cnx = mysql.connector.connect(user='bioprakt3', password='$1$dXmWsf6J$rQWMUrRzyAhhqjPscdRbG.',
                              host='mysql2-ext.bio.ifi.lmu.de',
                              database='bioprakt3',
                              port='3306')
cursor = cnx.cursor()

cursor.execute("CREATE TABLE IF NOT EXISTS Sequences (id INT AUTO_INCREMENT PRIMARY KEY, type VARCHAR(250), sequence LONGTEXT NOT NULL, source VARCHAR(250), source_id VARCHAR(250), os_id INT, CONSTRAINT Sequences_Org_fk_1 FOREIGN KEY (os_id) REFERENCES Organisms (id) ON DELETE CASCADE)")
cursor.execute("CREATE TABLE IF NOT EXISTS Organisms (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(250))")
cursor.execute("CREATE TABLE IF NOT EXISTS Keywords (id INT AUTO_INCREMENT PRIMARY KEY, seq_id INT NOT NULL, type VARCHAR(250), value VARCHAR(250), CONSTRAINT Keywords_Seq_fk_1 FOREIGN KEY (seq_id) REFERENCES Sequences (id) ON DELETE CASCADE)")
cursor.execute("CREATE TABLE IF NOT EXISTS Proteins (head VARCHAR(250) NOT NULL PRIMARY KEY, seq_id INT NOT NULL, CONSTRAINT Prot_Seq_fk_1 FOREIGN KEY (seq_id) REFERENCES Sequences (id) ON DELETE CASCADE)")
cursor.execute("CREATE TABLE IF NOT EXISTS Alignments (id INT AUTO_INCREMENT PRIMARY KEY, almnt_id INT NOT NULL, prot_head VARCHAR(250) NOT NULL, CONSTRAINT AlgnProt_Prot_fk_1 FOREIGN KEY (prot_head) REFERENCES Proteins (head) ON DELETE CASCADE, CONSTRAINT Alignments_Hmstd_fk_1 FOREIGN KEY (almnt_id) REFERENCES Homestrad (id) ON DELETE CASCADE)")
cursor.execute("CREATE TABLE IF NOT EXISTS Homestrad (id INT AUTO_INCREMENT PRIMARY KEY, family VARCHAR(250), alignment LONGTEXT NOT NULL)")
cursor.execute("SHOW TABLES")
for x in cursor:
  print(x)


cursor.close()
cnx.close()