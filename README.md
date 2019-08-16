### wenzi-maven-jmeter-plugins

### 简介
---

一个使用Maven构建的工具库，为Jmeter脚本编写提供实用工具。

最新版本1.0.0，由Maven 3.6.1构建，支持Apache JMeter 3.2 - 5.1.1。

### 安装
---

```
git clone
mvn clean package
cp wenzi-jmeter-plugins.jar /your-apache-jmeter-version/bin/lib/ext/
```

### 使用
---

##### WZDecryptAES

```
* 格式：${__WZDecryptAES(src, key, name)}
* 描述：根据给定的原文和密钥，返回解密后的字符串
* 参数：
    - src 需要解密的字符串（必须）
    - key 解密私钥（必须）
    - name 变量名，用于存放解密结果（可选）
```

##### WZEncryptAES

```
* 格式：${__WZEncryptAES(src, key, name)}
* 描述：根据给定的原文和密钥，返回加密后的字符串
* 参数：
    - src 需要加密的字符串（必须）
    - key 加密私钥（必须）
    - name 变量名，用于存放解密结果（可选）
```

##### WZGetFileRows

```
* 格式：${__WZGetFileRows(file, name)}
* 描述：根据给定文件的绝对路径，返回文件最大行数
* 参数：
    - file 文件的绝对路径（必须）
    - name 变量名，用于存放解密结果（可选）
```

##### WZRandomAsFile

```
* 格式：${__WZRandomAsFile(file, rows, name)}
* 描述：根据给定文件的绝对路径，返回随机行数的内容
* 参数：
    - file 文件的绝对路径（必须）
    - rows 文件行数，默认=0（可选）
    - name 变量名，用于存放解密结果（可选）
```

##### WZRandomDate

```
* 格式：${__WZRandomDate(age, name)}
* 描述：根据给定的年龄大小，返回随机日期
* 参数：
    - age 年龄，正整数，不指定则默认返回18-60周岁的日期（可选）
    - name 变量名，用于存放解密结果（可选）
```

##### WZRandomIDCard

```
* 格式：${__WZRandomIDCard(birth, gender, name)}
* 描述：根据给定的出生日期，返回随机18位身份证（可通过常规校验，无法用于公安认证）
* 参数：
    - birth 出生日期，不指定则默认返回18-60周岁的证件（可选）
    - gender 性别，M为男性，F为女性
    - name 变量名，用于存放解密结果（可选）
```

##### WZCSVReader

![WZCSReader](https://thumbnail0.baidupcs.com/thumbnail/1dba4f7d981cfd4b9c6087ae61ebf30a?fid=2986668196-250528-269297795274560&time=1565920800&rt=sh&sign=FDTAER-DCb740ccc5511e5e8fedcff06b081203-h%2F7krhrUgakP6YpwPptEUKoiGKo%3D&expires=8h&chkv=0&chkbd=0&chkpc=&dp-logid=5275185808071540165&dp-callid=0&size=c1920_u1200&quality=90&vuk=-&ft=video&autopolicy=1)
```
* 参数
    - FileURI 文件所在路径，必须是绝对路径
    - Count 文件总行数，=0时自动获取文件行数，但可能存在性能损耗
```

##### WZCSVWriter

![WZCSReader](https://thumbnail0.baidupcs.com/thumbnail/4c3aa750d2af380e6c5e636d720af63f?fid=2986668196-250528-22550466727209&time=1565924400&rt=sh&sign=FDTAER-DCb740ccc5511e5e8fedcff06b081203-ZT0fIlYu7khU%2B1wr4d9UCiSPzBI%3D&expires=8h&chkv=0&chkbd=0&chkpc=&dp-logid=5275430143121216080&dp-callid=0&size=c1920_u1200&quality=90&vuk=-&ft=video&autopolicy=1)
```
* 参数
    - FileURI 文件所在路径，必须是绝对路径
    - Content 写入内容，可接受变量
```

##### 嵌套函数

```
${__WZRandomIDCard(${__WZRandomDate(23,)},M,)}
首先生成随机的23周岁的出生日期，其次生成此日期的男性身份证号码。

${__WZRandomAsFile(file, ${__WZGetFileRows(file,)},)}
首先读取文件的总行数，其次将总行数代入外层函数进行随机读取。
```
