![](https://shitu-query-bj.bj.bcebos.com/2020-09-03/10/729487fce79fb1c3?authorization=bce-auth-v1%2F7e22d8caf5af46cc9310f1e3021709f3%2F2020-09-03T10%3A34%3A08Z%2F300%2Fhost%2F4665c75336899adc01bb9e7b07f7cf57e4314d447372407d58faea3d92b7f1ec)

# **Modularity**

本开发框架模板本着：功能模块化、集成组件化、开发实用化、编码清晰化的原则。  
以 MVP 开发模式为基础，依赖 ARouter 实现组件化开发。封装了实用的功能，如：TCP Server/Client 的创建、经典蓝牙/低功耗蓝牙通信、Fragment 间的通信以及各种自定义控件等。随着开发经验的增加也在不断更新完善中..
---
## **项目结构**

- **app**：应用主项目
- **moduleCore** 通用的核心功能封装集合  
1、**carlLib**:通用代码封装  
activity:Activity/Fragment 的基础封装  
fragmentfunc: Fragment 间通信工具  
httpframe: 网络请求框架封装，支持一键更换网络请求方式  
manage: 硬件设备通信管理类  
net: 网络请求实体基础解析工具  
presenter: MVP 中 Presenter 的基本封装  
utils: 通用工具类  
widget: 常用自定义组件  
2、**communicationLib**:TCP Server/Client 通信封装，经典蓝牙/低功耗蓝牙通信封装  
3、**lib-zxing**:二维码功能库
- **moduleBase** 针对项目的功能封装集合  
1、**muduleData**:通用数据模型库
const: 页面路由清单  
model: 业务逻辑中通用数据模型  
service: 用于 module 之间项目调用接口
- **moduleSystem** 组件化开发功能集合  
1、**testb**:插件化开发测试
