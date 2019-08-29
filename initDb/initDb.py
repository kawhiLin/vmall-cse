from time import sleep

import pymysql
import os

# 获取db的环境变量
host = os.getenv("MYSQLVMALL_SERVICE_HOST") or "192.170.113.208"
port = os.getenv("MYSQLVMALL_SERVICE_PORT") or 30862

# print日志同步到stdout TODO 尚未解决docker logs无日志问题
os.environ["PYTHONUNBUFFERED"] = "0"

print(host, port)

while 1:
    # 打开数据库连接
    try:
        db = pymysql.connect(host=host, port=init(port), user='root', passwd='123456', db='mysql')
        break
    except Exception as e:
        print(e, "connect db failed")
        sleep(1)
        continue

# 使用 cursor() 方法创建一个游标对象 cursor
cursor = db.cursor()

# 读取SQL文件,获得sql语句的list
with open(u'vmall.sql', 'r+', encoding='utf-8') as f:
    sql_list = f.read().split(';')[:-1]  # sql文件最后一行加上;
    sql_list = [x.replace('\n', ' ') if '\n' in x else x for x in sql_list]  # 将每段sql里的换行符改成空格
    # 执行sql语句，使用循环执行sql语句
    for sql_item in sql_list:
        print(sql_item)
        try:
            cursor.execute(sql_item)
            db.commit()
        except cursor.Error as e:
            print(e, "\n init db failed")
            db.close()
            exit()

# 不同格式语句单独处理
with open(u'vmall2.sql', 'r+', encoding='utf-8') as f:
    for sql_item in f.readlines():
        sql_item = sql_item.replace(";\n", "")
        print(sql_item)
        try:
            cursor.execute(sql_item)
            db.commit()
        except cursor.Error as e:
            print(e, "\n init db failed")
            db.close()
            exit()

# 关闭数据库连接
db.close()
print("init db succeed")
