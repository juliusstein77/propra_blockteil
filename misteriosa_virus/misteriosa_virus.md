# 05 -  Misteriosa Virus
Unsere `.fasta` Datei beinhaltet eine Sequenz, mit einer gesamt Länge von 4302 Basenpaaren.
Wir haben `blastn` benutzt, um die Sequenz zu analysieren und haben herausgefunden, dass es sich um ein Virus handelt.
Als `DB` haben wir `nt` benutzt.

# Ergebnisse
Es wurden insgesamt 100 Sequenzen durchsucht, alle von ihnen stammen 
aus dem Organismus `SARS-CoV-2`:
- **Taxonomy ID:** 2697049
- **Name:** Severe acute respiratory syndrome coronavirus 2 (SARS-CoV-2)
Es handelt sich also um das **Coronavirus**.
Um genauer zu sein haben wir es hier mit dem **Spike Protein** (GeneID: `43740568 (S)`)
zu tun:
- Location:	21,563..25,384
- Length:	3,822 nt

Als referenzquelle haben wir https://www.ncbi.nlm.nih.gov/gene/43740568 benutzt.


## E-Values
Die `E-Values` bei **BLAST** versucht die Anzahl an Alignments vorherzusagen,
die (ob wohl sie random sind), einen gewissen Score erreicht haben.
So können wir also abschätzen, ob ein Alignment zufällig ist oder nicht.
Ein niedriger `E-Value` bedeutet also, dass das Alignment wahrscheinlich nicht zufällig ist
und impliziert ein signifikantes Ergebnis.
