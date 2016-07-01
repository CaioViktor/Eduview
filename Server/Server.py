#!/usr/bin/python
# -*- coding: UTF-8 -*-

import os.path
import os
import sys
import psycopg2
import psycopg2.extras

from flask import Flask, jsonify, url_for, redirect, request, send_from_directory
from flask_restful import Resource, Api
from bson.objectid import ObjectId

from math import radians, cos, sin, asin, sqrt


def haversine(lon1, lat1, lon2, lat2):
    """
    Calculate the great circle distance between two points 
    on the earth (specified in decimal degrees)
    """
    # convert decimal degrees to radians 
    lon1, lat1, lon2, lat2 = map(radians, [lon1, lat1, lon2, lat2])
    # haversine formula 
    dlon = lon2 - lon1 
    dlat = lat2 - lat1 
    a = sin(dlat/2)**2 + cos(lat1) * cos(lat2) * sin(dlon/2)**2
    c = 2 * asin(sqrt(a)) 
    km = 6367 * c
    return km
    
def getKey(item):
		return item[0]
   

app = Flask(__name__)
app.config['MAX_CONTENT_LENGTH'] = 16 * 1024 * 1024
api = Api(app)

connection_string = "host='localhost' dbname='teste' user='postgres' password='014526'"
conn = psycopg2.connect(connection_string)


def getCursor():
	cursor = conn.cursor('cursor_unique_name', cursor_factory=psycopg2.extras.DictCursor)
	return cursor
	
def closeCursor(cursor):
	cursor.close()
	
def getEscola(cursor):
	resultado = {}
	resultado['nome'] = cursor[0]
	resultado['rua'] = cursor[1]
	resultado['bairro'] = cursor[2]
	resultado['numero'] = cursor[3]
	resultado['ddd'] = cursor[4]
	resultado['telefone'] = cursor[5]
	resultado['email'] = cursor[6]
	resultado['situacao'] = cursor[7]
	resultado['predio_proprio'] = cursor[8]
	resultado['acessibilidade'] = cursor[9]
	resultado['rede'] = cursor[10]
	resultado['atendimento_especializado'] = cursor[11]
	resultado['refeitorio'] = cursor[12]
	resultado['auditorio'] = cursor[13]
	resultado['laboratorio_informatica'] = cursor[14]
	resultado['laboratorio_ciencias'] = cursor[15]
	resultado['quadra_coberta'] = cursor[16]
	resultado['quadra_descoberta'] = cursor[17]
	resultado['patio_coberto'] = cursor[18]
	resultado['patio_descoberto'] = cursor[19]
	resultado['parque_infantil'] = cursor[20]
	resultado['biblioteca'] = cursor[21]
	resultado['numero_salas'] = cursor[22]
	resultado['alimentacao'] = cursor[23]
	resultado['agua'] = cursor[24]
	resultado['energia'] = cursor[25]
	resultado['internet'] = cursor[26]
	resultado['quantidade_computadores'] = cursor[27]
	resultado['pk_escola'] = cursor[28]
	resultado['cep'] = cursor[29]
	resultado['latitude'] = cursor[30]
	resultado['longitude'] = cursor[31]
	resultado['avaliacao'] = str(cursor[32])
	return resultado
	
class escola(Resource):
	def get(self,orderby,limite,deslocamento):
		data = request.get_json()
		where = ''
		if data:
			data = dict(data)
			where = data['where']
		cursor = getCursor()
		sql = 'SELECT e.*,AVG(a.avaliacao) AS avaliacao_media FROM public."Escola" e LEFT OUTER JOIN public."Avaliacao" a ON a.id_escola = e.pk_escola '+ where + " GROUP BY e.pk_escola ORDER BY " + orderby +" LIMIT "+limite+" OFFSET " + deslocamento
		#print sql
		cursor.execute(sql)
		contador = 0
		resultado = {'contador':0}
		for linha in cursor:
			contador+=1
			resultado['contador'] = contador
			resultado[contador] = getEscola(linha)
		closeCursor(cursor)
		return resultado
		
class listEscola(Resource):	
	def get(self,longitude,latitude,raio,maximo):
		cursor = getCursor()
		data = request.get_json()
		where = ''
		if data:
			data = dict(data)
			where = ' AND '+ data['where']
		sql = 'SELECT e.*,AVG(a.avaliacao) AS avaliacao_media FROM public."Escola" e LEFT OUTER JOIN public."Avaliacao" a ON a.id_escola = e.pk_escola WHERE latitude IS NOT NULL AND longitude IS NOT NULL '+ where +' GROUP BY e.pk_escola'
		#print sql
		try:
			cursor.execute(sql)
		except:
			conn.rollback()
			return {'codigo':1}
		contador = 0
		resultado = {'contador':0}
		itens = []
		for linha in cursor:
			escola = getEscola(linha)
			long1 = float(str(escola['longitude']).replace(",","."))
			lat1 = float(str(escola['latitude']).replace(",","."))
			long2 = float(str(longitude).replace(",","."))
			lat2 = float(str(latitude).replace(",","."))
			distancia  = haversine(long1,lat1,long2,lat2)
			raio = float(str(raio).replace(",","."))
			if (distancia <= raio):
				elemento = [distancia,escola]
				itens.append(elemento)
		sorted(itens,key=getKey)
		for item in itens:
			escola = item[1]
			contador+=1
			resultado['contador'] = contador
			resultado[contador] = escola
			if(contador >= maximo):
				closeCursor(cursor)
				return resultado
				
		closeCursor(cursor)
		return resultado	

class sql(Resource):
	def get(self):
		data = request.get_json()
		data = dict(data)
		sql = data['sql']
		cursor = getCursor()
		cursor.execute(sql)
		contador = 0
		resultado = {'contador':0}
		for linha in cursor:
			contador+=1
			resultado['contador'] = contador
			resultado[contador] = dict(linha)
		closeCursor(cursor)
		return resultado
		

def getUsuario(usuario):
	resultado = {}
	resultado['id_usuario'] = usuario[0]
	resultado['username'] = usuario[1]
	resultado['password'] = usuario[2]
	resultado['nome'] = usuario[3]
	resultado['email'] = usuario[4]
	return resultado
	
class usuarios(Resource):
    def get(self, usuario):
        cursor = getCursor()
        sql = 'SELECT * FROM public."Usuario" WHERE username = ' +"'" + usuario+"'"
        cursor.execute(sql)
	contador = 0
	resultado = {'contador':0}
	for linha in cursor:
		contador+=1
		resultado['contador'] = contador
		resultado[contador] = getUsuario(linha)
	closeCursor(cursor)
	return resultado
        
    def post(self, usuario):
        data = request.get_json()
        usuario = dict(data)
        sql = 'INSERT INTO public."Usuario" (username,password,nome,email) VALUES('+"'"+usuario['username']+"','"+usuario['password']+"','"+usuario['nome']+"','"+usuario['email']+"')"
        cursor = conn.cursor()
        try:
       	        cursor.execute(sql)
	        conn.commit()
        except:
        	conn.rollback()	
        	return {'codigo':1}
        closeCursor(cursor)
        return {'codigo':0}

    



api.add_resource(usuarios, '/usuario/<string:usuario>', endpoint = 'usuario_endpoint')
api.add_resource(escola,'/escola/<string:orderby>/<string:limite>/<string:deslocamento>',endpoint='escola_endpoint')
api.add_resource(listEscola,'/listescola/<string:longitude>/<string:latitude>/<string:raio>/<int:maximo>',endpoint='listEscola_endpoint')
api.add_resource(sql,'/sql',endpoint='sql_endpoint')

if __name__ == '__main__':
    app.run(host='0.0.0.0')
