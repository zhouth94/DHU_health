# DHU_health
# 健康项目后台管理系统
传智健康管理系统是一款应用于健康管理机构的业务系统，实现健康管理机构工作内容可视化、会员管理专业化、健康评估数字化。
## 一、技术架构

images/技术架构图.png

## 二、项目搭建
本项目采用maven分模块开发方式，即对整个项目拆分为几个maven工程，每个maven
工程存放特定的一类代码，具体如下：
各模块职责定位：
health_parent：父工程，打包方式为pom，统一锁定依赖的版本，同时聚合其他子模块便于统一执行maven命令；
health_common：通用模块，打包方式为jar，存放项目中使用到的一些工具类、实体类、返回结果和常量类；
health_interface：打包方式为jar，存放服务接口；
health_service_provider：Dubbo服务模块，打包方式为war，存放服务实现类、Dao接口、Mapper映射文件等，作为服务提供方，需要部署到tomcat运行；
health_backend：传智健康管理后台，打包方式为war，作为Dubbo服务消费方，存放Controller、HTML页面、js、css、spring配置文件等，需要部署到tomcat运行；
health_mobile：移动端前台，打包方式为war，作为Dubbo服务消费方，存放；
Controller、HTML页面、js、css、spring配置文件等，需要部署到tomcat运行。
