package cn.qssq666.robot.constants;

/**
 * Created by qssq on 2017/12/3 qssq666@foxmail.com
 */

public interface ControlCode {
    int SUCC = 0;
    int ON_RECEIVE_MSG_IGNORE = -8888;
    int FAIL = -1;
    int GAG = -1000;
    int KICK = -1001;
    int AITE = -1002;
    int TEST = -1006;
    int StrucMSG = -1003;
    int UNIVERSAL  = -1005;
    int PIC = -1010;

    int QUIT_GROUP=-1100;
    int QUIT_DISCUSSION=QUIT_GROUP+1;//1099
    int MODIFY_GROUP_MEMBER_CARD_NAME=QUIT_DISCUSSION+1;//-1098
    int MODIFY_GROUP_NAME=MODIFY_GROUP_MEMBER_CARD_NAME+1;//-1097


    int ADD_LIKE=MODIFY_GROUP_NAME+1;//-1096
    int INVITE_JOIN_GROUP=ADD_LIKE+1;//1095

      int SEND_MIX_MSG=INVITE_JOIN_GROUP+1;//-1096 图文消息  int SEND_MIX_MSG=INVITE_JOIN_GROUP+1;//-1096 图文消息
    int REVOKE_MSG=SEND_MIX_MSG+1;//-1097 撤回消息
    int REVOKE_MSG_1=REVOKE_MSG;//-1097 撤回消息
    int VOICE_CALL=REVOKE_MSG_1+1;



}
