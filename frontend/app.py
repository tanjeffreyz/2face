from flask import Flask
from flask import request
import os
import requests

BACKEND_HOST = os.getenv('BACKEND_HOST', 'http://localhost:8080')
# BACKEND_HOST = os.getenv('BACKEND_HOST', 'https://facial-recognition-backend.azurewebsites.net')
SERVER_PORT = os.getenv('SERVER_PORT', 8000)
NOSQL_HOST = os.getenv('NOSQL_HOST', 'http://localhost:8080')

# Create Flask app
app = Flask(__name__, static_url_path='/', static_folder='static')

@app.route('/')
def index():
    return app.send_static_file("index.html")

# Test endpoint, ignore
@app.route('/greeting', methods=['GET'])
def greeting():
    response = requests.get(BACKEND_HOST + '/greeting')
    return response.content

@app.route('/images', methods=['POST'])
def image():
    response = requests.post(BACKEND_HOST + '/images', data=request.data)
    return response.content

@app.route('/validate', methods=['POST'])
def validate():
    response = requests.post(BACKEND_HOST + '/validate', data=request.data)
    return response.content
    
@app.route('/form', methods=['POST'])
def form():
    response = requests.post(BACKEND_HOST + '/form', data=request.data)
    return response.content

@app.route('/users', methods=['POST'])
def form():
    response = requests.post(NOSQL_HOST + '/users', data=request.data)
    return response.content

if __name__ == '__main__': 
    app.run(host="0.0.0.0", port=SERVER_PORT, debug=True)
