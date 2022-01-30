package cn.qssq666.robot.interfaces;

import cn.qssq666.robot.constants.ControlCode;

/**
 * Created by qssq on 2018/1/21 qssq666@foxmail.com
 */

public interface DelegateSendMsgType {
    int GROUP = -996;
    int PRIVATE = -997;
    int GAG = ControlCode.GAG;
    int AITE = ControlCode.AITE;
    int KICK = ControlCode.KICK;
    int CARDMSG = ControlCode.StrucMSG;
    int DEFAULT = 0;
    int SEND_PIC = ControlCode.PIC;
    int CALL = ControlCode.VOICE_CALL;
}
