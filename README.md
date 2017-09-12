##源码一览（source）
###结构说明
src/
├── com/
│&nbsp;├── ssc/
│&nbsp;&nbsp;&nbsp;├── controller/
│&nbsp;&nbsp;&nbsp;├── dao/
│&nbsp;&nbsp;&nbsp;├── entity/
│&nbsp;&nbsp;&nbsp;├── filter/
│&nbsp;&nbsp;&nbsp;├── interceptor/
│&nbsp;&nbsp;&nbsp;├── listener/
│&nbsp;&nbsp;&nbsp;├── quartz/
│&nbsp;&nbsp;&nbsp;├── resolver/
│&nbsp;&nbsp;&nbsp;├── service/
│&nbsp;&nbsp;&nbsp;└── util/
├── mybatis/
├── PluSoft/
└── spring/

------
1.com.ssc中的包分别对应：
-**控制器**
-**dao层**
-**实体类**
-**过滤器**
-**拦截器**
-**监听器**
-**定时任务（暂存）**
-**service层**
-**工具**

2.mybatis中存放的是数据库配置文件，表结构改动sql的记录，以及mybatis业务文件。
3.PluSoft 暂存数据库工具类。
4.spring中是项目的框架配置。

其他的文件为一些公共字典和日志配置，不用改动。
>**注：**其中`dbconfig.properties`文件为连接数据库的参数配置文件，部署到服务器之前需改动其中的ip和数据库名。

---------------
