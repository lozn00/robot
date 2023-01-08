package cn.qssq666.robot.constants;

import kotlin.Metadata;

import org.jetbrains.annotations.NotNull;

/*@Metadata(
   mv = {1, 5, 1},
   k = 1,
   d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"},
   d2 = {"Lcn/qssq666/robot/constants/DefaultTipUtil;", "", "()V", "hostNameAndTip", "", "getHostNameAndTip", "()Ljava/lang/String;", "Robot.app"}
)*/

public final class DefaultTipUtil {
    @NotNull
    public static final DefaultTipUtil INSTANCE;

    @NotNull
    public final String getHostNameAndTip() {
        return "宿主";
    }

    private DefaultTipUtil() {
    }

    static {
        DefaultTipUtil var0 = new DefaultTipUtil();
        INSTANCE = var0;
    }
}

/*

package cn.qssq666.robot.constants
object DefaultTipUtil {
        val hostNameAndTip: String
        get() = "宿主(q++/情迁抢包等软件)"
        }



 */