# 05 -  Misteriosa Virus
Unsere `.fasta` Datei beinhaltet eine Sequenz, mit einer gesamt Länge von 4302 Basenpaaren.
Wir haben `blastn` benutzt, um die Sequenz zu analysieren und haben herausgefunden, dass es sich um ein Virus handelt.
Als `DB` haben wir `nt` benutzt.

# Ergebnisse
Der genaue Output kann in der Datei `blastn_results.txt` gefunden werden.
## Virus
- **Taxonomy ID:** 2697049
- **Name:** Severe acute respiratory syndrome coronavirus 2 (SARS-CoV-2)
Also handelt es sich hier um das **Coronavirus**.

Die Sequence hat die ID: `OR979776.1`

## E-Values
Die `E-Values` bei **BLAST** versucht die Anzahl an Alignments vorherzusagen,
die (ob wohl sie random sind), einen gewissen Score erreicht haben.
So können wir also abschätzen, ob ein Alignment zufällig ist oder nicht.
