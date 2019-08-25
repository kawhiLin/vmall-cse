FROM cse-jre:8u111

WORKDIR /home/apps/

ENV JRE_HOME=$JAVA_HOME
ENV CLASSPATH=.:${JRE_HOME}/lib \
    PATH=$JRE_HOME/bin:$PATH
ADD target/*.jar ./
ADD start.sh . 
  
ENTRYPOINT ["sh", "/home/apps/start.sh"]
