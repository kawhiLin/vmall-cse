# VMALL使用说明

## 说明

### 版本

#### 手册版本

- v0.2

#### VMALL版本

- 微服务2.2.1版本	基于CSE开发框架开发
- 容器2.0版本	    传统容器化

#### 平台版本

- FusionStage 6.5.0
- CSE Java SDK 6.5.22

### 功能

当前支持演示以下功能：

- 服务目录

- 图形化编排

- 微服务

  - 注册发现
  - 服务治理（限流、负载均衡、降级）
  - 灰度发布（按流量、自定义请求参数）

- APM

  - 非侵入式调用链（需要安装ATPS）
  - 自定义监控
  - 弹性伸缩

- 镜像构建

- CICD

- 模拟业务压测

- 模拟故障

  - 健康检查
  - 故障自愈
  - 告警触发

  - 故障分析

## 微服务版本

### 准备

#### 前提

- 环境已安装微服务
- MariaDB
  - 已发布mariadb到服务目录（服务名为mariadb），并订购一个服务实例（实例名称为mariadb-vmall，密码为123456）
- 资源要求：至少两个4U8G节点

#### 部署材料

已获取demo材料 vmall-cse

#### 部署参数收集

- CONFIGIP  微服务注册中心地址
- AK/SK
- PROJECT
- mariadb_instance_host  订购的mariadb实例的连接地址IP
- mariadb_instance_port  订购的mariadb实例的连接地址PORT
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

### VMALL控制台

#### 模拟抢购

> 场景说明：
>
> 模拟业务高压情况下，应用据自定义的策略，进行实例个数的自动伸缩。

1、使用admin登录VMALL，从右上角进入控制台页面，输入请求数(0~100)，开始抢购。

- 监控
  - 在微服务治理页面，查看order、product实时的吞吐量，其中product的吞吐量约为order的两倍（每次请求，order内部会调用两次product）
  - 在自定义指标监控页面，查看order的tps指标
  - 在应用监控页面，查看web、order、product的CPU、内存使用率的变化

2、弹性伸缩

​	可以针对order的自定义监控指标`` vmall_order_tps``制定伸缩策略

3、在控制台页面点击停止发送请求

#### 模拟故障

> 场景说明：
>
> 模拟业务出现故障之后，将触发告警；利用故障分析，快速定位错误。

1、给order应用设置健康检查-应用业务探针

参数填写：

- 延迟检查时间 10
- 超时时间 1
- HTTP GET请求方式
  - 路径 /order/healthCheck
  - 端口 8084

2、使用admin登录VMALL，右上角进入控制台页面，输入order实例IP（在order应用详情页面，可查看order实例IP），点击触发，模拟故障。

- 效果
  - 间隔一段时间，即可看到order实例处于异常状态。点击实例的事件，可以看到健康检查unhealthy事件
  - 在VMALL页面，从右上角访问订单处理/订单状态，提示查询错误，订单业务处于故障状态

3、告警：在告警列表中出现POD状态异常的告警

4、故障分析

添加故障规则，参数填写：

- 服务 自定义，如ORDER
- 镜像 order
- 故障名 自定义，如oder_error01
- 文件 stdout.log
- 故障详情 Order Not Found

- 修复建议 建议重启order

 完成创建之后，选择order应用进行故障分析，可进行错误定位。

5、在控制台点击故障恢复

> 补充：
>
> 若探针类型设置为应用存活探针，当健康检查失败时，将会进行自动重启，故障自愈。

### 灰度发布

> 场景说明：
>
> VMALL的新模块“新品推荐”存在两个版本，欲针对不同用户实现灰度发布。

##### 前提

- 已上传newArrivals两个版本镜像
- 已获取部署模板newarrivals-template.tar.gz

##### 上线v1.0版本

使用模板部署newArrivals，部署参考“堆栈部署”章节，container_image参数填写newArrivals V1.0版本镜像地址。

部署完成之后，admin登陆vmall，首页多出“新品推荐“页面。

##### 制定灰度规则

在微服务目录页面，针对newArrivals添加发布规则，选择自定义规则

参数：

- 作用域 添加自定义版本2.0.0，并勾选2.0.0
- 规则配置
  - 参数名 username
  - 规则 ~hz.*

> 操作符选择``~``，规则内容填``hz.*``，表示以hz开头的用户将会访问2.0.0版本的newArrivals

##### 上线v2.0版本

使用模板部署newArrivals2，部署参考“堆栈部署”章节，container_image参数填写newArrivals V2.0版本镜像地址。

##### 验证灰度发布

1、注册以hz开头的用户，如hz_user01

2、登录该用户，可以看到首页的“新品推荐”显示的版本与admin用户不同。

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

## 其他





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

  