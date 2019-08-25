#!/bin/bash 
  
CMDVAR="-Dcse.service.registry.address=https://$CONFIGIP:30100 -Dcse.config.client.serverUri=https://$CONFIGIP:30103 -Dcse.monitor.client.serverUri=https://$CONFIGIP:30109 -Dcse.credentials.accessKey=$AK -Dcse.credentials.secretKey=$SK -Dcse.credentials.project=$PROJECT" 
java $CMDVAR -jar ./*.jar

