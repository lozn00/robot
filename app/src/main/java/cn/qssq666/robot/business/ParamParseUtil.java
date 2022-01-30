package cn.qssq666.robot.business;

/**
 * Created by qssq on 2018/1/24 qssq666@foxmail.com
 */

public class ParamParseUtil {
    public static final int sArgFirst = 0;
    /**
     * args 数组里面的第二个元素， 这个里面不包含命令.
     */
    public static final int sArgSecond = 1;
    public static final int sArgThrid = 2;
    //fourth fifth
    public static final int sArgFourth = 3;
    public static final int sArgFifth = 4;

    public static boolean isInvalidArgument(String[] args, int position) {
        if (args == null || args.length == 0 || position < 0 || position >= args.length) {
            return true;
        }
        return false;
    }


    public static String getArgByArgArr(String[] args, int position) {
        if (isInvalidArgument(args, position)) {
            return null;
        }
        return args[position];

    }


    public static String mergeParameters(String[] args, int startPosition) {
        return mergeParameters(args, startPosition, args.length - 1);
    }

    public static String mergeParameters(String[] args, int startPosition, int endPosition) {
        if (isInvalidArgument(args, startPosition) || isInvalidArgument(args, endPosition)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = startPosition; i <= endPosition; i++) {

            sb.append(args[i]);
            if (i != endPosition) {
                sb.append(" ");
            }

        }
        return sb.toString();

    }

    public static String[] subArr(String[] args, int startPosition, int endPosition) {
        if (isInvalidArgument(args, startPosition) || isInvalidArgument(args, endPosition)) {
            return null;
        }
        int count = endPosition - startPosition+1;

        String[] temp = new String[count];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = args[startPosition + i];
        }
        return temp;
    }


}
