#!/usr/bin/python3
import json
import sys
import subprocess
import re
import cgi
import datetime

def execute_subprocess(query):    
    command = ["python3", "/home/h/hummelj/propra/search/run.py", "--query", query]
    result = subprocess.run(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, universal_newlines=True, encoding='utf-8')
    if result.returncode == 0:
        return {
            'success': True,
            'output': result.stdout
        }
    else:
        return {
            'success': False,
            'error': result.stderr
        }
    
def execute_seclib_subprocess(pdbid_b, file=False, name = None):
    if file:
       
        with open(f'{name}', 'w') as f:
            f.write(pdbid_b.decode('utf-8'))
        command = ["python3", "/home/h/hummelj/propra/gor/create_seclib.py", "--file_path", f'{name}']
        result = subprocess.run(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, universal_newlines=True, encoding='utf-8')
        if result.returncode == 0:
            dt = datetime.datetime.now()
            seq = int(dt.strftime("%Y%m%d%H%M%S"))
            file = f'{seq}-seclib.db'
        

            with open(file, 'w') as f:
                for line in result.stdout:
                    f.write(line)

            return {
                'success': True,
                'output': f'../cgi-bin/api/{file}',
                'file': result.stdout
            }
        else:
            return {
                'success': False,
                'error': result.stderr
            }




    else:
        pdbids = re.findall(r'"([^"]*)"',pdbid_b)
        command = ["python3", "/home/h/hummelj/propra/gor/create_seclib.py", "--pdb_id"]
        for pdbid in pdbids:
            command.append(pdbid)    
        result = subprocess.run(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, universal_newlines=True, encoding='utf-8')
        if result.returncode == 0:
            dt = datetime.datetime.now()
            seq = int(dt.strftime("%Y%m%d%H%M%S"))
            file = f'{seq}-seclib.db'
            with open(file, 'w') as f:
                bin = re.sub(r'\\n', '\n', result.stdout)
                f.write(bin)

            return {
                'success': True,
                'output': f'../cgi-bin/api/{file}'
            }
        else:
            return {
                'success': False,
                'error': result.stderr
            }
    
def main():
    print("Content-Type: application/json\n")
    form = cgi.FieldStorage()
    try:
        mode = form.getvalue('mode')
        if mode == 'query':
            query = form.getvalue('query')
            if query is not None:
                result = {'data': execute_subprocess(query)}
            else:
                result = {'error': 'Missing query'}
        elif mode == 'seclib':
            if form.getvalue('pdbId_f'):
                result = {'data': execute_seclib_subprocess(form['pdbId_f'].file.read(), True, form['pdbId_f'].filename)}

            elif form.getvalue('pdbId') is not None:
                result = {'data': execute_seclib_subprocess(form.getvalue('pdbId'))}
            else:
                result = {'error': 'Missing pdbid'}
    except Exception as e:
        result = {'error': str(e)}
    print(json.dumps(result))


if __name__ == "__main__":
    main()