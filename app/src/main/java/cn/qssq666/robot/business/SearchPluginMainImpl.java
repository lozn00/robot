package cn.qssq666.robot.business;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.constants.AppConstants;
import cn.qssq666.robot.plugin.sdk.interfaces.AtBeanModelI;
import cn.qssq666.robot.plugin.sdk.interfaces.IApiCallBack;
import cn.qssq666.robot.plugin.sdk.interfaces.IMsgModel;
import cn.qssq666.robot.plugin.sdk.interfaces.PluginControlInterface;
import cn.qssq666.robot.plugin.sdk.myimpl.PluginControlmpl;
import cn.qssq666.robot.utils.DensityUtil;
import cn.qssq666.robot.utils.HttpUtilOld;
import cn.qssq666.robot.utils.PicPathUtil;
import cn.qssq666.robot.utils.RegexUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by qssq on 2018/7/19 qssq666@foxmail.com
 */
//cn.qssq666.robot.plugin.sdk.control.PluginMainImpl
public class SearchPluginMainImpl {
    private static final String TAG = "PluginMainImpl";

    String mLastMsg = "";
    private static HashMap<String, String> map;

    public static void doSendCacheDir(MsgItem item, String text) {
        File dir = new File(PicPathUtil.getPicRootdir(), text.contains("美女") ? "girl" : "other");
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            File file = files[new Random().nextInt(files.length)];
            if (file.isFile()) {
                getControlApi().sendPicMsg(item, item.getFrienduin(), item.getSenderuin(), file.getAbsolutePath());
                getControlApi().sendMsg(item.setMessage(dir.getAbsolutePath() + "目录图片:" + file.getName()));
            }else{
                getControlApi().sendMsg(item.setMessage(dir.getAbsolutePath() + "目录的"+file.getName()+"不是图片"));
            }
        } else {
            getControlApi().sendMsg(item.setMessage(dir.getAbsolutePath() + "目录没有图片!"));

        }
    }

    @Deprecated
    private boolean doLogic(final IMsgModel item) {
        String message = item.getMessage();


        //todo
        if (message.startsWith("t2p")) {


            String text = message.substring("t2p".length(), message.length()).trim();
//            if (doText2PicLogic(item, text)) return true;


            return true;

        } else if (message.startsWith("搜")) {

            boolean b = MsgTyeUtils.isGroupMsg(item) && RobotContentProvider.getInstance().getConfigQueryImpl().checkSensitiveWordAndUseSystemGag(item);//用于检测是否违规的手段
            if (b) {
                return true;//违规勒
            }

            int robotVersion = RobotContentProvider.getInstance().getConfigQueryImpl().getRobotVersion();
            //https://image.baidu.com/search/acjson?tn=resultjson_com&ipn=rj&ct=201326592&is=&fp=result&queryWord=girl&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=-1&z=&ic=0&word=%E5%B8%85%E5%93%A5&s=&se=&tab=&width=&height=&face=0&istype=2&qc=&nc=1&fr=&pn=210&rn=30&gsm=d2&153345

            String searchword = message.substring("搜".length(), message.length()).trim();


            if (doSearchPicLogic(item, searchword)) return true;

            return true;

//            http://image.baidu.com/data/imgs?col=美女&tag=小清新&sort=0&pn=10&rn=10&p=channel&from=1


        } else if (message != null && message.equals("看美女")) {

            getControlApi().sendMsg(item.setMessage(AppConstants.ACTION_OPERA_NAME + "搜索相关美女图片中,请稍等片刻!"));


            Random random = new Random();

            final int randomp = random.nextInt(1300);
            ;

            //https://image.baidu.com/search/acjson?tn=resultjson_com&logid=&ipn=rj&ct=201326592&is=&fp=result&queryWord=%E7%BE%8E%E5%A5%B3+%E6%B8%85%E7%BA%AF&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=-1&z=&ic=0&hd=&latest=&copyright=&word=%E7%BE%8E%E5%A5%B3+%E6%B8%85%E7%BA%AF&s=&se=&tab=&width=&height=&face=0&istype=2&qc=&nc=1&fr=&expermode=&nojc=&cg=girl&pn=30&rn=30&gsm=1e&1628084232250=


            String format = String.format("https://image.baidu.com/search/acjson?tn=resultjson_com&logid=&ipn=rj&ct=&is=&fp=result&queryWord=%E7%BE%8E%E5%A5%B3+%E6%B8%85%E7%BA%AF&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=-1&z=&ic=0&hd=&latest=&copyright=&word=%E7%BE%8E%E5%A5%B3+%E6%B8%85%E7%BA%AF&s=&se=&tab=&width=&height=&face=0&istype=2&qc=&nc=1&fr=&expermode=&nojc=&cg=girl&pn=%s&rn=30&gsm=1e&=", randomp);
//            String format = String.format("http://image.baidu.com/data/imgs?col=美女&tag=小清新&sort=0&pn=%d&rn=1&p=channel&from=1", randomp);


            getControlApi().sendAsyncGetRequest(format, new IApiCallBack<byte[]>() {
                @Override
                public void onSucc(byte[] bytes) {
//                        getControlApi().sendMsg(item.setMessage("搜索" + searchword + "相关图片失败!! code="+code+",exception:"+e.getMessage()));

                    final String s = new String(bytes);

                    Log.w(TAG, "net_pic_result:" + s);

                    if (s.contains("listNum") && s.contains("displayNum")) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);

                            JSONArray imgs = jsonObject.getJSONArray("data");
                            if (imgs.length() > 0 && !imgs.isNull(0)) {

                                //downloadUrl: "http://c.hiphotos.baidu.com/image/pic/item/810a19d8bc3eb135298010bba41ea8d3fd1f446e.jpg",
                                int length = imgs.length();
                                JSONObject jsonObjectImgItem = imgs.getJSONObject(new Random().nextInt(length));
                                final String downloadUrl = jsonObjectImgItem.optString("hoverURL");
                                downloadPic(downloadUrl, item, "girl", "网络美女", randomp);

    /*
                "thumbURL":"https://img1.baidu.com/it/u=1910847619,3799288123&fm=26&fmt=auto&gp=0.jpg",
            "commodityInfo":null,
            "isCommodity":0,
            "middleURL":"https://img1.baidu.com/it/u=1910847619,3799288123&fm=26&fmt=auto&gp=0.jpg",
            "largeTnImageUrl":"",
            "hasLarge":0,
            "hoverURL":"https://img1.baidu.com/it/u=1910847619,3799288123&fm=26&fmt=auto&gp=0.jpg",
            "pageNum":30,
            "objURL":"ipprf_z2C$qAzdH3FAzdH3F2t42d_z&e3Bkwt17_z&e3Bv54AzdH3Ft4w2j_fjw6viAzdH3Ff6v=ippr%nA%dF%dFrtv8_z&e3Botg9aaa_z&e3Bv54%dFrtv%dFb%dFcb%dFv0l8ml8nn9_z&e3B3r2&6juj6=ippr%nA%dF%dFrtv8_z&e3Botg9aaa_z&e3Bv54&wrr=daad&ftzj=ullll,8aaaa&q=wba&g=a&2=ag&u4p=3rj2?fjv=8mnam0m98l&p=c9kc89da08wdbaabbkbjb8kmwcbmk8ak",
            "fromURL":"ippr_z2C$qAzdH3FAzdH3Fooo_z&e3Botg9aaa_z&e3Bv54AzdH3F4jtge0n0ba_m_z&e3Bip4s",
            "fromJumpUrl":"ippr_z2C$qAzdH3FAzdH3Fooo_z&e3Botg9aaa_z&e3Bv54AzdH3F4jtge0n0ba_m_z&e3Bip4s",
            "fromURLHost":"www.win4000.com",
            "currentIndex":"",
            "width":640,
            "height":960,
            "type":"jpg",
            "is_gif":0,
            "isCopyright":0,
            "resourceInfo":null,
            "strategyAssessment":"1342187026_229_0_0",
            "filesize":"",
            "bdSrcType":"0",
            "di":"206030",
            "pi":"0",
            "is":"0,0",
            "imgCollectionWord":"",
            "replaceUrl":[
                Object{...}
            ],
            "hasThumbData":"0",
            "bdSetImgNum":0,
            "partnerId":0,
            "spn":0,
            "bdImgnewsDate":"2016-12-11 00:00",
            "fromPageTitle":"日系美女</strong>写真性感连体泳衣白皙美腿清纯美女",
     */


                            } else {

                                getControlApi().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        getControlApi().sendMsg(item.setMessage(AppConstants.ACTION_OPERA_NAME + "搜索相关图片失败请更换关键词!"));


                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.w(TAG, "无法搜索图,结果 服务器出现问题:" + s);
                            getControlApi().sendMsg(item.setMessage(AppConstants.ACTION_OPERA_NAME + "搜索相关图片失败 数据格式出现错误!!" + e.getMessage()));

                        }

                    } else {
                        getControlApi().post(new Runnable() {
                            @Override
                            public void run() {
                                Log.w(TAG, "无法搜索图片,结果 服务器出现问题:" + s);
                                getControlApi().sendMsg(item.setMessage(AppConstants.ACTION_OPERA_NAME + "搜索相关图片失败 服务器已失效!"));


                            }
                        });
                    }


                    //http://image.baidu.com/data/imgs?col=%E7%BE%8E%E5%A5%B3&tag=%E5%B0%8F%E6%B8%85%E6%96%B0&sort=0&pn=2&rn=2&p=channel&from=1


                }

                @Override
                public void onFail(final int code, final Exception e) {

                    getControlApi().post(new Runnable() {
                        @Override
                        public void run() {
                            getControlApi().sendMsg(item.setMessage(AppConstants.ACTION_OPERA_NAME + "搜索相关图片失败!! code=" + code + ",exception:" + e.getMessage()));


                        }
                    });

                }
            });

            return true;

//            http://image.baidu.com/data/imgs?col=美女&tag=小清新&sort=0&pn=10&rn=10&p=channel&from=1


        } else if (message.trim().equals("打赏")) {


            File file = new File(PicPathUtil.getPicRootdir(), "shang");

            if (file.isDirectory() && file.list() != null && file.list().length > 0) {
                File[] list = file.listFiles();

//            int i = new Random().nextInt(list.length);
                int shang_index = getControlApi().readIntConfig("shang_index", 0);
                if (shang_index >= list.length || shang_index < 0) {

                    shang_index = 0;
                } else {
                }

                Log.w(TAG, "shang index" + shang_index + ",maxlength:" + list.length);

                String picPath = list[shang_index].getAbsolutePath();

                getControlApi().sendPicMsg(item, item.getFrienduin(), item.getSenderuin(), picPath);

                shang_index++;
                getControlApi().writeConfig("shang_index", shang_index);
                Log.w(TAG, "shang" + picPath);
                return true;

            } else {

                getControlApi().sendMsg(item.setMessage("无法发送图片,因为" + file.getAbsolutePath() + "文件夹里面没有图片"));

            }

        } else if (message.equals("王思聪")) {

            File file = new File(PicPathUtil.getPicRootdir(), "wang");

            if (file.isDirectory() && file.list() != null && file.list().length > 0) {
                File[] list = file.listFiles();

                int i = new Random().nextInt(list.length);
                String picPath = list[i].getAbsolutePath();

                getControlApi().sendPicMsg(item, item.getFrienduin(), item.getSenderuin(), picPath);


                Log.w(TAG, "wang" + picPath);
                return true;

            } else {

                getControlApi().sendMsg(item.setMessage("无法发送图片,因为" + file.getAbsolutePath() + "文件夹里面没有图片"));

            }

        }


        return false;
    }

    public static boolean doText2PicLogic(IMsgModel item, String fontColor, String bgColor, String text) {
        if (TextUtils.isEmpty(text)) {
            MsgReCallUtil.notifyHasDoWhileReply(RobotContentProvider.getInstance(), "请输入文字", item);
            return true;
        }
        String[] split = null;
        if (text.contains("\n")) {
            split = text.split("\n");

        } else if (text.contains(" ")) {
            split = text.split(" ");

        } else if (text.contains(",")) {
            split = text.split(",");

        } else {
            split = text.split(" ");
        }
        text.split(" ");
        textArr2PicAndSend(item, text, split, fontColor, bgColor, true);
        return false;
    }

    /**
     * @param item
     * @param searchword 多个图片，如果是数字2个参数，否则可以多个参数关键词搜索
     * @return
     */

    public static boolean doSearchPicLogic(final IMsgModel item, String searchword) {
        int randomOrStartPage;
        String[] split = searchword.split(" ");
        int useSearchMultiPicMode = 0;
        int searchStart = 0;
        if (split != null && split.length == 2) {

            if (RegexUtils.checkDigit(split[1])) {

                try {

                    searchword = split[0];
                    randomOrStartPage = Integer.parseInt(split[1]);
                    if (randomOrStartPage < 100 && randomOrStartPage > 1) {
                        if (RobotContentProvider.getInstance().isManager(item.getSenderuin()) || randomOrStartPage < 5) {
                            useSearchMultiPicMode = randomOrStartPage;
                            randomOrStartPage = 0;
                        }
                    }

                } catch (Exception e) {
                    Random random = new Random();
                    randomOrStartPage = random.nextInt(300);
                }

            } else {
                Pair<Integer, Integer> pair = FloorUtils.parseMultiFloorData(split[1]);
                if (pair != null && (RobotContentProvider.getInstance().isManager(item.getSenderuin()) || (pair.second - pair.first < 5))) {

                    //if (RobotContentProvider.getInstance().isManager(item.getSenderuin())) {
                    randomOrStartPage = pair.first;
                    useSearchMultiPicMode = (pair.second - pair.first) + 1;
                } else {
                    Random random = new Random();
                    randomOrStartPage = random.nextInt(300);
                }

            }


            /*

               Pair<Integer, Integer> pair = FloorUtils.parseMultiFloorData(argZan);
             */
        } else {


            Random random = new Random();
            randomOrStartPage = random.nextInt(300);
        }
/*
IllegalArgumentException: unexpected url:
        at okhttp3.Request$Builder.url(Proguard:143)
 */

        if (TextUtils.isEmpty(searchword)) {

            getControlApi().sendMsg(item.setMessage(AppConstants.ACTION_OPERA_NAME + "搜图需要填写名称"));
        } else {


            if (searchword.length() > 30) {
                getControlApi().sendMsg(item.setMessage(AppConstants.ACTION_OPERA_NAME + "搜图名字过长!"));
                return true;

            }

/*

            String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(searchword);
            if (m.matches()) {

                getControlApi().sendMsg(item.setMessage("无法搜" + searchword + ",里面包含特殊符号,请删除"));
                return true;
            }
*/

//                String format = String.format("https://image.baidu.com/search/acjson?tn=resultjson_com&ipn=rj&ct=201326592&is=&fp=result&queryWord=%s&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=-1&z=&ic=0&word=%s=&se=&tab=&width=&height=&face=0&istype=2&qc=&nc=1&fr=&pn=%d&rn=1&gsm=d2&153345", searchword,searchword,randomp);

            String format = "https://image.baidu.com/searc" +
                    "h/acjson?tn=resultjson_com&ipn=rj&ct=201326592&is=&fp=resu" +
                    "lt&queryWord=" + encodeParam(searchword) + "&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=-" +
                    "1&z=&ic=&word=" + encodeParam(searchword) + "&s=&" +
                    "se=&tab=&width=&height=&face=&istype=&qc=&nc=1&fr=&pn" +
                    "=" + randomOrStartPage + "&rn=" + (useSearchMultiPicMode > 0 ? useSearchMultiPicMode : 1) + "&gsm=78&1533453669527";


            format = String.format("https://image.baidu.com/search/acjson?tn=resultjson_com&logid=" +
                    "&ipn=rj&ct=&is=&fp=result&queryWord=%s&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=" +
                    "-1&z=&ic=0&hd=&latest=&copyright=&word=" +
                    "%s&s=&se=&tab=&width=&height=&face=0&istype=2&qc=&nc=1&fr=&expermode=&nojc=&cg=girl&pn=%d&rn=%d&gsm=1e&=", encodeParam(searchword), encodeParam(searchword), randomOrStartPage, (useSearchMultiPicMode > 0 ? useSearchMultiPicMode : 1));
//             format =String.format("https://image.baidu.com/search/acjson?tn=resultjson_com&logid=&ipn=rj&ct=&is=&fp=result&queryWord=%E7%BE%8E%E5%A5%B3+%E6%B8%85%E7%BA%AF&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=-1&z=&ic=0&hd=&latest=&copyright=&word=%E7%BE%8E%E5%A5%B3+%E6%B8%85%E7%BA%AF&s=&se=&tab=&width=&height=&face=0&istype=2&qc=&nc=1&fr=&expermode=&nojc=&cg=girl&pn=%d&rn=30&gsm=1e&=",randomOrStartPage);


            Log.w(TAG, "处理之后的图片:" + format);
            final int finalRandompage = randomOrStartPage;
            final String finalSearchword = searchword;
            final int finalRandompage1 = randomOrStartPage;
            final int finalUseSearchMultiPicMode = useSearchMultiPicMode;
            map = new HashMap<>();
            https:
//m.baidu.com,https://www.baidu.com,http://m.baidu.com,http://www.baidu.com
            map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.164 Safari/537.36");
            map.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
            map.put("Connection", "keep-alive");
            map.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:60.0) Gecko/20100101 Firefox/60.0");
            map.put("Upgrade-Insecure-Requests", "1");


            HttpUtilOld.queryGetData(format, map, false, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    getControlApi().post(new Runnable() {
                        @Override
                        public void run() {
                            getControlApi().sendMsg(item.setMessage(AppConstants.ACTION_OPERA_NAME + "搜索[" + finalSearchword + "]相关图片失败!! " + ",exception:" + e.getMessage()));


                        }
                    });

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
//                        getControlApi().sendMsg(item.setMessage("搜索" + searchword + "相关图片失败!! code="+code+",exception:"+e.getMessage()));
                    int code = response.code();
                    if (code != 200) {
                        Log.w(TAG, "无法搜索图片" + finalSearchword + ",结果 服务器错误 结果码:" + code);
                        getControlApi().sendMsg(item.setMessage(AppConstants.ACTION_OPERA_NAME + "搜索" + finalSearchword + "相关图片失败 网络错误!!"));
                        return;
                    }
                    final String s = response.body().string();


                    Log.w(TAG, "search " + finalSearchword + " result :" + s);
                    if (s.contains("listNum") && s.contains("queryEnc")) {
                        try {
                            final JSONObject jsonObject = new JSONObject(s);

                            JSONArray imgs = jsonObject.getJSONArray("data");


                            if (finalUseSearchMultiPicMode > 0) {


                                if (imgs.length() < 0) {

                                    getControlApi().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            getControlApi().sendMsg(item.setMessage(AppConstants.ACTION_OPERA_NAME + "无法搜索" + finalSearchword + "相关图片失败,你请求的搜索" + finalUseSearchMultiPicMode + "张图片请求被拒绝，请重试"));


                                        }
                                    });
                                } else {


                                    int count = imgs.length() > finalUseSearchMultiPicMode ? finalUseSearchMultiPicMode : imgs.length();
                                    for (int i = 0; i < count; i++) {

                                        JSONObject currentPicObject = imgs.optJSONObject(i);

                                        if (currentPicObject != null && !TextUtils.isEmpty(currentPicObject.optString("hoverURL"))) {

                                            //hoverURL
                                            final String downloadUrl = currentPicObject.optString("hoverURL");

                                            downloadPic(downloadUrl, item, "other", finalSearchword, finalRandompage, false);


                                        }


                                    }


                                }


                            } else {


                                if (imgs.length() > 0 && !imgs.isNull(0) && !TextUtils.isEmpty(imgs.getJSONObject(0).optString("hoverURL"))) {

                                    if (finalUseSearchMultiPicMode == 0) {

                                        getControlApi().post(new Runnable() {
                                            @Override
                                            public void run() {
                                                String bdFmtDispNum = jsonObject.optString("bdFmtDispNum");
                                                getControlApi().sendMsg(item.setMessage("正在下载[" + finalSearchword + "" + "]图片,共" + bdFmtDispNum + "张图片,随机从第一个列表中取第" + (finalRandompage - 1) + "张"));
                                            }
                                        });
                                    }
                                    //hoverURL
                                    JSONObject jsonObjectFirst = imgs.getJSONObject(0);
                                    final String downloadUrl = jsonObjectFirst.optString("hoverURL");


                                    downloadPic(downloadUrl, item, "other", finalSearchword, finalRandompage);


                                } else {

                                    getControlApi().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            getControlApi().sendMsg(item.setMessage(AppConstants.ACTION_OPERA_NAME + "搜索" + finalSearchword + "相关图片失败请尝试再搜索词后面添加数字 指定查看第几页图片,当前随机页(" + finalRandompage1 + ")没有图片!"));


                                        }
                                    });
//hoverURL
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.w(TAG, "无法搜索图片" + finalSearchword + ",结果 服务器出现问题:" + s);
                            getControlApi().sendMsg(item.setMessage(AppConstants.ACTION_OPERA_NAME + "搜索" + finalSearchword + "相关图片失败 数据格式出现错误!!" + e.getMessage()));

                        }

                    } else {
                        getControlApi().post(new Runnable() {
                            @Override
                            public void run() {
                                Log.w(TAG, "无法搜索图片" + finalSearchword + ",结果 服务器出现问题:" + s);
                                String str = "";
                                if (s.contains("antiFlag")) {

                                    str = JSON.parseObject(s).getString("message");
                                }
                                getControlApi().sendMsg(item.setMessage(AppConstants.ACTION_OPERA_NAME + "搜索" + finalSearchword + "相关图片失败 服务器已失效!" + str));


                            }
                        });
                    }

                    //http://image.baidu.com/data/imgs?col=%E7%BE%8E%E5%A5%B3&tag=%E5%B0%8F%E6%B8%85%E6%96%B0&sort=0&pn=2&rn=2&p=channel&from=1


                }

            });
        }
        return false;
    }

    public static boolean textArr2PicAndSend(IMsgModel item, String text, String[] split, String fontColor, String bgColor, boolean fromcast) {


        File textRootdir = PicPathUtil.getTextRootdir();
        if (!textRootdir.exists()) {
            textRootdir.mkdirs();


        }
        String savePath = new File(textRootdir, "" + (text.length() >= 10 ? text.substring(0, 9) : text) + "_" + text.hashCode() + ".jpg").getAbsolutePath();


        boolean b = text2bitmapfile(split, fontColor, bgColor, savePath);
        if (!b) {

            if (fromcast) {
                getControlApi().sendMsg(item.setMessage("转换图片失败!"));

            } else {
                getControlApi().sendMsg(item.setMessage(text));
            }
            return true;
        } else {
            getControlApi().sendPicMsg(item, item.getFrienduin(), item.getSenderuin(), savePath);

        }


        return true;
    }

    public static boolean text2bitmapfile(String[] split, String savePath) {
        return text2bitmapfile(split, null, null, savePath);
    }


    public static boolean text2bitmapfile(String[] split, String fontColor, String bgColor, String savePath) {
        Paint paint = new Paint();

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStrokeWidth(15);

        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(15);
        try {
            if (fontColor == null) {
                paint.setColor(Color.RED);
            }
            {

                paint.setColor(Color.parseColor(fontColor));
            }


        } catch (Exception e) {
            paint.setColor(Color.RED);
        }

        Rect rect = new Rect();

        int maxLineIndex = getMaxWidthLineIndex(split);
        String maxText = split[maxLineIndex];


        paint.getTextBounds(maxText, 0, maxText.length(), rect);


        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int length = split.length;


        float total = (length - 1) * (-top + bottom) + (-fontMetrics.ascent + fontMetrics.descent);
        float offset = total / 2 - bottom;
        int totalHeight = (int) (total + (30));


        int picWidth;

        if (rect.width() > 200) {
            picWidth = 200;
        } else {
            picWidth = rect.width() + DensityUtil.dip2px(RobotContentProvider.getInstance().getProxyContext(), 5);
        }

        Bitmap bitmap = Bitmap.createBitmap(picWidth, totalHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        try {
            if (bgColor == null) {
                canvas.drawColor(Color.WHITE);
            } else {

                canvas.drawColor(Color.parseColor(bgColor));
            }


        } catch (Exception e) {
            canvas.drawColor(Color.WHITE);
        }

        Point point = new Point(canvas.getWidth() / 2, canvas.getHeight() / 2);


        textCenter(split, paint, canvas, point, Paint.Align.CENTER);

//            textCenterByWH(split, paint, canvas, canvas.getWidth(), canvas.getHeight(), fontHeight, totalHeight);
        // 保存绘图为本地图片
        canvas.save();
        canvas.restore();


        try {
            new File(savePath).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getControlApi().bitmap2File(bitmap, savePath);
    }

    public static void downloadPic(final String downloadUrl, final IMsgModel item, String childdir, final String name, final int randomp) {
        downloadPic(downloadUrl, item, childdir, name, randomp, true);
    }

    public static void downloadPic(final String downloadUrl, final IMsgModel item, String childdir, final String name, final int randomp, final boolean enableTip) {

        if (TextUtils.isEmpty(downloadUrl)) {


            if (enableTip) {

                getControlApi().post(new Runnable() {
                    @Override
                    public void run() {


                        getControlApi().sendMsg(item.setMessage(AppConstants.ACTION_OPERA_NAME + "搜索相关图片失败!!请再试一次,code=" + randomp));


                    }


                });
            }
            return;
        }
        File dir = new File(PicPathUtil.getPicRootdir(), childdir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        int i = downloadUrl.lastIndexOf("/");
        String postfix = "";
        if (i != -1) {
            postfix = downloadUrl.substring(i, downloadUrl.length());
            if(!postfix.contains("jpg")
                    &&!postfix.contains("png")
                    &&!postfix.contains("gif")
            ){
                postfix = ".jpg";
            }

        } else {
            postfix = ".jpg";
        }
        final File fileSavePath = new File(dir, name + "" + downloadUrl.hashCode() + postfix);
        try {


            HttpUtilOld.queryGetData(downloadUrl, map, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (enableTip) {
                        getControlApi().post(new Runnable() {
                            @Override
                            public void run() {
                                getControlApi().sendMsg(item.setMessage("下载美女图片失败!\n图片地址:" + downloadUrl + "\n保存路径:" + fileSavePath.getAbsolutePath() + "\n错误原因 code=" + e.getMessage() + ",message=" + e.getMessage()));
                            }
                        });

                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        byte[] bytes = response.body().bytes();
                        File file = PluginControlmpl.bytes2File(bytes, fileSavePath.getAbsolutePath());
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                        if (fileSavePath.exists()) {

                            Log.w(TAG, "下载美女图片" + downloadUrl + "成功,保存的路径:" + fileSavePath);

                            getControlApi().post(new Runnable() {
                                @Override
                                public void run() {
                                    getControlApi().sendPicMsg(item, item.getFrienduin(), item.getSenderuin(), fileSavePath.getAbsolutePath());

                                }

                            });
                        } else {
                            if (enableTip) {
                                getControlApi().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        getControlApi().sendMsg(item.setMessage("下载美女图片失败! 无法得知原因!\n图片地址:" + downloadUrl + "\n保存路径:" + fileSavePath.getAbsolutePath()));


                                    }
                                });
                            }

                        }

                    } catch (Throwable e) {
                        if (enableTip) {
                            getControlApi().post(new Runnable() {
                                @Override
                                public void run() {
                                    getControlApi().sendMsg(item.setMessage("下载美女图片失败!\n图片地址:" + downloadUrl + "\n保存路径:" + fileSavePath.getAbsolutePath() + "\n错误原因 " + e.getMessage() + ",message=" + e.getMessage()));

                                }
                            });

                        }
                    }
                }
            });

        } catch (final Exception e) {

            getControlApi().post(new Runnable() {
                @Override
                public void run() {
                    RobotContentProvider.getInstance().getPluginControlInterface().sendMsg(item.setMessage("下载美女图片失败!\n搜索词:" + name + "\n图片地址:" + downloadUrl + "\n保存路径:" + fileSavePath.getAbsolutePath() + "\n错误原因,message=" + e.getMessage()));

                }
            });


        }
    }


    private void textCenterByWH(String[] strings, Paint paint, Canvas canvas, int width, int height, float fontHeighXXt, float textTotalhEIGHT) {

        int length = strings.length;
        int startx = width / 2;
        int starty = (int) ((fontHeighXXt / textTotalhEIGHT) / 2);
        Log.w(TAG, "starty:" + starty);
        for (int i = 0; i < length; i++) {
            Rect rect = new Rect();
            paint.getTextBounds(strings[i], 0, strings[i].length(), rect);

            canvas.drawText(strings[i] + "", startx, starty + 2 + rect.height(), paint);

            starty = starty + rect.height();
            Log.w(TAG, "starty:" + starty);
            //            canvas.drawText(strings[i]+"",startx,starty+yAxis,paint);
        }
    }

    public static void textCenter(String[] strings, Paint paint, Canvas canvas, Point point, Paint.Align aligin) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int length = strings.length;
        float total = (length - 1) * (-top + bottom) + (-fontMetrics.ascent + fontMetrics.descent);
        float offset = total / 2 - bottom;
        for (int i = 0; i < length; i++) {
            float yAxis = -(length - i - 1) * (-top + bottom) + offset;
            canvas.drawText(strings[i] + "", point.x, point.y + yAxis, paint);
        }
    }


    public View onConfigUi(ViewGroup viewGroup) {
        TextView textView = new TextView(viewGroup.getContext());
        textView.setText("使用方法\n发送 看美女 或者看帅哥,或者看图 就能欣赏图片啦,前提是需要在/sdcard/pic文件夹放入 boy ,girl other文件夹 然后分别放入对应的图片哦,本程序插件免费开源!欢迎各位开发新的插件哦!温馨提示,插件不支持自己测试触发,必须借助非机器人qq发送消息测试! \n最后消息:" + mLastMsg);


        return textView;
    }

    public int getMinRobotSdk() {
        return 0;
    }

    public static String encodeParam(String word) {
        try {
            return URLEncoder.encode(word, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "毛主席";
    }

    public static int getRandomPage(String word, Random random) {

        int maxRandom = 300;
        if (word.equals("美女") || word.equals("帅哥") || word.equals("男人") || word.equals("女人") || word.equals("图片") || word.equals("壁纸") || word.equals("照片") || word.equals("风景") || word.equals("动物")) {

            maxRandom = 2500;
        } else if (word.length() < 3) {
            maxRandom = 400;
        } else if (word.length() < 5) {
            maxRandom = 300;
        } else if (word.length() < 7) {
            maxRandom = 150;
        } else if (word.length() < 15) {
            maxRandom = 60;
        } else if (word.length() < 30) {
            maxRandom = 10;
        } else {
            maxRandom = 2;
        }


        return random.nextInt(maxRandom);
    }

    public boolean onReceiveRobotFinalCallMsgIsNeedIntercept(IMsgModel item, List<AtBeanModelI> list, boolean aite, boolean haisaiteme) {
        String message = item.getMessage();
        Log.w(TAG, "final recall msg:" + item.getMessage());

        if (message.length() <= sMaxLength) {
//            textArr2PicAndSend(item, message, new String[]{message}, false);
            return true;


        } else {

            if (message.length() > 100) {

                Log.w(TAG, "msg length than ,is  " + message.length());
                return false;

            }
            String[] arr;


            if (message.contains("\n")) {

                arr = message.split("\n");
            } else {
                int length = message.length();
                int arrlength = (int) Math.ceil(length * 1.0f / sMaxLength);

                arr = new String[arrlength];

                int startindex = 0;
                int endIndex = (int) (startindex + sMaxLength);
                int index = 0;

                while (index < arrlength) {

                    Log.w(TAG, "第" + index + "行,startIndex" + startindex + ",endIndex:" + endIndex + ",maxsize:" + length + ",共" + arrlength + "行");

                    arr[index] = message.substring(startindex, endIndex);
                    Log.w(TAG, "[" + arr[index] + "],index;" + index);
//                  startindex=  startindex+ sMaxLength>=length?sMaxLength-1:(int)(startindex+sMaxLength);
                    startindex = startindex + sMaxLength > length ? length : (startindex + sMaxLength);
                    endIndex = (startindex + sMaxLength) > length ? length : (startindex + sMaxLength);


                    index++;


                }
            }
//            textArr2PicAndSend(item, message, arr, false);

        }


        return true;
    }


    public static int getMaxWidthLineIndex(String[] strs) {

        int maxLength = 0;
        int maxIndex = 0;
        for (int i = 0; i < strs.length; i++) {
            String current = strs[i];
            if (current.length() > maxLength) {


                maxLength = current.length();
                maxIndex = i;
            }
        }

        return maxIndex;


    }

    int sMaxLength = 8;

    public static PluginControlInterface getControlApi() {
        return RobotContentProvider.getInstance().getPluginControlInterface();
    }
}
