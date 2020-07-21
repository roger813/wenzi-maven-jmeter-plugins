# wenzi-maven-jmeter-plugins

#### 介绍
自定义的JMeter插件，包括了一些简单的自定义函数和自定义请求。

```
开发环境：Apache JMeter 3.2 (不保证其他版本的JMeter能够正常使用)
```

#### 安装教程

1. git clone http://liwenhuan@gitlab.zhonganonline.com/liwenhuan/wenzi-maven-jmeter-plugins.git
2. cd wenzi-maven-jmeter-plugins
3. mvn clean package
4. cp wenzi-maven-jmeter-plugins-1.0-SNAPSHOT /yourjmeter/lib/ext
5. restart jmeter

#### 使用说明

##### WZDecryptAES

   - 说明：AES解密
   - 用法：${__WZDecryptAES(9taFmUe+cbjk8bIoBr9zKw==,key,)}

##### WZEncryptAES

   - 说明：AES加密
   - 用法：${__WZEncryptAES(hello,key,)}

##### WZIDCardFromAge

   - 说明：根据年龄生成随机身份证（默认18-60周岁）
   - 用法：${__WZIDCardFromAge()}、${__WZIDCardFromAge(M,18,60,)}、${__WZIDCardFrom(R,40,40,)}

##### WZIDCardFromBirth

   - 说明：根据出生日期生成随机身份证（默认18-60周岁）
   - 用法：${__WZIDCardFromBirth()}、${__WZIDCardFromBirth(M,19880808,)}

##### WZImageBase64

   - 说明：返回指定图片的base64编码
   - 用法：${__WZImageBase64(file,)}，file为文件路径，必须是绝对路径

##### WZRandomDate

   - 说明：返回指定区间内的随机日期（格式必须为：yyyy-MM-dd，默认返回：yyyy-MM-dd）
   - 用法：${__WZRandomDate(1988-08-08,,,)}、${__WZRandomDate(1988-08-08,2008-08-08,,)}、${__WZRandomDate(1988-08-08,2008-08-08,yyyyMMdd,)}

#### 欢迎贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request
