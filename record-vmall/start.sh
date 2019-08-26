#!/bin/bash 

# 后台启动exporter
nohup ./exporter > exporter.logs 2>&1 &


CMDVAR="-Dcse.service.registry.address=https://$CONFIGIP:30100 -Dcse.config.client.serverUri=https://$CONFIGIP:30103 -Dcse.monitor.client.serverUri=https://$CONFIGIP:30109 -Dcse.credentials.accessKey=$AK -Dcse.credentials.secretKey=$SK -Dcse.credentials.project=$PROJECT" 
java $CMDVAR -jar ./*.jar

