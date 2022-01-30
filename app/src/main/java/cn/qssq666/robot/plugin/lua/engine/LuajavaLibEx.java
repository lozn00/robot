package cn.qssq666.robot.plugin.lua.engine;


import org.luaj.vm2.lib.jse.LuajavaLib;

/**
 * Created by qssq on 2018/12/23 qssq666@foxmail.com
 */
public class LuajavaLibEx extends LuajavaLib {

    protected Class classForName(String name) throws ClassNotFoundException {
        return Class.forName(name, true, LuajavaLibEx.class.getClassLoader());
//		return Class.forName(name, true, ClassLoader.getSystemClassLoader());
    }

}
