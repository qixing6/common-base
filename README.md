# common-base

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Version](https://img.shields.io/badge/version-1.0.2-brightgreen.svg)](https://github.com/qixing6/common-base)

一个轻量级的 Java 通用基础依赖包，封装了后端开发中高频使用的核心能力，旨在减少重复编码、统一开发规范，提升项目开发效率。

## ✨ 核心功能

| 功能模块                | 详细说明                                                                 |
|-------------------------|--------------------------------------------------------------------------|
| 统一返回结果封装        | 标准化 API 响应格式（Result），包含状态码、消息、数据体，支持链式调用     |
| 自定义异常体系          | 封装业务异常（BusinessException）等，区分系统异常/业务异常，便于精准处理  |
| 全局异常处理            | 统一捕获项目中各类异常，格式化返回结果，避免前端接收到非标准化错误信息     |
| JWT 令牌工具            | 封装 JWT 的生成、验证、解析逻辑，支持自定义过期时间、签名密钥             |

#### Maven
```xml
依赖位置和版本
<dependency>
    <groupId>com.example</groupId>
    <artifactId>common-base</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>
版本号	更新时间	核心变更
1.0.0	2026-01-01	初始版本，基础功能封装
1.0.1	2026-02-01	优化 Result 链式调用体验
1.0.2-SNAPSHOT	2026-03-01	修复 JWT 过期时间计算 bug，增强异常提示
