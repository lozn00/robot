#qqrobot1



  /*            String arg0 = getArgByArgArr(args, sArgFirst);
                String arg1 = getArgByArgArr(args, sArgSecond);
                String arg2 = getArgByArgArr(args, sArgSecond);*/

                long gagTime = IGnoreConfig.DEFAULT_GAG_TIME_SHORT;
                String group = item.getFrienduin();


                if (MsgTyeUtils.isGroupMsg(item)) {
                    doGagFromGroupMsgCmd(item, args);
                } else {
                    doGagCmdPrivateMsgCmd(item, args);
                }


                String account = null;
                android.support.v4.util.Pair<Boolean, android.support.v4.util.Pair<Boolean, List<GroupAtBean>>> atPair = ConfigUtils.clearAndFetchAtArray(item);
                if (atPair.first) {

                } else if (RobotUtil.isEmptyArg(args) && MsgTyeUtils.isGroupMsg(item)) {
                    account = FloorUtils.getFloorQQ(group);
                    if (TextUtils.isEmpty(account)) {
                        notifyJoinReplaceMsgJump("无法禁言,因为未发现楼层数据 需要指定禁言的人", item);
                        return true;
                    }

                } else if (args.length >= 3) {
                    group = getArgByArgArr(args, sArgFirst);
                    account = getArgByArgArr(args, sArgSecond);
                    gagTime = ParseUtils.parseGag(getArgByArgArr(args, sArgThrid));
                    if (FloorUtils.isFloorData(account)) {
                        account = FloorUtils.getFloorQQ(group, account);
                        if (TextUtils.isEmpty(account)) {
                            notifyJoinReplaceMsgJump(FloorUtils.getFloorInputDataMsg(account), item);
                            return true;
                        }
                    } else {
                        Pair<Integer, Integer> pair = FloorUtils.parseMultiFloorData(account);
                        if (pair != null) {
                            List<MsgItem> floors = FloorUtils.getFloors(group, pair.first, pair.second);

                            FloorMultiUtils.doMultiGagLogic(RobotContentProvider.this, item, floors, group, gagTime);
                            return true;
                        }
                    }


                } else {
                    if (args.length == 2) {
                        if (MsgTyeUtils.isGroupMsg(item)) {//群消息发送 禁言 1  1000
                            String arg0 = getArgByArgArr(args, sArgFirst);
                            String gagTimeStr = getArgByArgArr(args, sArgSecond);

                            gagTime = ParseUtils.parseGag(gagTimeStr);
                            if (gagTime < 0) {
                                gagTime = 0;
                            }
                            if (FloorUtils.isFloorData(arg0)) {
                                String tempQQ = FloorUtils.getFloorQQ(group, arg0);
                                if (tempQQ != null) {
                                    account = tempQQ;
                                } else {
                                    notifyJoinReplaceMsgJump(FloorUtils.getFloorInputDataMsg(tempQQ), item);
                                    return true;
                                }


                            } else {

                                Pair<Integer, Integer> pair = FloorUtils.parseMultiFloorData(arg0);
                                if (pair != null) {
                                    List<MsgItem> floors = FloorUtils.getFloors(group, pair.first, pair.second);

                                    FloorMultiUtils.doMultiGagLogic(RobotContentProvider.this, item, floors, group, gagTime);
                                    return true;
                                } else {
                                    account = arg0;

                                }
                            }


                        } else {//2个参数 私聊 禁言 群  编号
                            group = getArgByArgArr(args, sArgFirst);
                            account = getArgByArgArr(args, sArgSecond);
                            if (FloorUtils.isFloorData(account)) {//先检测长度
                                account = FloorUtils.getFloorQQ(group, account);
                                if (TextUtils.isEmpty(account)) {
                                    notifyJoinReplaceMsgJump(FloorUtils.getFloorInputDataMsg(account), item);
                                    return true;
                                } else {
                                    //验证正确 是楼层
                                }
                            } else {

                                Pair<Integer, Integer> pair = FloorUtils.parseMultiFloorData(account);
                                if (pair != null) {
                                    List<MsgItem> floors = FloorUtils.getFloors(group, pair.first, pair.second);

                                    FloorMultiUtils.doMultiGagLogic(RobotContentProvider.this, item, floors, group, gagTime);
                                    return true;
                                }
                            }

                        }


                    } else if (args.length == 1) {
                        if (MsgTyeUtils.isGroupMsg(item)) {
                            account = getArgByArgArr(args, sArgFirst);
                            if (FloorUtils.isFloorData(account)) {
                                String floorQQ = FloorUtils.getFloorQQ(group, account);
                                if (floorQQ != null) {
                                    account = floorQQ;
                                } else {
                                    notifyJoinReplaceMsgJump(FloorUtils.getFloorInputDataMsg(group), item);
                                    return true;
                                }

                            }
                            {
                                Pair<Integer, Integer> pair = FloorUtils.parseMultiFloorData(account);
                                if (pair != null) {//如果是禁言1-5
                                    List<MsgItem> floors = FloorUtils.getFloors(group, pair.first, pair.second);

                                    FloorMultiUtils.doMultiGagLogic(RobotContentProvider.this, item, floors, group, gagTime);
                                    return true;
                                } else if (!RegexUtils.checkNoSignDigit(account)) {//填写的是禁言1分钟
                                    String arg = account;
                                    gagTime = ParseUtils.parseGag(arg);
                                    account = FloorUtils.getFloorQQ(group);
                                }
                            }

//                            gagTime = ParseUtils.parseGag(IGnoreConfig.DEFAULT_GAG_TIME_STR);
                        } else {
                            //那么第一个参数传递的是群号了 而楼层默认为0
                            group = getArgByArgArr(args, sArgFirst);

                            account = FloorUtils.getFloorQQ(group);//没有指定楼层 那么默认是0 楼


                            if (TextUtils.isEmpty(account)) {
                                notifyJoinReplaceMsgJump("无法禁言,私聊需要指定群号和q号", item);
                                return true;
                            }
                        }
                    } else {

                        notifyJoinReplaceMsgJump("无法禁言,参数不正确", item);
                        return true;
                    }
                }
                if (!RegexUtils.checkNoSignDigit(account)) {
                    notifyJoinReplaceMsgJump("操作失败,只支持qq号楼层号,你填写的命令中QQ账号非法", item);
                    return true;
                }

                if (MsgTyeUtils.isSelfMsg(item, account)) {
                    notifyJoinReplaceMsgJump("当前锁定的用户是机器人自身,无法进行禁言", item);
                    return true;
                }

                if (isManager(account)) {
                    notifyJoinReplaceMsgJump("" + NickNameUtils.formatNickname(account, account) + "是管理员,你禁言他作甚", item);
                    return true;
                }

                MsgItem gagMsgItem = item.clone();
                gagMsgItem.setIstroop(1);
                gagMsgItem.setSenderuin(account);
                gagMsgItem.setFrienduin(group);
                gagMsgItem.setNickname(NickNameUtils.queryMatchNickname(group, account, false));
                gagMsgItem.setMessage(gagTime + "");
                item.setNickname(gagMsgItem.getNickname());
                //设置结果在群里触发
                item.setFrienduin(group);
                item.setIstroop(1);
                notifyGadPersonMsgNoJump(gagTime, gagMsgItem);

                String nickname;
                if (ConfigUtils.isDisableAtFunction(this)) {
                    nickname = NickNameUtils.formatNicknameFromNickName(gagMsgItem.getSenderuin(), gagMsgItem.getNickname());
                } else {
                    nickname = gagMsgItem.getNickname();
                }


                if (gagTime <= 0) {
                    notifyAtMsgJump(gagMsgItem.getSenderuin(), gagMsgItem.getNickname(), "请求解除" + nickname + "的禁言", item);
                } else {
                    notifyAtMsgJump(gagMsgItem.getSenderuin(), gagMsgItem.getNickname(), "请求禁言" + nickname + ",禁言时间" + DateUtils.getGagTime(gagTime), item);


                    //                    notifyJoinReplaceMsgJump("请求禁言" + NickNameUtils.formatNickname(account, account) + ",禁言时间" + DateUtils.getGagTime(gagTime), item);
                }

                