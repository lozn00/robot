package cn.qssq666.robot.interfaces;

/**
 * Created by luozheng on 15/12/2.
 */
public interface IResultNotify<Param,Result> {
     Result onNotify(Param param);
}
