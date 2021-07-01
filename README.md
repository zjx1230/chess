## 联机象棋

[TOC]

### 需求

- [x] 登录、注册
- [x] 在线人人匹配对战（待完善）
- [ ] 人机对战(待实现)
- [ ] 

### 架构与开发技术

- 架构上采用客户端-服务端模式
- 客户端主要使用Java Awt/Swing、Netty,服务端使用SpringBoot、Netty、MyBatis
- 软件上数据库使用MySQL，Java JDK版本为8

### 主要设计与实现

#### 1. 棋盘、棋子布局

- 主要将象棋棋盘看成一个10*9的二维方格，用一个二维数组map\[10][9]来表示，原象棋红黑双方共32枚棋子，使用一个数组Chess[32]表示。
- map\[i][j]=-1表示该棋盘处当前没有棋子，map\[i][j]不等于-1则表示为Chess[map\[i][j]]的索引。

#### 2. 选棋、下棋

1. 选棋下棋总体流程图

![未命名文件](/Users/zjx/Downloads/未命名文件.png)

2. 判断棋子移动是否合法

   **利用面向对象编程的多态特性，首先构建一个抽象类Chess，其中包含一个抽象方法isAllowMove(),由各类棋子继承编写自己的走法规则约束**

![象棋类图](/Users/zjx/Downloads/象棋类图.png)

#### 3. 人人对战匹配

​	目前暂时是使用阻塞队列实现，但实际多人使用时还是会存在一些并发问题，后面再完善。其当前流程图如下：

![游戏匹配流程图](/Users/zjx/Downloads/游戏匹配流程图.png)

#### 4. 判断是否被将

​	每次棋局盘面的变化都判断一下是否有棋子可以吃掉王/帅。

#### 5. 通信模块

```java
public class Command {

    private Integer userId;

    private Integer enemyId;

    private String commandType;

    private int oldX, oldY;

    private int newX, newY;

    private String enemyName;

    private String message;

    private short player;
}
```

使用Netty自定义编码器和解码器进行数据的传输，根据不同命令类型做出相应的反应。比如下棋、悔棋、人数等。

#### 6. 其他如声音效果等提升用户体验

1. 落子声音、吃、将军的声音；
2. 合理提示信息。

#### 7. 人机对战(尚未实现)

#### 8. 最终实现效果图

![image-20210701205543397](/Users/zjx/Library/Application Support/typora-user-images/image-20210701205543397.png)

