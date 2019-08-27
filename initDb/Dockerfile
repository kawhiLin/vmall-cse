FROM python:3.6.4

#代码添加到code文件夹
ADD ./ /code
RUN chown -R root:root /code/* &&  mv /code/venv/Lib/site-packages/pymysql /usr/local/lib/python3.6/site-packages && mv /code/venv/Lib/site-packages/PyMySQL-0.9.3.dist-info /usr/local/lib/python3.6/site-packages
# 设置code文件夹是工作目录
WORKDIR /code

CMD ["python", "/code/initDb.py"]