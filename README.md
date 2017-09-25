## Android-Serial-Port
安卓串口通讯，基于google官方编译，方便以后使用。

## 说明library
* libs
各类cpu架构对应的so文件

* src/main/android_serialport_api
一些控制类和打开关闭串口的操作

* ByteUtil
工具类，字节转string

* CRC16Verify
crc16校验算法

## 使用
1、将aar包复制到libs目录下
2、如果使用时报错缺少so，请将so文件复制到libs下，并配置
```
 sourceSets {
        main {
            jniLibs.srcDirs = ['libs']
        }
```
3、在module 的build.gradle中添加
```
repositories {
    flatDir {
        dirs 'libs'
    }
}
```
```
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
        exclude group: 'com.android.support', module: 'support-annotations'
    })
    compile 'com.android.support:appcompat-v7:26.+'
    testCompile 'junit:junit:4.12'
    compile(name: 'serialport', ext: 'aar')
}
```
### SerialPort
串口操作类，对应jni方法。用于串口打开关闭，获取输入输出流，通过输入输出流发送报文和获取响应报文。

### MachineControl
控制类，打开关闭串口

### SerialFinder
串口操作类
可枚举所有设备串口


