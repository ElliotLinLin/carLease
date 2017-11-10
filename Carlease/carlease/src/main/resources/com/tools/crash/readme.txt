
类名：AbsExceptionHandler.java  抽象类，需要子类实现二个方法中的一个。
包名：com.crash
说明：完善的模块。
功能：捕获全局错误。
依赖：
权限:


类名：FileCrashHandler.java 实现AbsExceptionHandler.java中的两个方法其中一个。
包名：com.crash
说明：完善的模块。
功能：捕获全局错误，并且保存到本地文件。
依赖：FileUtil.java VersionUtil.java
权限: android.permission.WRITE_EXTERNAL_STORAGE


类名：CrashApplication.java
包名：com.crash
说明：完善的模块。
功能：替换程序的Application
依赖：
权限:


