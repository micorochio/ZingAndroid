# 我的APP基础架架-Android版


我们希望你能有所收获
并且我们会将用到的开源代码名称列出来


> **编译环境**
+ gradle (3.2.1)
+ android studio (2.2.+)
+ sdk 5.0+
+ ndk 13.1+

> **使用的开源代码们**
+ xUtils的DB模块
+ rxAndroid
+ rxJava
+ retrofit2
+ GSON
+ fresco
+ OKHttp
+ [PersistentCookieJar](https://github.com/franmontiel/PersistentCookieJar/blob/master/README.md) OKHttp的cookie管理工具

# 代码规范
+ 代码按功能模块划分，不同功能模块建立功能名称文件夹,参照前人的代码
+ 修改他人代码，请务必添加修改注释
+ 提交代码时，务必格式化代码
+ 不许使用拼音命名！不许使用拼音命名！不许使用拼音命名！
+ 建议不要使用缩进符号，使用空格
+ 文件命名规范
    + 布局文件`（模块_功能_类型.xml）`如`（login_mian_aty.xml）`
    + 图片文件 `（模块_功能_[用于].png）`如`（login_forgetpassword_btn.png）`
    + 其他资源文件`（模块_功能_[备注]）`

*注意:一个资源被多个模块使用时，资源模块名使用common*



# 提交规范
提交需要声明提交前缀，不加声明则随时可以被回滚

1. 功能添加前缀 `future:`
2. bug修复前缀 `bugfix:`
3. 代码重构、整顿前缀 `refactor:`

# 声明：

不保证兼容所有手机

引用本源码请保留作者名称和出处

源码不建议使用于商业产品

代码上传者请保证可用性，否则被回退、覆盖，概不负责

增、删、修改代码使用者，责任均为使用者自负

代码提交者不要提交、引用不安全的代码


# 数据库模块的使用方法
xUtils的DB模块使用方法一样（因为原来的xUtil对Android6以上的支持不太好，所以只抽出DB模块）
需要数据库读写权限！
```Java
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    public static final String TAG = "ExampleInstrumentedTest";
    @Test
    public void DBTest(){

        DbUtils db = DbUtils.create(this);
        User user = new User(); //这里需要注意的是User对象必须有id属性，或者有通过@ID注解的属性
        user.setEmail("wyouflf@qq.com");
        user.setName("wyouflf");
        db.save(user); // 使用saveBindingId保存实体时会为实体的id赋值

        //...
        // 查找
        Parent entity = db.findById(Parent.class, parent.getId());
        List<Parent> list = db.findAll(Parent.class);//通过类型查找

        Parent Parent = db.findFirst(Selector.from(Parent.class).where("name","=","test"));

        // IS NULL
        Parent Parent = db.findFirst(Selector.from(Parent.class).where("name","=", null));
        // IS NOT NULL
        Parent Parent = db.findFirst(Selector.from(Parent.class).where("name","!=", null));

        // WHERE id<54 AND (age>20 OR age<30) ORDER BY id LIMIT pageSize OFFSET pageOffset
        List<Parent> list = db.findAll(Selector.from(Parent.class)
                                           .where("id" ,"<", 54)
                                           .and(WhereBuilder.b("age", ">", 20).or("age", " < ", 30))
                                           .orderBy("id")
                                           .limit(pageSize)
                                           .offset(pageSize * pageIndex));

        // op为"in"时，最后一个参数必须是数组或Iterable的实现类(例如List等)
        Parent test = db.findFirst(Selector.from(Parent.class).where("id", "in", new int[]{1, 2, 3}));
        // op为"between"时，最后一个参数必须是数组或Iterable的实现类(例如List等)
        Parent test = db.findFirst(Selector.from(Parent.class).where("id", "between", new String[]{"1", "5"}));

        DbModel dbModel = db.findDbModelAll(Selector.from(Parent.class).select("name"));//select("name")只取出name列
        List<DbModel> dbModels = db.findDbModelAll(Selector.from(Parent.class).groupBy("name").select("name", "count(name)"));
        //...

        List<DbModel> dbModels = db.findDbModelAll(sql); // 自定义sql查询
        db.execNonQuery(sql); // 执行自定义sql
        //...

    }
 }

```

# 网络模块
已经引入okhttp和retrofit2，可以自行使用OKHttp，也可以使用简单封装的HttpUtil来发送网络请求。
需要网络权限
*如果不满足提供的默认请求工具，可以了解Retrofit2和RxJava如何搭配使用*

```java
package com.bb.zinglibrary;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by zing on 2016/12/5.
 */

public interface Hahahaha {
    @GET("/")
    Observable<List<String>> baidu();
}
```
```java

package com.bb.zinglibrary;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.bb.zinglibrary.http.RequestCallback;
import com.bb.zinglibrary.http.HttpUtil;
import com.bb.zinglibrary.http.http_client.DefaultOkHttpClient;

import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    public static final String TAG = "ExampleInstrumentedTest";

   @Test
    public void HttpTest() throws Exception {
        // Context of the app under test.
       Context appContext = InstrumentationRegistry.getTargetContext();

       CookieJar cookieJar = DefaultOkHttpClient.getDefaultCookieJar(appContext);

       HttpUtil httpUtil = HttpUtil.getInstance();

       RequestCallback<String> callback = new RequestCallback<String>() {
           @Override
           public void onStart() {
               Log.i("LoginActivity", "onStart: ");
           }

           @Override
           public void onResponse(String o) {
               Log.i(TAG, "onResponse: ");
           }

           @Override
           public void onError(Throwable e) {
               Log.e("LoginActivity", "onError: ", e);
           }

           @Override
           public void onCompleted() {
               Log.i(TAG, "onCompleted:");           }
       };

       OkHttpClient client = DefaultOkHttpClient.getOkHttpClient(true, appContext, null, cookieJar);

       Hahahaha hahahaha = httpUtil.buildNormRequest("http://www.baidu.com", Hahahaha.class, client);

       httpUtil.addCallBack(hahahaha.baidu(), callback);
    }
}

```

# 其他
+ 所有Activity务必继承`com.bb.zing.common.activity.BaseActivity`
+ 使用前，请查看`com.bb.zing.common.utils.ActivityUtil`和`com.bb.zing.common.activity.BaseActivity`是如何搭配使用的
+ 一些功能封装在`package com.bb.zinglibrary.common.AndroidUtils`务必了解，不用重复造轮子
+ `BBApplication`内封装了一些`SharedPreferences`存储方法，不过不建议使用。后期会将其重构到独立的地方，避免影响性能。


