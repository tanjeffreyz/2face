FROM python:3.8.2-alpine
WORKDIR /frontend
COPY . /frontend
RUN pip install -r requirements.txt
ENV SERVER_PORT 80
EXPOSE 80
CMD [ "python3", "app.py"]
