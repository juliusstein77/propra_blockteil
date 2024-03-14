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

def clean_json(data):
  return re.sub(r'^[^\"]*\"', '"', data)

def execute_train_subprocess(train_gor_data):
    command = ["java", "-jar", "/home/h/hummelj/propra/gor/train.jar"]
    if train_gor_data['train_f']:
        with open('temp_train.fasta', 'w') as f:
            f.write(train_gor_data['train_f'].decode('utf-8'))
        train = 'temp_train.fasta'
    else:
        train = '/home/h/hummelj/propra/gor/training/train_1.txt'
    command.append("--db")
    command.append(train)
    
    if train_gor_data['gor_1'] == "true":
        version = "gor1"
    elif train_gor_data['gor_3'] == "true":
        version = "gor3"
    elif train_gor_data['gor_4'] == "true":
        version = "gor4"
    command.append("--method")
    command.append(version)
    
    
    dt = datetime.datetime.now()
    seq = int(dt.strftime("%Y%m%d%H%M%S"))
    outpath = f'{seq}-{version}.mod'
    command.append("--model")
    command.append(outpath)
    subprocess.run(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, universal_newlines=True)
  
    if train_gor_data['train_f']:
        os.remove('temp_train.fasta')

    outpath = f'../cgi-bin/api/{outpath}'
    return {
        'success': True,
        'output': outpath
    }
    
    


def execute_prediction_subprocess(gor_data):
    command = ["java", "-jar", "/home/h/hummelj/propra/gor/predict.jar"]
    if gor_data['mod_f']:
        with open('temp_mod.mod', 'w') as f:
            f.write(gor_data['mod_f'].decode('utf-8'))
        
        model = 'temp_mod.mod'
    else:
        model = '/home/h/hummelj/propra/gor/gor1cb513dssp.mod'
    command.append("--model")
    command.append(model)

    with open('temp_pred.fasta', 'w') as f:
        f.write(gor_data['pred_f'].decode('utf-8'))
    command.append("--seq")
    command.append('temp_pred.fasta')
    command.append("--format")
    command.append("txt")
    command.append("--probabilities")
    result = subprocess.run(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, universal_newlines=True)  
    os.remove('temp_pred.fasta')
    if gor_data['mod_f']:
        os.remove('temp_mod.mod')
    
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

def execute_validation_subprocess(val_data):
    command = ["java", "-jar", "/home/h/hummelj/propra/gor/evalGor.jar"]

    
    with open('temp_ref.db', 'w') as f:
        f.write(val_data['ref_v'].decode('utf-8'))

    command.append("-r")
    command.append('temp_ref.db')

    with open('temp_pred.prd', 'w') as f:
        f.write(val_data['pred_v'].decode('utf-8'))
    
    command.append("-p")
    command.append('temp_pred.prd')	
    dt = datetime.datetime.now()
    seq = int(dt.strftime("%Y%m%d%H%M%S"))
    sum_outpath = f'{seq}-gor-summary.txt'
    command.append("-s")
    command.append(f'{sum_outpath}')
    dt_sum_outpath = f'{seq}-gor-detailed-summary.txt'
    command.append("-d")
    command.append(f'{dt_sum_outpath}')
    command.append("-f")
    command.append("txt")
    subprocess.run(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, universal_newlines=True)  
    os.remove('temp_pred.prd')
    os.remove('temp_ref.db')
    sum_outpath = f'../cgi-bin/api/{sum_outpath}'
    dt_sum_outpath = f'../cgi-bin/api/{dt_sum_outpath}'

    return {
            'success': True,
            'output': {'summary': sum_outpath, 'detailed summary': dt_sum_outpath}
        }

def execute_analytics_subprocess(ana_data):
    command = ["/home/h/hummelj/propra/gor/similarity/sequenceSimilarity.py"]
    with open('temp_ana_ref.db', 'w') as f:
        f.write(ana_data['ref_a'].decode('utf-8'))
    command.append("-d")
    command.append('temp_ana_ref.db')
    command.append("-t")
    command.append(ana_data['threshold'])
    splt = ana_data['threshold'].split('.')
    vs = splt[0] + '_'+ splt[1]
    f_plot_outpath = f'filter_{vs}'
    command.append("-filtered_png")
    command.append(f_plot_outpath)
    no_f_plot_outpath = f'no_filter_{vs}'
    command.append("-no_filter_png")
    command.append(f_plot_outpath)
    f_data_outpath = f'filter_data_{vs}.db'
    command.append("-filtered_db")
    command.append(f_data_outpath)
    result = subprocess.run(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, universal_newlines=True)
    os.remove('temp_ana_ref.db')
    f_plot_outpath = f'../public/{f_plot_outpath}.png'
    no_f_plot_outpath = f'../public/{no_f_plot_outpath}.png'
    f_data_outpath = f'../public/{f_data_outpath}'
    return {
            'success': True,
            'output': {'filtered plot': f_plot_outpath, 'non-filtered plot': no_f_plot_outpath, 'filtered data': f_data_outpath}
        }




def execute_train_predict_subprocess(train_pred_gor_data):
    command = ["java", "-jar", "/home/h/hummelj/propra/gor/train/JARS/trainPredNew.jar"]
    if train_pred_gor_data['train_f']:
        with open('temp_train.fasta', 'w') as f:
            f.write(train_pred_gor_data['train_f'].decode('utf-8'))
        train = 'temp_train.fasta'
    else:
        train = '/home/h/hummelj/propra/gor/training/train_1.txt'
    command.append("--db")
    command.append(train)

    with open('temp_pred.fasta', 'w') as f:
        f.write(train_pred_gor_data['pred_f'].decode('utf-8'))
    command.append("--seq")
    command.append('temp_pred.fasta')

    command.append("--format")
    command.append("txt")




    
    if train_pred_gor_data['gor_1'] == "true":
        version = "gor1"
    elif train_pred_gor_data['gor_3'] == "true":
        version = "gor3"
    elif train_pred_gor_data['gor_4'] == "true":
        version = "gor4"
    command.append("--method")
    command.append(version)
    dt = datetime.datetime.now()
    seq = int(dt.strftime("%Y%m%d%H%M%S"))
    v_num = version[-1]
    model_name = f'{seq}_gor_{v_num}_w_{v_num}.mod'
    command.append("--model")
    command.append(f'models/{model_name}')
    command.append("--modelT")
    command.append(f'models/{model_name}')
    
    if train_pred_gor_data['w'] != "":
        command.append("--w")
        command.append(train_pred_gor_data['w'])

    command.append("--out")
    command.append(f'predictions/{seq}_gor_{v_num}_w_{v_num}.prd')
    result = subprocess.run(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, universal_newlines=True)
  
    if train_pred_gor_data['train_f']:
        os.remove('temp_train.fasta')
    os.remove('temp_pred.fasta')
    outpath = f'../cgi-bin/api/predictions/{seq}_gor_{v_num}_w_{v_num}.prd'

    if result.returncode == 0:
        return {
        'success': True,
        'output': outpath,
        'command': command
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
        if mode == 'predict':
            default_model = form.getvalue('default_model')
            if not default_model:
                mod_f = form['mod_f'].file.read()
            else:
                mod_f = None
            gor_data = {
                'pred_f': form['pred_f'].file.read(),
                'mod_f': mod_f
            }
            result = {'data': execute_prediction_subprocess(gor_data)}
        elif mode == 'train':
            default_data = form.getvalue('default_data')
            if not default_data:
                train_f = form['train_f'].file.read()
            else:
                train_f = None
            gor_data = {
                'train_f': train_f,
                'gor_1': form.getvalue('gor_1'),
                'gor_3': form.getvalue('gor_3'),
                'gor_4': form.getvalue('gor_4'),
            }
            result = {'data': execute_train_subprocess(gor_data)}
        elif mode == 'train_predict':
            default_data = form.getvalue('default_data')
            if not default_data:
                train_f = form['train_f'].file.read()
            else:
                train_f = None
            gor_data = {
                'train_f': train_f,
                'pred_f': form['pred_f'].file.read(),
                'gor_1': form.getvalue('gor_1'),
                'gor_3': form.getvalue('gor_3'),
                'gor_4': form.getvalue('gor_4'),
                'w': form.getvalue('w'),
            }
            result = {'data': execute_train_predict_subprocess(gor_data)}
            #result = {'data': {'w': form.getvalue('w'), 'gor_1': form.getvalue('gor_1'), 'gor_3': form.getvalue('gor_3'), 'gor_4': form.getvalue('gor_4')}}
        elif mode == 'validate':
            val_data = {
                'pred_v': form['pred_v'].file.read(),
                'ref_v': form['ref_v'].file.read(),
            }
            result = {'data': execute_validation_subprocess(val_data)}
        elif mode == 'analyze':
            ana_data = {
                'threshold': form.getvalue('th_a'),
                'ref_a': form['ref_a'].file.read(),
            }
            result = {'data': execute_analytics_subprocess(ana_data)}
    except Exception as e:
        result = {'error': str(e)}

    # Print JSON response
    print(json.dumps(result))

if __name__ == "__main__":
    main()