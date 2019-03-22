<h1 align="center">基于S2SH架构的购物商城项目</h1>

## 项目描述
本项目采用了Struts2+Spring5+Hibernate5传统架构，其中Spring部分实现无xml纯java配置。采用Maven做项目管理，采用MySQL数据库，c3p0连接池，采用本地文件系统作为图片上传后的存储目录。采用Hibernate作为数据持久层，Spring作为业务逻辑层管理所有bean，Struts2作为控制层，jsp作为展现层。前端部分：前台网页使用Bootstrap3 UI框架设计；后台管理页面使用Jquery EasyUI框架内嵌在Bootstrap3框架内的结构以实现美观，使用CKEditor作为在线编辑器。


## 实现功能
1. 前台模块：用户注册、登录、注销，修改个人信息；商品分类、商品搜索、商品详情，可添加至购物车；可对商品评论，可对评论评论。订单中心可查看交易状态；商城公告，商城新闻，外部链接。
2. 后台模块：可对用户增删改查，可对商品及商品所属大小类别增删改查，可查看并修改订单状态，可查看和删除评论及子评论，可增删改查全站公告、商城新闻及外部链接。

## 亮点
1. 用户中心页面集中了很多亮点操作：datetimepicker日期控件、file样式的隐式替代、文件异步上传、表单异步提交、通过EL表达式直接控制js切换菜单、通过样式定位父级再定位子级元素、图片验证之防止重复提交、修改ajaxSettings实现密码的同步验证、通过切换dispaly实现页面局部异步刷新（此处可能有更好的实现方式，有空再优化）。
2. 项目重要设计：评论借鉴了B站的评论模式做了一个简化版的评论模块，分评论和内部评论，内部评论对特定用户的回复显示回复@user字样。
3. 项目重要设计：用js写了一个前端二级菜单的hover弹出实现。
4. 前台页面对顶部、底部、侧边导航、路径导航、搜索栏做了模块化分离。
5. 分页和路径导航采用了纯java的处理方式，并封装了一套bootstrap的分页实现。
6. 采用FormData实现了文件异步传输，采用serialize()实现表单form的异步传输。
后台通过easyui实现crud的jsp部分是很好的模板，可供其他项目借鉴。

## 不足
1. Hibernate框架学习成本高，且建表过程比较复杂。
2. 没有用安全框架，没用对密码进行MD5加密。
3. 新闻和公告没有做list页面。
4. 评论刷新没有单条刷新，而采用了整体异步刷新，内部评论暂时没有实现分页（完整版可参考我的blog项目）。
5. 后台验证应该在每个方法处对每一个传入的参数做相应的验证，做到方法对自身负责。
6. 好多方法之前都需要后端验证当前用户是否存在，struts2还是不够灵活，实现起来繁琐。
发现一个数据库外键错误：不应该在商品中建立大类到商品的外键关联，不然在修改时会做重复多余的外键修改操作，这里暂时不修改数据库怕其他地方从商品直接获取大类报错。

## 备注
1. 项目会创建 D:/Project-Data/market-data 目录作为文件存储目录。 
2. 项目仅供学习参考。