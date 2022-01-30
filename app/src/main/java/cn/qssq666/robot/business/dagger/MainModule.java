package cn.qssq666.robot.business.dagger;

import dagger.Module;
import dagger.Provides;

//第一步 添加@Module 注解
//第一步 添加@Module 注解
@Module
public class MainModule {
    //第二步 使用Provider 注解 实例化对象
    @Provides
    Object providerA() {
        return new Object();
    }
}
