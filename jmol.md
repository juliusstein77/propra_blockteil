# Usage of Jmol
- Navigate to `Jmol.jar`
- Pass args to `Jmol.jar`:
```bash
java -jar Jmol.jar -s script.txt
```

In the `script.txt` you can pass args line by line:

**EXAMPLE**
```
load "1crn.pdb"  # load .pdb into Jmol
select all 
wireframe off  # turn off the default display style
spacefill off  
color structure  # add color to secondary structure
cartoon on  # enable cartoon mode (for secondary structure)
write PNG "1crn_cartoon.png"  # save image to .png
```

