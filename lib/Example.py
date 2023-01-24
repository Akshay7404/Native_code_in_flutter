from flask import Flask, jsonify, request

app = Flask(__name__)

@app.route('/name',methodes = ['GET'])
def nameRoute():
    d = {}
    inputChar = str(request.args(['query']))
    answer = str(ord(inputChar))
    d['output'] = answer
    return d
   
          
if __name__ == "__manin__" :
    app.run(debug=True)
