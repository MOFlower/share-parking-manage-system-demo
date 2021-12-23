# MQTT 对树莓派的接口

## V1.0

1. 设备启动向服务器请求设备唯一身份，服务器返回对应的身份 ID，订阅对应的两个收发信息主题。

   url: http://localhost:8081/device/{device_id}

   example: http://localhost:8081/device/qoiw3nsiodj0132

   response:

   ```json
   {
       "receive-topic":"j912jineqpnwo",
       "send-topic":"jsipdofjo1"
   }
   ```

2. 服务器控制树莓派的接口：

    - 控制电机启动车篷：

      接收主题为对应的 `receive-topic` 值

      ```json
      // 打开电机
      {
          "method":"open"
      }
      // 关闭电机
      {
          "method":"close"
      }
      // 确认车牌是否占用, 
      // 此时车位需要向服务器的 send-topic 响应消息
      {
          "method":"isEmpty",
          "args": "{"msg":"string"}"
      }
      ```

    - 发送对应的车牌信息到服务器

      发送主题为对应的 `send-topic`  值

      ```java
      // 验证对应的车牌是否空闲
      {
          "method":"isAvailable",
          "args":"{"number":"粤A33333"}"
      }
      // 响应车位值占用情况的查询
      {
          "method":"isEmptyRespones",
          "args":"{"isEmpty":"false","msg":"string"}" // 指示车位以占用
      }
      ```
     
     
