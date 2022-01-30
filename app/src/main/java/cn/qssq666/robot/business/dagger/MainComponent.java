package cn.qssq666.robot.business.dagger;

import cn.qssq666.robot.activity.MainActivity;
import dagger.Component;

@Component(modules = {MainModule.class})
public interface MainComponent {
    //第三步  写一个方法 绑定Activity /Fragment
    void inject(MainActivity activity);
}
