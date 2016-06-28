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
	return resultado
	
class escola(Resource):
	def get(self,campo,comparador,valor,deslocamento):
		cursor = getCursor()
		sql = 'SELECT * FROM public."Escola" WHERE ' + campo + " " +comparador + " '" +valor+"' ORDER BY nome LIMIT 10 OFFSET " + deslocamento
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
	def get(self,posicao,raio,limit,deslocamento):

		return		
			

api.add_resource(escola,'/escola/<string:campo>/<string:comparador>/<string:valor>/<string:deslocamento>',endpoint='escola_endpoint')
#api.add_resource(escola,'/escola/<string:campo>/<string:comparador>/<string:valor>/<string:deslocamento>',endpoint='escola_endpoint')

if __name__ == '__main__':
    app.run(host='0.0.0.0')
