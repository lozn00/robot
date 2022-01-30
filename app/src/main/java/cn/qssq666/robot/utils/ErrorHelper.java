package cn.qssq666.robot.utils;

/**
 * Created by luozheng on 2017/3/8.  qssq.space
 */

public class ErrorHelper {
    /*
    code	说明
0	请求成功
101	请求内容为空
201	请求超时
301	异常信息(json格式错误，500错误等)
401	帐号不合法
403	帐号上传权限已经用尽
404	帐号没有知识库接口使用权限
405	帐号开启安全模式，token校验失败
501	json请求的内容有误
     */
    public static String codeToMessage(int code) {
        String errMsg = null;
        switch (code) {

            case 0:

                errMsg = "请求成功";
                break;
            case 101:
                errMsg = "请求内容为空";
                break;
            case 201:
                errMsg = "请求超时";
                break;
            case 301:
                errMsg = "异常信息(json格式错误，500错误等)";
                break;
            case 401:
                errMsg = "帐号不合法";
                break;
            case 403:
                errMsg = "帐号上传权限已经用尽";
                break;
            case 404:
                errMsg = "帐号没有知识库接口使用权限";
                break;
            case 405:
                errMsg = "帐号开启安全模式，token校验失败";
                break;
            case 501:
                errMsg = "json请求的内容有误";
                break;
            case 40001:
                errMsg = "参数key错误";
                break;
            case 40002:
                //40002
                errMsg = "请求内容info为空";
                break;
            case 40004:
                errMsg = "当天请求次数已使用完";
                break;
            case 40007:
                errMsg = "数据格式异常";
                break;
            default:
                errMsg = "未知错误,code=" + code;
                break;

    /*
       /* 40001	参数key错误
            40002	请求内容info为空
            40004	当天请求次数已使用完
            40007	数据格式异常*/

        }
        return errMsg;
    }

    /*

      if (code == 100000) {

        } else if (code == 200000) {

        } else if (code == 200000) {

        } else if (code == 302000) {

        } else if (code == 308000) {

        } else if (code == 313000) {

        } else if (code == 314000) {

     */
    public static boolean isNotSupportMsgType(int code) {
        switch (code) {
            case 302000:
            case 308000:
            case 313000:
            case 314000:
                return true;
            default:
                return false;
        }

    }
}
