# webverify2: Integrity monitor for websites

With this project, we give everyone a free and reliable tool for monitoring websites to ensure its authenticity. A defacement attack can greatly affect the image of any company so it is important to detect it quickly. webverify2 is built in java with mysql so it is compatible with any operating system that supports these technologies.

If you like this project you can support us on patreon: https://www.patreon.com/zeusafk

![Alt text](http://i.imgur.com/rszxXE8.png "webverify2 console output")

![Alt text](http://i.imgur.com/mtjMHAv.png "Active scan schedules list")

# License
Copyright 2016 ZeusAFK

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


# Funcionalidades en planeacion y desarrollo

Verificacion de estado en linea
	Estado servicio http en intervalos configurables
	Registro de eventos (subida, bajada del servicio)
	Registro de tiempos de respuesta promedio

Alertas phishing
	Codigo de seguridad anti-phishing
	Reportes de eventos

Nuevos modos de operacion
	
	Remoto HTTP
		Se verifican los archivos mediante peticiones http

	Remoto FTP, SFTP, FTPS
		Se verifican los archivos accediendo a travez de un servicio FTP, require acceso a un servidor FTP en el servidor con una cuenta valida y permisos de lectura
		
	* Protocolos FTP seguros requieren configuracion adicional o SSH habilitado

	Local sistema de archivos
		Se verifica los archivos directamente en sistema de archivos del servidor, requiere que la aplicacion funcione en el mismo servidor y permisos de lectura en el directorio raiz de la web

Acciones caso de que ocurra un evento en la verificacion de archivos
	Archivo nuevo: Se elimina el archivo nuevo no identificado
	Archivo modificado: Se restaura el archivo original desde la base de datos
	Archivo eliminado: Se restaura el archivo original desde la base de datos

	Requisitos segun Modos de operacion:
		Remoto HTTP
			No soportado

		Remoto FTP, SFTP, FTPS
			Se requiere permisos de escritura en el servidor FTP

		Local sistema de archivos
			Se requiere permisos de escritura en el directorio raiz de la web

Verificacion de integridad de archivos en sistema de archivos completo
	Verificacion de integridad en el servidor completo

	Pero...:
		Requiere que la aplicacion funcione en el mismo servidor
		Acceso de lectura a todo el sistema de archivos
		Requiere bastantes recursos de hardware
		Puede demorar bastante tiempo dependiendo de la cantidad de archivos en el servidor por tanto no debe realizarse en intervalos cortos

Verificacion en tiempo real de sistema de archivos
	Deteccion de cambios en archivos y directorios especificos
	Bloqueo de modificaciones en archivos y directorios especificos

	Pero...:
		Se recomienda solo para archivos criticos del sistema, EJ: archivo host
		Requirere ROOT o Administrador
