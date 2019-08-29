# VMALL使用手册

## 说明

VMALL版本

- 微服务版本	基于CSE开发框架开发
- 容器版本	    传统容器化

平台版本

- FusionStage 6.5.0
- CSE Java SDK 6.5.22

当前支持演示以下功能：

- 服务目录
- 图形化编排
- 微服务
  - 注册发现
  - 服务治理
  - 灰度发布
- APM
  - 非侵入式调用链（需要安装ATPS）
  - 自定义监控
  - 弹性伸缩

- 镜像构建
- CICD

## 微服务版本

### 准备

#### 前提

- 环境已安装微服务
- MariaDB
  - 已发布mariadb到服务目录（服务名为mariadb），并订购一个服务实例（实例名称为mariadb-vmall，密码为123456）

#### 部署材料

已获取demo材料 vmall-cse

#### 部署参数收集

- CONFIGIP  微服务注册中心地址
- AK/SK
- PROJECT
- MYSQLVMALL_SERVICE_HOST  订购的mariadb实例的连接地址IP
- MYSQLVMALL_SERVICE_PORT  订购的mariadb实例的连接地址PORT

- image_xxx  改为实际环境的中的镜像地址

- metrics_value  保持默认

### 堆栈部署

#### 上传镜像

上传各个模块镜像，共9个

#### 模板部署

在设计器页面导入部署模板，在输入输出页面修改参数默认值（根据前面已收集的），一键部署

#### 验证

- 查看微服务是否测试注册
- 访问web应用的服务页面是否显示正常

### 镜像构建

1、在source2image文件夹中，上传basic-images中两个基础镜像

2、解压各个模块的zip包，分别修改其中Dockfile中的``FROME xxxx``将xxxx替换为实际环境中的镜像地址。其中initdb模块的基础镜像为python镜像，其余为jre镜像

3、在各个模块的文件夹内，选中全部文件（Dockerfile、star.sh·····）后右击添加为zip包

4、将各个模块的zip上传到软件仓库（软件仓库需要为私有，建议全部放到一个仓库中）

5、创建二进制构建任务，各个模块的构建镜像源选择到对应的zip包

### CICD

待补充

## 容器版本

### 准备

#### 前提

- MariaDB
  - 已发布mariadb到服务目录（服务名为mariadb），并订购一个服务实例（实例名称为mariadb-vmall，密码为123456）

#### 部署材料

已获取demo材料 vmall-container

#### 部署参数收集

- MYSQLVMALL_SERVICE_HOST  订购的mariadb实例的连接地址IP
- MYSQLVMALL_SERVICE_PORT  订购的mariadb实例的连接地址PORT

- image_xxx  改为实际环境的中的镜像地址

- metrics_value  保持默认

### 堆栈部署

#### 上传镜像

上传各个模块镜像，共8个

#### 模板部署

在设计器页面导入部署模板，在输入输出页面修改参数默认值（根据前面已收集的），一键部署

#### 验证

- 查看微服务是否测试注册
- 访问web应用的服务页面是否显示正常

### 镜像构建

1、在source2image文件夹中，上传basic-images中两个基础镜像

2、解压各个模块的zip包，分别修改其中Dockfile中的``FROME xxxx``将xxxx替换为实际环境中的镜像地址。其中initdb模块的基础镜像为python镜像，其余为jre镜像

3、在各个模块的文件夹内，选中全部文件（Dockerfile、star.sh·····）后右击添加为zip包

4、将各个模块的zip上传到软件仓库（软件仓库需要为私有，建议全部放到一个仓库中）

5、创建二进制构建任务，各个模块的构建镜像源选择到对应的zip包

### CICD

待补充



## 常见问题

- 堆栈部署失败，mariadb报错

  确认是否按要求：已发布mariadb到服务目录（服务名为mariadb），并订购一个服务实例（实例名称为mariadb-vmall，密码为123456）

  若环境命名有冲突，需要修改部署模板：删除右侧mariadb节点，从左侧拖入实际环境中的mariadb服务，并填写相关参数：instancename、parameter（两个订购参数）

- 微服务注册不上

  检查AK、SK、CONFIGIP、PROJECT是否填写正确

  查看在运维页面查看应用日志进行定位

- vmall页面打开后，提示查询错误

  其他模块尚未完成初始化，或者数据库连接or初始化异常

  可进入mariadb终端，通过``mysql -uroot -p123456``连接数据库，并输入``show databases;``查看是否已创建shopping实例库

  