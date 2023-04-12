# 简介

**Leaves**组织旨在帮助企业快速搭建企业级应用，并提供一系列的基础能力，并提供多种便捷starter进行功能扩展，方便使用者根据项目需求快速进行功能拓展。

以前每次搭项目时，经常都费一番功夫，并且还要解决项目版本升级工程中带来的各种坑。在使用一些开源的组件一段时间后，经常会遇到博主bug更新速度慢或者停止维护情况。项目开发比较机械，缺少一些提效工具。

为解决这些问题，**leaves**将项目搭建过程抽象，只需要根据相关业务场景引入相关jar包便能快速搭建项目，当项目升级时，只需要同步更新版本号，即可获得功能更新；并提供多种可插拔的starter方便后续拓展，并提供一些项目开发过程中的提效工具，帮助我们专注业务开发的同时提高开发效率。

**leaves 后续会将所有jar包推送至中央仓库，也会为每个版本的升级改动列出详细的更新日志。**

> 如果在使用中遇到了必须通过二开修改源码才能解决的问题或功能时，欢迎提 issues，如果功能具有通用性，我们会为 leaves 添加此能力，也欢迎直接 PR 你的改动。

- Github地址：[https://github.com/ryanddu/leaves](https://github.com/ryanddu/leaves)

# 技术栈

- 后端：Spring Boot、Spring Cloud Alibaba、Mybatis Plus、Redis、Rocket MQ、Mysql

# 相关模块

- leaves-base(项目搭建基础模块)：根据业务场景，分别引入相关jar包
  - leaves-base-core(项目核心能力)
  - leaves-base-api(普通项目api相关内容)
  - leaves-base-cloud-api(微服务项目api相关内容) 
  - leaves-base-web(普通项目web相关内容) 
  - leaves-base-biz(需要持久化的普通web项目相关内容) 
  - leaves-base-cloud-biz(需要持久化的微服务web项目) 
  - leaves-base-cloud-gateway(微服务项目的网关相关内容) 
- leaves-dependencies(依赖统一管理模块): 所有依赖、插件在这里统一管理
- leaves-utils(提效类工具模块):  提供提效类工具在这里提供
- leaves-starters(提供丰富的项目插件式starter): 根据业务需要分别引入相关的jar包 

# 快速上手

## 环境准备

- JDK: 1.8


