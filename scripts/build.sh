#!/bin/sh
mvn	-DskipTests				\
	-Dmaven.wagon.http.ssl.insecure=true	\
	-Dmaven.wagon.http.ssl.allowall=true	\
	-Dmaven.wagon.http.ssl.ignore.validity.dates=true	\
    clean install