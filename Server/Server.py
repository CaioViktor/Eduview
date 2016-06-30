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

connection_string = "host='localhost' dbname='eduviewBD' user='postgres' password='014526'"
conn = psycopg2.connect(connection_string)


#class usuarios(Resource):
#    def get(self, usuario):
#        test = coll.find_one({'username': usuario}, {'_id': 0})
#        if test: 
#        	return {'codigo' : 0 ,"mensagem" : test}
#        else:
#        	return {'codigo': 1,'mensagem':'Usuario nao existente'}

#    def post(self, usuario):
#        data = request.get_json()
#        test = coll.find_one({'username': usuario})
#        if not test:
#        	coll.insert_one(data)
#        	return {'codigo' : 0 ,"mensagem" : test}
#        else:
#        	return {'codigo':1,'mensagem': 'Usuario ja existe'}

#    def put(self, usuario):
#    	data = request.get_json()
#    	data = dict(data)
#    	coll.update_one({'username': usuario}, {'$set': data})
#    	return {'codigo' : 0 ,'mensagem' : 'Usuario atualizado'}



#api.add_resource(usuarios, '/palinha/<string:usuario>', endpoint = 'usuario_endpoint')
#api.add_resource(musicas, '/palinha/<string:usuario>/<string:musicapai>', endpoint = 'musica_endpoint')
#api.add_resource(posts, '/palinha/posts/<string:usuario>/<string:nomemusica>', endpoint = 'post_endpoint')
#api.add_resource(follow, '/palinha/seguir/<string:usuario>/<string:following>', endpoint = 'seguir_endpoint')
#api.add_resource(feed, '/palinha/feed/<string:usuario>', endpoint = 'feed_endpoint')

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
	def get(self,campo,comparador,valor,deslocamento):
		valor = "'"+valor+"'"
		campo = 'e.'+campo
		if(comparador.lower() == 'like'):
			campo = 'UPPER('+campo+')'
			valor = 'UPPER('+valor+')'
		cursor = getCursor()
		sql = 'SELECT e.*,AVG(a.avaliacao) AS avaliacao_media FROM public."Escola" e LEFT OUTER JOIN public."Avaliacao" a ON a.id_escola = e.pk_escola WHERE ' + campo + " " +comparador + " " +valor+" GROUP BY  e.pk_escola ORDER BY e.pk_escola LIMIT 10 OFFSET " + deslocamento
		print sql
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
		sql = 'SELECT e.*,AVG(a.avaliacao) AS avaliacao_media FROM public."Escola" e LEFT OUTER JOIN public."Avaliacao" a ON a.id_escola = e.pk_escola WHERE latitude IS NOT NULL AND longitude IS NOT NULL GROUP BY e.pk_escola'
		cursor.execute(sql)
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
		

api.add_resource(escola,'/escola/<string:campo>/<string:comparador>/<string:valor>/<string:deslocamento>',endpoint='escola_endpoint')
api.add_resource(listEscola,'/listescola/<string:longitude>/<string:latitude>/<string:raio>/<int:maximo>',endpoint='listEscola_endpoint')

if __name__ == '__main__':
    app.run(host='0.0.0.0')
