package cn.qssq666;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//字段 本地变量 类 接口 包名 
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE, ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.CLASS)// RUNTIME字节码级别。 CLASS字节码级别
public @interface StringIgnores {

}
