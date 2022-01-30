package com.myopicmobile.textwarrior.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/2/4.
 */

public class ProjectAutoTip {
    public static boolean autotip = true;
    static Map<String,List<Tip>> maps = new HashMap<>();
    static Map<String ,String> mmaps = new HashMap<>();
    public static boolean hasKey(String TAG){
        if (maps.containsKey(TAG)){
            return true;
        }else{
            return false;
        }
    }
    public static Map<String,List<Tip>> getMaps(){
        return maps;
    }
    public static Tip get(String name,int dex){
        if (hasKey(name)){
            return maps.get(name).get(dex);
        }else{
            return null;
        }
    }
    public static int getSize(String name){
        if (hasKey(name)){
            return maps.get(name).size();
        }else{
            return 0;
        }
    }
    public static void on(){
        autotip = true;
    }
    public static void off(){
        autotip = false;
    }
    public static void add(String name,Tip tip){
        if (hasKey(name)){
            maps.get(name).add(tip);
        }else{
            List<Tip> tips = new ArrayList<>();
            maps.put(name,tips);
            add(name,tip);
        }
    }
    public static void add(String tag,String value){
        mmaps.put(tag,value);
    }
    public static Map<String,String> getMmaps(){
        return mmaps;
    }
    public static void add(String tag,String name,String value){
        add(tag,new Tip(name,value));
    }
    public static void clear(){
        maps = new HashMap<>();
        mmaps = new HashMap<>();
    }
    public static List<Tip> getTips(String name){
        return maps.get(name);
    }
    public  static class Tip{
        public String name = "";
        public String value = "";
        public Tip(String name,String value){
            this.name = name;
            this.value = value;
        }
    }
}