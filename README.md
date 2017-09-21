## 源码一览（source）
### 结构
> src/  
>├── com/  
>│&nbsp;├── ssc/  
>│&nbsp;&nbsp;&nbsp;&nbsp;├── controller/  
>│&nbsp;&nbsp;&nbsp;&nbsp;├── dao/  
>│&nbsp;&nbsp;&nbsp;&nbsp;├── entity/  
>│&nbsp;&nbsp;&nbsp;&nbsp;├── filter/  
>│&nbsp;&nbsp;&nbsp;&nbsp;├── interceptor/  
>│&nbsp;&nbsp;&nbsp;&nbsp;├── listener/  
>│&nbsp;&nbsp;&nbsp;&nbsp;├── quartz/  
>│&nbsp;&nbsp;&nbsp;&nbsp;├── resolver/  
>│&nbsp;&nbsp;&nbsp;&nbsp;├── service/  
>│&nbsp;&nbsp;&nbsp;&nbsp;└── util/  
>├── mybatis/  
>├── PluSoft/  
>└── spring/ 

------

### 说明
1.com.ssc中的包分别对应：
> 
- **控制器**
- **dao层**
- **实体类**
- **过滤器**
- **拦截器**
- **监听器**
- **定时任务（暂存）**
- **service层**
- **工具** 

2.mybatis中存放的是数据库配置文件，表结构改动sql的记录，以及mybatis业务文件。

3.PluSoft 暂存数据库工具类。

4.spring中是项目的框架配置。

其他的文件为一些公共字典和日志配置，不用改动。
> 
**注:** 其中`dbconfig.properties`文件为连接数据库的参数配置文件，部署到服务器之前需改动其中的ip和数据库名。

---------------

## 接口补充
### *2017-09-21* 更新:
√为本地测试通过的接口。

### 后台已经写好的接口：
##### 用户：
> 
- **发送验证码:**`/api/v1/member/verificationCode` √
- **登录/注册:**`/api/v1/member/login`√
- **更新用户:**`/api/v1/member/updateUser`	√

---------------

##### 内容
> 
- **批量查询内容**`/api/v1/content/contentList`√
- **查询某内容**`/api/v1/content/findContent`√

-----------

##### 文集
> 
- **批量查询文集**`/api/v1/anthology/List`√
- **查询某文集**`/api/v1/anthology/findAnthology`	√
- **新增文集**`/api/v1/anthology/add`√
- **更新文集名称**`/api/v1/anthology/update`√
- **删除文集**`/api/v1/anthology/delete`√

----

##### 文章
> 
- **新建文章:**`/api/v1/article/add`√
- **更新文章:**`/api/v1/article/update`√
- **发布文章:**`/api/v1/article/publish`√
- **删除文章:**`/api/v1/article/delete`√


-----
##### 问答
> 
- **新增问题**`/api/v1/issue/add`√
- **更新问题**`/api/v1/issue/update`√
- **删除问题**`/api/v1/issue/delete`√

-----

##### 2级评论操作
> 
- **查询某评论**`/api/v1/comment/findComment`√
- **发表**`/api/v1/comment/publish`√
- **更新**`/api/v1/comment/update`√
- **删除**`/api/v1/comment/delete`√

----