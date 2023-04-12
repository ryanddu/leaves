### [0.0.1-SNAPSHOT] 2023-04-12

#### Features

##### 第一个版本新增的内容

- leaves-base 项目搭建基础模块
  - leaves-base-core 项目核心能力
    - 通用枚举
    - 全局异常
    - 全局异常断言
    - 核心工具类
  - leaves-base-api 普通项目api相关内容
    - 基础api交互相关公共实体
    - 参数验证
    - openapi3相关注解
  - leaves-base-cloud-api 微服务项目api相关内容
    - 该模块依赖leaves-base-api
    - 新增openfeign，提供rpc调用能力
  - leaves-base-web 普通项目web相关内容
    - 序列化相关配置
    - WebMvc相关配置
    - 全局异常处理，提供国际化处理
    - web相关工具类
  - leaves-base-biz 需要持久化的普通web项目相关内容，提供相关持久化能力，目前仅支持mysql
    - 持久层需要的所有抽象基类
    - mybatisplus相关配置
    - 全局事务管理配置
  - leaves-base-cloud-biz 需要持久化的微服务web项目
    - 该模块依赖leaves-base-biz
    - nacos
    - openfeign
  - leaves-base-cloud-gateway 微服务项目的网关相关内容
    - gateway
    - nacos
    - openfeign
- leaves-dependencies 依赖统一管理模块
  - 项目有关的依赖统一管理
- leaves-utils 提效类工具模块
  - codegen 代码生成工具
    - 提供多种代码模板
    - 根据配置动态生成项目代码
- leaves-starters 提供丰富的项目插件式starter
  - 待持续迭代
