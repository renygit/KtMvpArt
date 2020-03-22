# KtMvpArt

### 第一步 布局
```xml
<LinearLayout
        android:id="@+id/viewReplacer"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center"
        android:orientation="vertical">

        <com.scwang.smart.refresh.layout.SmartRefreshLayout
            android:id="@+id/srl"
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <androidx.recyclerview.widget.RecyclerView
                android:id="@+id/rv"
                android:layout_width="match_parent"
                android:layout_height="match_parent" />

        </com.scwang.smart.refresh.layout.SmartRefreshLayout>

    </LinearLayout>
```
布局也许是这样,前2个id（viewReplacer，srl）在库中指定了的，不能修改，这样有什么用呢？如果定义了这两个id，多状态显示，下拉刷新，下拉加载更多的逻辑
就不用写了。当然，你可以重写它的样式，动态修改显示的UI。唯一还要做的一步就是在activyt或者fragment中调用一下presenter的loadData方法，在你的presenter
（继承MvpPresenter）中须要重写这个方法，super.loadData方法必须调，这样就完成了自动处理事件，在loadData中怎么访问网络等操作呢？看下面：
### 第二步 使用协程加载数据
```kotlin
addJob(doAsync(exceptionHandle(), {//exceptionHandle：网络访问出错等异常时处理UI的逻辑 封装到一个地方统一处理
            val data = Api.api().getDatas(参数).doRequest()  //retrofit方式 最后调用扩展方法doRequest()，在里面统一处理所有错误码的逻辑
            uiThread {
                val empty = isEmpty(data)
                setLoadState(if(empty) STATE_EMPTY else STATE_CONTENT, !empty, isRefresh)//这是唯一需要关心的和UI相关的代码 
                getIView().setResult(data, isRefresh)//设置UI相关数据
            }
        }))
  ```
  上面的exceptionHandle和doRequest需要关心一下，都是网络错误的情况怎么处理相关业务逻辑，但只写一遍，维护一份代码，写好以后就都是上面这样的少量
 代码
 
 ### 第三步（也可以没有）
 想使用骨架屏（[Skeleton](https://github.com/ethanhua/Skeleton)）怎么办呢，在initView中初始化screenBuilder或者screenRecyclerBuilder
 ```kotlin
 screenRecyclerBuilder = Skeleton.bind(recyclerView)
            .adapter(adapter)
            .count(1)
            .load(R.layout.screen_page)
 ```
 这样就行了，不要再多写代码了<br>
 想修改一下加载出错，网络出错，数据为空的ui(都是基于[Skeleton](https://github.com/ethanhua/Skeleton))，或者不想用骨架屏，想用个loading。。。 不写上面的代码，默认就有了。想修改一下loading的UI，应该怎么办呢？
 可以自己写个BaseActivity继承MvpActivity,重写getLoadingId或者getLoadingView就可以了，具体的查看源码吧，源码太简单不多说了
 
  ##### 其它的都是MVP的套路了，有兴趣请看demo
  ### 其它
  还在用AOP来做集成式登录吗（在任何需要判断登录的地方加注解的方式），看看我项目中的方式
  ```kotlin
  fun Any.checkLogin(tip:String = "您未登录，请先登录", nextFunc: () -> Unit){
    if(LoginApi.api.isLogin()){//ARouter接口访问login组件判断是否登录
        nextFunc()
    }else {
        showToast(tip)
        getARouter().build(RConfig.FtLogin.login).navigation()
    }
}

//调用
checkLogin { 登录后的操作 你要做的操作 }
```
同理，重复点击过滤之类的也可以这样写，当然，这些和本项目无关，就不一一例举了。本项目还有更多未完善的地方，没有集成dagger2,rxjava等，只是为了做一个
能快速开发，快速迭代的架构。
### 翻太快到底了，没有更多代码了，真的很少，主要代码都贴完了，为RecyclerView设置Adapter这样的代码就不贴了，demo中也写了一个，还是vlayout的示例，如果有幸看到了这个项目，觉得有点帮助的话请给一个小星星，谢谢，祝秀发浓密 └(^o^)┘
