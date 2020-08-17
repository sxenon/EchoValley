# EchoValley
https://github.com/scwang90/SmartRefreshLayout

换个角度看刷新控件
刷新控件是移动开发中上层几乎必然接触到的控件之一，市面上的开源刷新控件数不胜数，功能日益强大（为了满足产品不断增长的设计需求，哦不，是为了给用户更好的交互体验）。
本套框架的目的，当然不是去讲述怎样去做好一个功能完善的刷新控件，大家直接学习各位大神的源码即可，没必要班门弄斧了。
So，这篇框架的目的究竟是什么，要解决哪些痛点呢？
让我们想想刷新控件使用的场景是什么？
一个最最常见的场景：下拉刷新，上拉加载
那么我们就来分析一下在这一场景下，我们都有哪些常规操作及可能的结果
1、下拉刷新，从数据源获取数据，填充。可能的结果
   2.1 没有数据，显示空白页
   2.2 比设定每页数量少，没有更多
   2.3 发生错误，显示错误页
   2.4 正常加载第一页
2、上拉加载，从数据源获取数据，填充。可能的结果
   2.1 比设定每页数量少，没有更多
   2.2 发生错误，显示错误页
   2.3 正常加载当前页

你至少
1、必须手动维护页数，对所有上述操作的的结果进行响应，特别需要小心的，对于发生错误的场景
2、必须手动维护空白页，错误页，列表视图的显示或者隐藏

这些工作有什么技术含量吗？没有。
那么每次遇到这样刷新需求，都要去重复完成这样的工作，对自己有实质上的提高吗？没有。
那么有人想要做出些改变吗？没有。
因为这些都是一线开发人员每天需要面对的，又没有什么难度，改变往往吃力不讨好，有动力吗？没有。

But，这不是笔者的风格。
本套框架的目前就是要消灭这种重复工作，当然不仅仅是下拉刷新，上拉加载的这一场景。
肯定有人会提出来，市面上的刷新控件这么多，怎么可能一套代码实现？
不好意思，这不是笔者的风格。不要你觉得，我要我觉得。
肯定还是会有人提出来，上拉加载更多发生错误的时候，我不想显示错误页，而是在Footer上提示，这种个性化的需求千差万别，怎么可能一套代码实现？
笔者必须承认，无法用一套代码实现，但可以用一套模式去实现

好了，铺垫了那么多，终于可以切入正题了。
请淡定地拿出瓜子和小板凳，开始了！

首先让我们来看一下就分页加载这一典型场景（注意，已经比下拉刷新，上拉加载范围大了），会涉及到哪些对象
1、刷新控件，支持上拉下拉动作的容器
2、列表控件，用来填充数据，常见就是RecyclerView和ListView，但在这一场景下我们更关心它们的adapter
3、刷新策略
    3.1 下拉刷新，上拉加载更多。（这是最常见的）
    3.2 下拉最新，上拉加载更多。（新闻类）
    3.3 上拉下一页，下拉上一页。(比较少见）
4、空白页（可选）
5、错误页（可选）

上面的五个成员中，
1、空白页和错误页没有什么疑问
2、刷新策略是有限的
但问题在于
1、刷新控件，可以选择的相当的多。
2、adapter，几乎每个项目都会在官方版本的基础上做自己的封装。
可能的组合是海量，怎么办？

这个时候就非常适合采用伟大的桥接模式（Bridge Pattern）(请网上自行找结构图，不然可能理解不了)
先复习一下它的定义：将抽象部分与它的实现部分分离，使它们可以独立地变化。

一步步来解释
Bridge Pattern 其实还有一个别名为Handle/Body Pattern
由此我们定义一个接口
public interface IViewHandle {//由桥接方式组织多个View的Handle
    Context getContext();
}
那么自然的，用来处理Refresh这些事的Handle，可以有
public interface IRefreshViewHandle extends IViewHandle {

    void onCancel();

    void onError(Throwable throwable);

    void onEmpty();

    void onNonEmpty();

    int getPullAction();
}
对应关系
Abstraction
BaseListRefreshViewHandle

Implementor
IListRefreshViewHandle<T> //刷新策略
IAdapter<T> //列表视图的Adapter
IRefreshLayout //刷新控件

一一分析这三个Implementor
IRefreshStrategy<T> //刷新策略，笔者给出了它的三个实现类,基本包含了常见的业务场景
NewAndMoreListRefreshStrategy<T>
PrevAndNextListRefreshStrategy<T>
RefreshAndMoreListRefreshStrategy<T>

IAdapter<T> //列表视图的Adapter
虽然大家都各自有自己的实现，但就基础的方法就是这个接口所定义的，很容易通过适配器模式解决

IRefreshLayout //刷新控件
这个很遗憾，虽然笔者做过尝试，实在是无法将市面上的刷新控件做统一的处理。
但是，我们可以退而求其次，我们并不需要对所有的刷新控件去实现我们的功能，但可以给出一套思路。
因为，在我们的项目中，用到刷新控件非常有限，通常只有一个。

接下来的事情就变得简单了(以SmartRefreshLayout为例)
RefinedAbstraction
SmartRefreshViewHandle

ConcreteImplementor
IRefreshStrategy<T> 上述的三个策略
IAdapter<T> 适配器模式去实现

最核心的，其实是刷新策略的实现，
以最常见的RefreshAndMoreListRefreshStrategy<T>为例
所有的方法都通俗易通
而我们一直重复的工作内容，其实就是对数据结果的分类监听。这部分是可以复用的。这便是这套框架的核心目的

回到上面提到过的个性化问题，还是那句话，一套代码解决不了的问题，一套模式可以。






