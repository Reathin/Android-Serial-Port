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

* BCCVerify
bcc异或校验

* OnDataReceiverListener
接受到回复后的回调监听

## 使用
1、将library作为依赖导入

2、如果使用时报错缺少so，请将so文件复制到libs下，并配置
```
 sourceSets {
        main {
            jniLibs.srcDirs = ['libs']
        }
```
3、在module 的build.gradle中添加

```
dependencies {
    compile(':library')
}
```
### SerialPort
串口操作类，对应jni方法。用于串口打开关闭，获取输入输出流，通过输入输出流发送报文和获取响应报文。


### MachineControl
控制类，打开关闭串口，发送接受报文

一般写成单例，在App中打开或关闭串口，不需要频繁的打开关闭
```
public MachineControl(String devName, int baudRate) 构造方法(串口设备名，波特率)

boolean openCOM()  打开串口

void setOnDataReceiverListener(OnDataReceiverListener onDataReceiverListener) 设置监听，接收回复的报文及数据长度

void closeCOM() 关闭串口

boolean sendCMD(byte[] data) 发送报文
```
### SerialFinder 可不使用
串口操作类
枚举所有设备串口

## License
Apache2.0
