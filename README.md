### 使用说明
本软件仅供娱乐个人方便使用,不得用于商业用途,违法用途,
### 文档官网
(http://robot.lozn.top)

### 机器人接入与使用
机器人属于进程通讯,Q++默认实现了和本机器人的进程通讯,开发者可以自行把aidl对应的java文件拷贝到其它xposed或者其它项目,编写绑定服务代码即可完成绑定,通讯交互
具体参考文档
### 如何使用?
由于q++早就停止更新了,而且qq也限制了低版本不让登录,因此目前只能用户自己想办法,涉及到法律风险,因此我不再提供有关针对具体某软件的支持,
但是技术无罪,本软件是纯aidl项目,与QQ等无关,用户使用其他xposed插件和本机器人绑定不得用于损害对应宿主软件的事情,一切法律后果执行承担.design风格

### 开源的目的
本软件因为可以接入任何聊天软件,所以它有存在的意义, 可以通过本机器人和对应的聊天软件,实现远程控制对应的机器人设备,
因此才进行开源
而我本人一直用它做消息提醒功能以及当做一台远程设备(具体不透露了),通过它可以实现早上2点把你搞醒让你看你需要看的消息.
### 基本功能
执行shell,监听消息,转发提醒,权限管控(不允许所有人执行特权命令),JS/LUA/dex插件加载，跨进程代发消息
### 本软件的演示
我所发布的视频效果是结合服务绑定+本机器人绑定服务实现了远程执行shell命令,查看股票行情.搜索图片等.跨应用通知转发邮箱订阅,也支持chatgpt,让小爱同学音箱进行朗读,内网穿透,执行shell,授权adb
如果群被封了,可以访问QQ35068264的QQ空间翻看说说可找到最新的群,建议加入电报群

### 愿景
+ 界面 
  
    机器人写的很烂,特别是界面,有精力的朋友可以做一个界面消息版,material design风格,我的意思是aidl传递过来的消息可以通过界面显示,可以查看某人,某群分组聊天记录信息.

我只只是为了自己方便 甚至代码都懒得写在插件里了,直接写在机器人软件里头了.


+ 自动升级插件

    目前机器人是支持加载本地插件的,我希望以后管理员发送命令激活插件 https://xxxx.xxx/xx.dex 自动升级插件
    然后输入配置重载,和插件信息可以查看信息了.



+ 完善老人远程辅助和内网穿透相关能力
    由于机器人具备执行shell的能力,如果运行在root设备,那么远程控制手机会更加得心应手.
+ 增加电话控制能力，方便提醒转电话提醒
+  目前已经支持内网穿透,adb远程授权调试,chat gpt聊天,软件通知栏消息转发(由其它xposed软件发送给本机器人) 

    





### 其它
为了图方便自己加功能，我懒得写插件，需要什么直接改什么。。



