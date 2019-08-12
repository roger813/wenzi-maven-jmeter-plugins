### wenzi-maven-jmeter-plugins

### 简介

一个使用Maven构建的工具库，为Jmeter脚本编写提供实用工具。

最新版本1.0.0，由Maven 3.6.1构建，支持Apache JMeter 3.2 - 5.1.1。

### 使用

```
git clone
mvn clean package
cp wenzi-jmeter-plugins.jar /your-apache-jmeter-version/bin/lib/ext/
```

### 功能

#####自定义函数

* WZDecryptAES, 根据给定的原文和密钥，返回解密后的字符串
* WZEncryptAES, 根据给定的原文和密钥，返回加密后的字符串
* WZGetFileRows, 根据给定文件的绝对路径，返回文件最大行数
* WZRandomAsFile, 根据给定文件的绝对路径，返回随机行数的内容
* WZRandomDate, 根据给定的年龄大小（默认18-60周岁），返回随机日期
* WZRandomIDCard，根据给定的出生日期（默认18-60周岁），返回随机18位身份证

#####Java Sampler

* WZCSVReader, 根据给定文件的绝对路径，返回随机行数的内容
* WZCSVWriter, 根据给定文件的绝对路径，将指定内容写入文件
