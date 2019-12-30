### wenzi-maven-jmeter-plugins

### 介绍
---

使用 Maven 构建的 Jmeter 自定义函数包。收录函数如下：

| 函数名 | 用途 | 测试版本 |
| :- | :- | :- |
| WZDecryptAES      | 返回一个由 AES 解密后的字符串 | Jmeter 3.2 |
| WZEncryptAES      | 返回一个由 AES 加密后的字符串 | Jmeter 3.2 |
| WZIDCardFromAge   | 根据性别、最小~最大年龄，返回随机身份证号码（18位）| Jmeter 3.2 |
| WZIDCardFromBirth | 根据性别、出生日期，返回随机身份证号码（18位）| Jmeter 3.2 |
| WZImageBase64     | 根据指定的图片路径，返回由 Base64 编码后的字符串 | Jmeter 3.2 |

### 安装
---

```
git clone
cd wenzi-maven-jmeter-plugins
mvn clean package
cp wenzi-maven-jmeter-plugins.jar /your-jmeter-path/lib/ext/
```
your-jmeter-path 请替换为自己的 Jmeter 安装路径

### 使用
---

##### __WZDecryptAES

返回一个由 AES 解密后的字符串

变量
| 变量名 | 含义 | 是否必须 |
| :- | :- | :-: |
| Clear text |||
| Cipher key |||

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
