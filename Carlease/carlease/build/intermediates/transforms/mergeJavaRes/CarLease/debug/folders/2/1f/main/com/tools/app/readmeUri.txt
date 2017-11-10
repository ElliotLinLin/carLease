
通过Uri打开界面

配置：
在AndroidManifest.xml加入
<activity android:name="com.tools.test.TestURIUI1" >
            <intent-filter >
                <action android:name="android.intent.action.VIEW" />

                <category android:name="android.intent.category.DEFAULT" />

                <data
                    android:host="com.baidu.aaa.bbb.ccc"
                    android:scheme="baidu" >
                </data>
            </intent-filter>
        </activity>
        

一定要包含<action android:name="android.intent.action.VIEW" />
还有<data />


调用：
Uri uri = Uri.parse("baidu://com.baidu.aaa.bbb.ccc");
AbsUI.startUri(context, uri);


