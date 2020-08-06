# EchoValley
TODO 适配
https://github.com/CymChad/BaseRecyclerViewAdapterHelper
https://github.com/scwang90/SmartRefreshLayout

从分页加载的两三事说起以及更多
分页加载是大前端开发（包括移动端和Web）中司空见惯的一类场景，但能玩出的花样却足以让人眼花缭乱。
为了将程序员从重复的模板代码中解放出来，专注于业务本身，各位大神做了不少不懈的努力，就Android端而言，比方说功能一代比一代强的刷新控件，Adapter等等
而本套框架的目的，试图从另一个角度来对分页加载这一场景，更广义的来说是围绕着刷新组件的功能单元，做一套抽丝剥茧的分析。

首先定义一个概念，IViewModule——专注于某个连贯的功能单元的一组View，这个定义其实是参考UseCase的定义（在不展现一个系统或子系统内部结构的情况下，对系统或子系统的某个连贯的功能单元的定义和描述）
接下来自然的，对于专注于刷新组件这一连贯的功能单元的一组View，可以定义为IRefreshViewModule（既然核心功能是依赖于刷新组件的）

既然是一组View，让我们来分析一下这组View应该有哪些成员
1、刷新组件，支持上拉下拉动作的容器（比方说SwipeRefreshLayout，SmartRefreshLayout）
2、列表控件，用来填充数据，常见就是RecyclerView和ListView，但在这一场景下我们更关心它们的adapter，一般情况下，我们都会继承原始的adapter，做些基础的封装（比方说BRVAH）
3、空白页（可选）
4、错误页（可选）