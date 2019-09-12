FROM 192.168.69.130:20202/vmall/cse-huawei-jre:8u201

WORKDIR /home/apps/

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENV JRE_HOME=$JAVA_HOME
ENV CLASSPATH=.:${JRE_HOME}/lib \
    PATH=$JRE_HOME/bin:$PATH

ADD target/*.jar ./
ADD exporter ./
RUN chmod -R 777 exporter

ADD start.sh .

ENTRYPOINT ["sh", "/home/apps/start.sh"]
