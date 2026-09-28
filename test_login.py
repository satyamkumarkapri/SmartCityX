import requests
from bs4 import BeautifulSoup

session = requests.Session()
response = session.get('http://localhost:8080/login')
soup = BeautifulSoup(response.text, 'html.parser')
csrf_token = soup.find('input', {'name': '_csrf'})['value']

login_data = {
    'username': 'admin',
    'password': 'admin123',
    '_csrf': csrf_token
}

post_response = session.post('http://localhost:8080/login', data=login_data)
print("Status Code:", post_response.status_code)
print("URL:", post_response.url)
