#!/usr/bin/python3
import json
import sys
import subprocess
import re
import cgi
import cgitb
import os
import datetime 

cgitb.enable()

def execute_subprocess(alignment_data, dwnl):
    
    
    command = ["java", "-jar", "/mnt/biocluster/praktikum/bioprakt/progprakt-03/Solution3/NeedlemanWunsch.jar"]
    if alignment_data['go']:
        command.append("--go")
        command.append(alignment_data['go'])
    if alignment_data['ge']:
        command.append("--ge")
        command.append(alignment_data['ge'])

    
    command.append("--mode")
    if alignment_data['global_align']:
        command.append("global")
    elif alignment_data['local_align']:
        command.append("local")
    elif alignment_data['freeshift_align']:
        command.append("freeshift")
    command.append("--format")
    command.append("ali")
    if alignment_data['nw']:
        command.append("--nw")
    if alignment_data['seq_1']:
        with open('temp_seq_1.seqlib', 'w') as f:
            for seq in alignment_data['seq_1'].split(','):
                i = seq.split(':')
                f.write(i[0].strip() + ':' + i[1].strip() + '\n')
    elif alignment_data['seq_1_f']:
        with open('temp_seq_1.seqlib', 'w') as f:
            f.write(alignment_data['seq_1_f'].decode('utf-8'))
    command.append("--seqlib")
    command.append('temp_seq_1.seqlib')
    
    if alignment_data['seq_2']:
        with open('temp_seq_2.pairs', 'w') as f:
            for seq in alignment_data['seq_2'].split(','):
                i = seq.split(':')
                f.write(i[0].strip() + ' ' + i[1].strip() + '\n')
    elif alignment_data['seq_2_f']:
        with open('temp_seq_2.pairs', 'w') as f:
            f.write(alignment_data['seq_2_f'].decode('utf-8'))
    command.append("--pairs")
    command.append('temp_seq_2.pairs')

    if alignment_data['mat']:
        with open('temp_mat.mat', 'w') as f:
            f.write(alignment_data['mat'].decode('utf-8'))
        command.append("--m")
        command.append('temp_mat.mat') 
    
    mx_path='/home/h/hummelj/public_html/cgi-bin/api'
    command.append("--dpmatrices")
    command.append(mx_path)
 


    result = subprocess.run(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, universal_newlines=True)
    os.remove('temp_seq_1.seqlib')
    os.remove('temp_seq_2.pairs')

    if dwnl:
        if result.returncode == 0:
            dt = datetime.datetime.now()
            seq = int(dt.strftime("%Y%m%d%H%M%S"))
            res_outpath = f'{seq}-ali-validation_results.txt'
            with open(res_outpath, 'w') as f:
                f.write(result.stdout)
            res_outpath = f'../cgi-bin/api/{res_outpath}'
            mx_path = f'../cgi-bin/api/dpmatrices_result.txt'

            

            return {
                'success': True,
                'output': {'alignment results': res_outpath, 'dp matrices': mx_path},
            }
        else:
            return {
                'success': False,
                'error': result.stderr
            }
    else:

        if result.returncode == 0:
            return {
                'success': True,
                'output': result.stdout,
            }
        else:
            return {
                'success': False,
                'error': result.stderr
            }
    
def execute_validation_subprocess(val_data, dwnl):
    command = ["java", "-jar", "/mnt/biocluster/praktikum/bioprakt/progprakt-03/Solution3/NeedlemanWunschAli.jar"]
    with open('temp_val.txt', 'w') as f:
        f.write(val_data.decode('utf-8'))

    command.append("-f")
    command.append('temp_val.txt')
   

    result = subprocess.run(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, universal_newlines=True)
    os.remove('temp_val.txt')
    if dwnl:
        dt = datetime.datetime.now()
        seq = int(dt.strftime("%Y%m%d%H%M%S"))
        sum_outpath = f'{seq}-ali-validation_results.txt'
        with open(sum_outpath, 'w') as f:
            f.write(result.stdout)

        sum_outpath = f'../cgi-bin/api/{sum_outpath}'

        if result.returncode == 0:
            return {
            'success': True,
            'output': sum_outpath
        }
        else:
            return {
                'success': False,
                'error': result.stderr,
            }
    else:
        if result.returncode == 0:
            return {
                'success': True,
                'output': result.stdout,
            }
        else:
            return {
                'success': False,
                'error': result.stderr,
            }
    




def main():
    print("Content-Type: application/json\n")
    form = cgi.FieldStorage()
    

    
    try:
        mode = form.getvalue('mode')
        if mode == 'predict':
            seq_1 = None
            seq_2 = None
            seq_1_f = None  
            seq_2_f = None
            mat = None
            if form.getvalue('pdbId'):
                seq_1 = form.getvalue('pdbId')
            elif form.getvalue('pdbId_f'):
                seq_1_f = form['pdbId_f'].file.read()
            
            if form.getvalue('pdbId2'):
                seq_2 = form.getvalue('pdbId2')
            elif form.getvalue('pdbId2_f'):
                seq_2_f = form['pdbId2_f'].file.read()
            if form.getvalue('mat_f'):
                mat = form['mat_f'].file.read()

            go = form.getvalue('go')
            ge = form.getvalue('ge')
            global_align = form.getvalue('global')
            local_align = form.getvalue('local')
            freeshift_align = form.getvalue('freeshift')
            nw = form.getvalue('nw')
            gt = form.getvalue('gt')

        
            alignment_data = {
            'seq_1': seq_1,
            'seq_2': seq_2,
            'seq_1_f': seq_1_f,
            'seq_2_f': seq_2_f,
            'go': go,
            'ge': ge,
            'global_align': global_align,
            'local_align': local_align,
            'freeshift_align': freeshift_align,
            'nw': nw,
            'gt': gt,
            'mat': mat 
            }
            dwnl = form.getvalue('downl_f') == 'true'
            alignment = execute_subprocess(alignment_data, dwnl)
            
            result = {'data': alignment}
        elif mode == 'validate':
            vali_v = form['vali_f'].file.read()
            dwnl = form.getvalue('downl_v') == 'true'
            result = {'data': execute_validation_subprocess(vali_v, dwnl)}
            #result = {'data': os.path.dirname(os.path.realpath(__file__))}
    except Exception as e:
        result = {'error': str(e)}

    # Print JSON response
    print(json.dumps(result))

if __name__ == "__main__":
    main()


