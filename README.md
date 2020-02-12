### wenzi-maven-jmeter-plugins

### 介绍
---

使用 Maven 构建的 Jmeter 自定义函数包。收录函数如下：

| 函数名 | 用途 | 测试版本 |
| :- | :- | :- |
| WZDecryptAES | 返回一个由 AES 解密后的字符串 | Jmeter 3.2 |
| WZEncryptAES | 返回一个由 AES 加密后的字符串 | Jmeter 3.2 |
| WZIDCardFromAge | 根据性别、最小~最大年龄，返回随机身份证号码（18位）| Jmeter 3.2 |
| WZIDCardFromBirth | 根据性别、出生日期，返回随机身份证号码（18位）| Jmeter 3.2 |
| WZImageBase64 | 根据指定的图片路径，返回由 Base64 编码后的字符串 | Jmeter 3.2 |
| WZRandomDate | 根据最小~最大日期，返回期间的一个随机日期 | Jmeter 3.2 |


### 安装
---

```
git clone
cd wenzi-maven-jmeter-plugins
mvn clean package
cp /target/wenzi-jmeter-plugins-1.0-SNAPSHOT.jar /your-jmeter-path/lib/ext/
```
your-jmeter-path 请替换为自己的 Jmeter 安装路径

### 使用
---

##### __WZDecryptAES

返回一个由 AES 解密后的字符串

变量

| 变量名 | 含义 | 是否必须 |
| :- | :- | :-: |
| Clear text | 待解密的字符串 | 必要 |
| Cipher key | 解密密钥 | 必要 |
| Variable name | 变量名 | 可选 |

举例
```
${__WZDecryptAES(8v3f1iDoCfXsTlscEts5Pg==,secret key,)}
```
返回：hello world

##### __WZEncryptAES

返回一个由 AES 加密后的字符串

变量

| 变量名 | 含义 | 是否必须 |
| :- | :- | :-: |
| Clear text | 待加密的字符串 | 必要 |
| Cipher key | 解密密钥 | 必要 |
| Variable name | 变量名 | 可选 |

举例
```
${__WZEncryptAES(hello world,secret key,)}
```
返回：8v3f1iDoCfXsTlscEts5Pg==

##### __WZIDCardFromAge

根据性别、最小~最大年龄，返回随机身份证号码（18位）

变量

| 变量名 | 含义 | 是否必须 |
| :- | :- | :-: |
| Sex | 性别，M-男性，F-女性，其他或为空时随机 | 可选 |
| Min age | 最小年龄，为空时默认18岁 | 可选 |
| Max age | 最大年龄，为空时默认60岁 | 可选 |
| Variable name | 变量名 | 可选 |

举例
```
${__WZIDCardFromAge(,,,)}
```
返回性别随机、年龄18-60周岁的身份证号码
```
${__WZIDCardFromAge(M,,,)}
```
返回男性、年龄18-60周岁的身份证号码
```
${__WZIDCardFromAge(M,33,,)}
```
返回男性、年龄33-60周岁的身份证号码
```
${__WZIDCardFromAge(M,33,48,)}
```
返回男性、年龄33-48周岁的身份证号码

##### __WZIDCardFromBirth

根据性别、出生日期，返回随机身份证号码（18位）

变量

| 变量名 | 含义 | 是否必须 |
| :- | :- | :-: |
| Sex | 性别，M-男性，F-女性，其他或为空时随机 | 可选 |
| Birth | 出生日期，格式yyyyMMdd，为空时默认18~60岁 | 可选 |
| Variable name | 变量名 | 可选 |

举例
```
${__WZIDCardFromBirth(,,)}
```
返回性别随机、年龄18-60周岁的身份证号码
```
${__WZIDCardFromBirth(M,,)}
```
返回男性、年龄18-60周岁的身份证号码
```
${__WZIDCardFromBirth(M,19900808,)}
```
返回男性、出生日期为1990-08-08的身份证号码

##### __WZImageBase64

根据指定的图片路径，返回由 Base64 编码后的字符串

变量

| 变量名 | 含义 | 是否必须 |
| :- | :- | :-: |
| File | 图片文件的绝对路径 | 必要 |
| Variable name | 变量名 | 可选 |

举例
```
${__WZImageBase64(/path/test.jpg,)}
```
返回：略

##### __WZRandomDate

根据开始~结束日期，返回期间的一个随机日期

变量

| 变量名 | 含义 | 是否必须 |
| :- | :- | :-: |
| Start date | 开始日期，格式yyyy-MM-dd | 必要 |
| End date | 结束日期，格式yyyy-MM-dd, 默认为系统当前日期 | 可选 |
| Format | 输出格式，例：yyyyMMdd，默认为yyyy-MM-dd | 可选 |
| Variable name | 变量名 | 可选 |

举例
```
${__WZRandomDate(1990-08-08,,,)}
```
返回：1998-08-08
```
${__WZRandomDate(1990-08-08,1992-08-08,,)}
```
返回：1991-08-08
```
${__WZRandomDate(1990-08-08,1992-08-08,yyyyMMdd,)}
```
返回：19910808

### 嵌套使用
---

__WZRandomDate生成的随机日期用于__WZIDCardFromBirth
需要新建BeanShell PreProcessor中预先定义
```
vars.put("birth", "${__WZRandomDate(1984-08-08,1989-08-08,yyyyMMdd,)}");
vars.put("idcard", "${__WZIDCardFromBirth(F,"+ vars.get("birth") +",)}");
```
请求中使用${idcard}即可。
