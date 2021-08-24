### wenzi-maven-jmeter-plugins

##### 介绍

自定义的JMeter插件，包括了一些简单的自定义函数和自定义请求。
*开发环境：Apache JMeter 3.2 (不保证其他版本的JMeter能够正常使用)*

##### 安装教程
1. git clone http://liwenhuan@gitlab.zhonganonline.com/liwenhuan/wenzi-maven-jmeter-plugins.git
2. cd wenzi-maven-jmeter-plugins
3. mvn clean package
4. cp wenzi-maven-jmeter-plugins-1.0-SNAPSHOT /yourjmeter/lib/ext
5. restart jmeter 
   
##### 使用说明
1. WZDecryptAES
   ```text
   说明：AES解密
   用法：${__WZDecryptAES(9taFmUe+cbjk8bIoBr9zKw==,key,)}
   ```
2. WZEncryptAES
   ```text
   说明：AES加密
   用法：${__WZEncryptAES(hello,key,)}
   ```
3. WZIDCardFromAge
   ```text
   说明：根据年龄生成随机身份证（默认18-60周岁）
   用法：${__WZIDCardFromAge()}
        ${__WZIDCardFromAge(M,18,60,)}
        ${__WZIDCardFrom(R,40,40,)}，R代表性别随机
   ```
4. WZIDCardFromBirth
   ```text
   说明：根据出生日期生成随机身份证（默认18-60周岁）
   用法：${__WZIDCardFromBirth()}
        ${__WZIDCardFromBirth(M,19880808,)}
   ```
5. WZImageBase64
   ```text
   说明：返回指定图片的base64编码
   用法：${__WZImageBase64(file,)}
        其中file为文件路径，且必须是绝对路径
   ```
6. WZRandomDate
   ```text
   说明：返回指定区间内的随机日期（默认格式：yyyy-MM-dd）
   用法：${__WZRandomDate(1988-08-08,,,)}
        ${__WZRandomDate(1988-08-08,2008-08-08,,)}
        ${__WZRandomDate(1988-08-08,2008-08-08,yyyyMMdd,)}
   ```
   
##### 欢迎贡献
1. Fork 本仓库
2. 新建 Feature_xxx 分支
3. 提交代码
4. 新建 Pull Request