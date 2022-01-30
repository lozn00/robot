package cn.qssq666.robot.business;

import androidx.core.util.Pair;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import cn.qssq666.robot.BuildConfig;
import cn.qssq666.robot.asynctask.QssqTask;
import cn.qssq666.robot.bean.GroupAtBean;
import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.config.CmdConfig;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.interfaces.MusicModuleI;
import cn.qssq666.robot.utils.BatchUtil;
import cn.qssq666.robot.utils.ConfigUtils;
import cn.qssq666.robot.utils.MapUrlCookie;
import cn.qssq666.robot.utils.NetQuery;
import cn.qssq666.robot.utils.ParseUtils;
import cn.qssq666.robot.utils.StringUtils;
import cn.qssq666.robot.xbean.MusicCardInfo;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by qssq on 2018/1/24 qssq666@foxmail.com
 */

public class MusicMoudle {

    private static final String TAG = "MusicMoudle";
    static MapUrlCookie storeCookies = new MapUrlCookie();

    public static void kuGOuPermissionFix(OkHttpClient.Builder okhttpclientBuilder) {
        Interceptor setHeaderInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Connection", "close")
                        .addHeader("Cache-Control", "max-age=0")
                        .addHeader("Cookie", storeCookies.toString())
                        .addHeader("Accept-Charset", "UTF-8")
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36").build();
//                                addHeader("User-Agent", "Mozilla/5.0(Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.3506.8264 Safari/537.36").build();


                return chain.proceed(request);
            }
        };
//        at java.util.Collections$UnmodifiableCollection.add(Collections.java:928)
        okhttpclientBuilder.addInterceptor(setHeaderInterceptor);
        okhttpclientBuilder.addInterceptor(new OkHttpUtil.ReceivedCookiesInterceptor(new INotify<HashSet<String>>() {
            @Override
            public void onNotify(HashSet<String> param) {
                for (String s : param) {
                    storeCookies.putKeyAndValue(s);
                }
            }
        }));

    }

    public static void fix163PermissionFix(OkHttpClient.Builder okhttpclientBuilder) {
        Interceptor setHeaderInterceptor = new Interceptor() {
            // Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
            //Accept-Encoding:gzip, deflate, sdch
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Host", "s.music.163.com")
                        .addHeader("Cache-Control", "max-age=0")

                        .addHeader("Connection", "close")
                        .addHeader("Accept-Encoding", "gzip, deflate, sdch")
                        .addHeader("Accept", "ext/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
//                        .addHeader(" Proxy-Connection","keep-alive")
//                        .addHeader(" Proxy-Connection","keep-alive")
//                        .addHeader(" Upgrade-Insecure-Requests","1")
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36").build();
//                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.69488.6526 Safari/537.36").build();
     /*           Host:s.music.163.com
                Proxy-Connection:keep-alive
                Upgrade-Insecure-Requests:1*/
//                                addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.69488.6526 Safari/537.36").build();


                return chain.proceed(request);
            }
        };
//        at java.util.Collections$UnmodifiableCollection.add(Collections.java:928)
        okhttpclientBuilder.addInterceptor(setHeaderInterceptor);
        okhttpclientBuilder.addInterceptor(new OkHttpUtil.ReceivedCookiesInterceptor(new INotify<HashSet<String>>() {
            @Override
            public void onNotify(HashSet<String> param) {
                for (String s : param) {
                    storeCookies.putKeyAndValue(s);
                }
            }
        }));

    }

    /**
     * 只支持艾特点歌.
     *
     * @param contentProvider
     * @param argStr
     * @param atPair
     * @param args
     * @param isManager
     */
    public static void onReceiveMusic(final RobotContentProvider contentProvider, final MsgItem item, final String argStr, Pair<Boolean, Pair<Boolean, List<GroupAtBean>>> atPair, final String[] args, boolean isManager) {


        String music = null;
        int count = -1;
        boolean showAll = false;
        final int[] countArr = {0};//总数， index
        boolean showCount = false;
        boolean onlyDownload = false;
        boolean aiteAll = false;
        if (TextUtils.isEmpty(argStr)) {
            MsgReCallUtil.notifyHasDoWhileReply(contentProvider, "支持的命令如下" +
                    "\n" + CmdConfig.FECTCH_MUSIC + "音乐名称" +
                    "\n" + CmdConfig.FECTCH_MUSIC + "音乐名称 5(列出5首歌曲)" +
                    "\n" + CmdConfig.FECTCH_MUSIC + "音乐名称 下载地址(获取音乐直链接)" +
                    "\n" + CmdConfig.FECTCH_MUSIC + "音乐名称 全体(给本群所有人点歌)" +
                    "\n" + CmdConfig.FECTCH_MUSIC + "音乐名称 (艾特一个或者多个人,表示给指定人点歌)" +
                    "\n" + CmdConfig.CONFIG + "" + CmdConfig.FECTCH_MUSIC + "[0|1|2|酷狗音乐|网易云音乐|QQ音乐] (切换点歌引擎)" +
                    "\n", item);
            return;
        } else if (args.length == 2) {
            String argsCurrent = ParamParseUtil.getArgByArgArr(args, ParamParseUtil.sArgSecond);
            int i = ParseUtils.parseInt(argsCurrent, 0);
            if (i > 0) {
                count = i;
                music = args[ParamParseUtil.sArgFirst];
                if (!isManager) {
                    MsgReCallUtil.notifyNotManagerMsg(contentProvider, item);
                    return;
                }
            } else {

                if ("showcount".equals(argsCurrent)) {
                    if (!isManager) {
                        MsgReCallUtil.notifyNotManagerMsg(contentProvider, item);
                        return;
                    }
                    showCount = true;

                    music = args[ParamParseUtil.sArgFirst];
                } else if ("download".equals(argsCurrent) || "下载地址".equals(argsCurrent)) {

                    if (!isManager) {
                        MsgReCallUtil.notifyNotManagerMsg(contentProvider, item);
                        return;
                    }
                    music = args[ParamParseUtil.sArgFirst];
                    onlyDownload = true;
                } else if (Cns.ALL_PERSON.equals(argsCurrent) || Cns.ALL_PERSON_1.equals(argsCurrent)) {

                    if (!isManager) {
                        MsgReCallUtil.notifyNotManagerMsg(contentProvider, item);
                        return;
                    }
                    music = args[ParamParseUtil.sArgFirst];

                    if (atPair == null || atPair.second.second == null || atPair.second.second.isEmpty()) {
                        atPair = ConfigUtils.createAllAiteMsg(Cns.ALL_PERSON_AITE);
                        aiteAll = true;
                    }

                } else {

                    music = argStr;
                }
            }
        } else {
            music = argStr;
        }
        final boolean finalShowAll = showAll;
        final int finalCount = count;
        final boolean finalShowCount = showCount;
        final String finalMusic = music;
        final boolean finalOnlyDownload1 = onlyDownload;
        final int pageMusicCount = finalCount >= 0 ? finalCount : (finalShowAll ? 10 : 1);
        final Pair<Boolean, Pair<Boolean, List<GroupAtBean>>> finalAtPair = atPair;
        boolean finalAiteAll = aiteAll;
        new QssqTask<Object>(new QssqTask.ICallBack() {
            @Override
            public Object onRunBackgroundThread() {
                if (RobotContentProvider.musicType == MusicModuleI.WANGYIYUN) {

                    return do163Music(finalCount, pageMusicCount, finalShowAll, finalMusic, args, argStr, countArr, finalOnlyDownload1);
                } else if (RobotContentProvider.musicType == MusicModuleI.KUGOU) {

                    return doKuGouMusic(finalCount, pageMusicCount, finalShowAll, finalMusic, args, argStr, countArr, finalOnlyDownload1);
                } else if (RobotContentProvider.musicType == MusicModuleI.QQ) {

                    return doQQMusic(finalCount, pageMusicCount, finalShowAll, finalMusic, args, argStr, countArr, finalOnlyDownload1);
                } else {
                    return "未知的音乐类型配置";
                }


            }

            @Override
            public void onRunFinish(Object o) {
                if (o instanceof Throwable) {
                    MsgReCallUtil.notifyHasDoWhileReply(contentProvider, ((Exception) o).getMessage(), item);
                } else if (o instanceof String) {
                    MsgReCallUtil.notifyHasDoWhileReply(contentProvider, o + "", item);

                } else if (o instanceof List) {
                    List list = (List) o;
                    for (int i = 0; i < list.size(); i++) {
                        MusicCardInfo info = (MusicCardInfo) list.get(i);
                        MsgReCallUtil.notifMusicCardJump(contentProvider, item, info.toString());
                    }

                    if (MsgTyeUtils.isGroupMsg(item) && finalAtPair != null && finalAtPair.first) {

                      /*  if (atPair.second.first && atPair.second.second.size() <=1) {
                            //艾特了机器人
                            return;
                        }*/

                        if (finalAtPair.second.second.isEmpty()) {
                            return;

                        }
                        List second = finalAtPair.second.second;
                        String obj = "你" + (finalAtPair.second.second.size() > 1 || finalAiteAll ? "们" : "");
                        String s = BatchUtil.buildAtNickSource(item, finalAtPair.second.second);
                        String msg = s + " " + item.getNickname() + "为" + obj + "点了" + list.size() + "首《" + args[ParamParseUtil.sArgFirst] + "》" + obj + "喜欢吗?";
                        MsgReCallUtil.notifyAtMsgJumpB(contentProvider, msg, second, item);
                    } else if (finalShowCount) {
                        MsgReCallUtil.notifyJoinMsgNoJump(contentProvider, args[ParamParseUtil.sArgFirst] + "歌曲查询完成 查询共" + countArr[0] + "首", item);
                    }
                } else {
                    MsgReCallUtil.notifyHasDoWhileReply(contentProvider, "未知错误 " + o, item);

                }

            }
        }).execute();

    }

    private static Object doQQMusic(int finalCount, int pageMusicCount, boolean finalShowAll, String finalMusic, String[] args, String argStr, int[] countArr, boolean finalOnlyDownload1) {


        try {


            String qqMusic = "https://c.y.qq.com/soso/fcgi-bin/client_search_cp?ct=24&qqmusic_ver=1298" +
                    "&new_json=1&remoteplace=txt.yqq.center&searchid=49376627710948669&t=0&aggr=1&cr=1" +
                    "&catZhida=1&lossless=0&flag_qc=0&p=1&n=" + pageMusicCount + "&w=" + finalMusic + "&g_tk=5381&" +
                    "jsonpCallback=qssq&" +
                    "loginUin=0&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0";

//            qqMusic="https://c.y.qq.com/soso/fcgi-bin/client_search_cp?aggr=1&cr=1&flag_qc=0&p=1&n=20&w="+finalMusic;


            String json = OkHttpUtil.syncRequest(qqMusic, new INotify<OkHttpClient.Builder>() {

                @Override
                public void onNotify(OkHttpClient.Builder param) {

                    kuGOuPermissionFix(param);
                }
            });


            if (json != null) {

                //getOneSongInfoCallback(

                json = formatPjson("qssq", json);
                JSONObject jsonObjectRoot = JSON.parseObject(json);

                ArrayList<MusicCardInfo> musicCardInfos = new ArrayList<>();
                if (jsonObjectRoot.containsKey("data")) {


                    JSONArray jsonArray = jsonObjectRoot.getJSONObject("data").getJSONObject("song").getJSONArray("list");


                    if (jsonArray == null || jsonArray.isEmpty()) {
                        return String.format("抱歉,没找到歌曲%s 换一首歌曲吧!", argStr);

                    } else {
                        countArr[0] = jsonArray.size();

                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject firstMusicMusicItem = jsonArray.getJSONObject(i);
//                                        JSONObject firstMusicObj = jsonArray.getJSONObject(0);
                            MusicCardInfo info = new MusicCardInfo();
                            info.setUrl(Cns.MY_URL);
                            info.setActionData(BuildConfig.APPLICATION_ID);
                            info.setSharesource("小冰的朋友(QSSQ)");

                            info.setIcon(Cns.DEFAULT_ROBOT_ICON);
                            String songName = firstMusicMusicItem.getString("title") + firstMusicMusicItem.getString("subtitle");
                            JSONArray singersjJSONArray = firstMusicMusicItem.getJSONArray("singer");
                            String singerName = "情随事迁";
                            if (singersjJSONArray != null) {
                                JSONObject jsonObjectX = singersjJSONArray.getJSONObject(0);
                                singerName = jsonObjectX.getString("name");


                            }
                            //alert_sound

                            info.setTitlebrief("[点歌]" + songName + " " + singerName);
                            info.setMusictitle(info.getTitlebrief());
                            info.setAuthor(singerName);

                            info.setExtraStr("发布时间:" + firstMusicMusicItem.getString("time_public"));

                            String url = "";


                            //file: { media_mid: "000uhMwj387EBp"
                            String mid = firstMusicMusicItem.getJSONObject("file").getString("media_mid");
                            String queryDetialUrl = "https://c.y.qq.com/v8/fcg-bin/fcg_play_single_song.fcg?songmid=" + mid + "&tpl=" +
                                    "yqq_song_detail&format=jsonp&callback=qssq&g_tk=5381&jsonpCallback=" +
                                    "&loginUin=0&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0";


                            String detailJSON = OkHttpUtil.syncRequest(queryDetialUrl, new INotify<OkHttpClient.Builder>() {
                                @Override
                                public void onNotify(OkHttpClient.Builder param) {

                                    kuGOuPermissionFix(param);
                                }
                            });


                            String audioCoverUrl = Cns.DEFAULT_QQ_MUIC_URL;
                            if (detailJSON != null) {
                                detailJSON = formatPjson("qssq", detailJSON);
                                JSONObject jsonObjectDetial = JSONObject.parseObject(detailJSON);

                                JSONObject urlJson = jsonObjectDetial.getJSONObject("url");
                                if (urlJson != null) {

                                    findlink:
                                    for (int i1 = 0; i1 < urlJson.size(); i1++) {
                                        for (Map.Entry<String, Object> entry : urlJson.entrySet()) {
                                            String value = (String) entry.getValue();
                                            //url: {   213224236: "ws.stream.qqmusic.qq.com/C100000uhMwj387EBp.m4a?fromtag=38"
                                            if (value.contains("/")) {
                                                url = value;
                                                if (!url.startsWith("http")) {
                                                    url = "http://" + url;
                                                }
                                                break findlink;
                                            }
                                        }
                                    }
                                } else {
                                    if (BuildConfig.DEBUG) {

                                        Log.e(TAG, "find url error:" + detailJSON);
                                    }
                                }

                                JSONArray data = jsonObjectDetial.getJSONArray("data");
                                if (data != null && data.size() > 0) {
                                    long picPid = data.getJSONObject(0).getJSONObject("album").getLongValue("id");

                                    long temp = picPid % 100;
                                    audioCoverUrl = "http://imgcache.qq.com/music/photo/album_300/" + temp + "/300_albumpic_" + picPid + "_0.jpg";

                                }

                            }

                            if (finalOnlyDownload1) {
                                String s = songName + " " + singerName + " " + "歌曲下载地址:" + url;
                                return s;
                            }
                            info.setAudioFile(url);
                            info.setDuration(0);


//                            data数组 下面  第一个   的 album 里面的  id


                            info.setAudioCover(audioCoverUrl);
                            musicCardInfos.add(info);
                             /*   if (!finalShowAll) {

                                    return musicCardInfos;
                                }
*/

                        }

                        return musicCardInfos;
                    }
                } else {

                    return "QQ服务器已失效,或服务器错误,请联系情迁更新 qq:" + Cns.DEFAULT_QQ;
                }

            } else {
                return "服务器数据非法";
            }


        } catch (IOException e) {
            e.printStackTrace();
            return "服务器错误" + e.getMessage();
        }


    }


    /**
     * 网易音乐搜索API
     * http://s.music.163.com/search/get/
     * 获取方式：GET
     * 参数：
     * src: lofter //可为空
     * type: 1
     * filterDj: true|false //可为空
     * s: //关键词
     * limit: 10 //限制返回结果数
     * offset: 0 //偏移
     * callback: //为空时返回json，反之返回jsonp callback
     *
     * @param s
     * @param
     * @return 注意废数字才用‘’符号，要不不能用，否则出错！！
     */
    public static void SearchMusic(String s, int limit, int type, int offset) {
        String url = WangYiMusicMoudle.CLOUD_MUSIC_API_SEARCH + "type=" + type + "&s='" + s + "'&limit=" + limit + "&offset=" + offset;
    }

    private static String zipInputStreamParse(InputStream is) throws IOException {
        GZIPInputStream gzip = new GZIPInputStream(is);
        BufferedReader in = new BufferedReader(new InputStreamReader(gzip, "utf-8"));
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null)
            buffer.append(line + "\n");
        is.close();
        return buffer.toString();
    }


    private static String zipInputStreamParse(String out) throws IOException {
        ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(out.getBytes("iso-8859-1"));
        return zipInputStreamParse(tInputStringStream);
    }

    static String callBack = "";

    /**
     * @param finalCount
     * @param pagesize           几首歌
     * @param finalShowAll
     * @param finalMusic
     * @param args
     * @param argStr
     * @param countArr
     * @param finalOnlyDownload1
     * @return
     */
    public static Object do163Music(int finalCount, int pagesize, boolean finalShowAll, String finalMusic, String[] args, String argStr, int[] countArr, boolean finalOnlyDownload1) {
        try {
//短网址
            //web  http://music.163.com/song/36471631/?userid=98282765

//            String urlQuery = WangYiMusicMoudle.CLOUD_MUSIC_API_SEARCH;
            String json = null;
            String urlQuery = WangYiMusicMoudle.CLOUD_MUSIC_API_SEARCH + "type=" + 1 + "&src=lofter&filterDj=false&s=" + finalMusic + "&limit=" + pagesize + "&offset=" + 0;
/*

            FormBody formBody = new FormBody
                    .Builder()
                    .add("type","1")//
                    .add("callback","")//
                    .add("src","lofter")//
                    .add("filterDj","true")//
                    .add("s",""+finalMusic)//
                    .add("limit",""+(pagesize * 10))//
                    .add("offset","0")//
                    .build();

            json = OkHttpUtil.syncPostRequest(urlQuery, formBody,new INotify<OkHttpClient.Builder>() {
                    @Override
                    public void onNotify(OkHttpClient.Builder param) {

                        fix163PermissionFix(param);
                    }
                });*/


            NetQuery netQuery = new NetQuery();
            HashMap<String, String> map = new HashMap<>();
            /*
                .addHeader("Host", "s.music.163.com")
                        .addHeader("Cache-Control", "max-age=0")

                        .addHeader("Connection", "close")
                        .addHeader("Accept-Encoding", "gzip, deflate, sdch")
                        .addHeader("Accept", "ext/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/
            ;
//                        .addHeader(" Proxy-Connection","keep-alive")
//                        .addHeader(" Proxy-Connection","keep-alive")
//                        .addHeader(" Upgrade-Insecure-Requests","1")
            map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36.35068264 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
            map.put("Cache-Control", "max-age=0");
            map.put("Host", "s.music.163.com");
            map.put("Connection", "close");
            map.put("Accept-Encoding", "gzip, deflate, sdch");
            map.put("Accept", "application/json; charset=utf-8");
//            map.put("Accept", "ext/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            json = netQuery.sendGet(urlQuery, "", false, "", map);

            if (json != null) {
                if (!StringUtils.isJSON(json)) {


//                    json = EntityUtils.toString(new GzipDecompressingEntity(json));


                    return "服务器数据存在严重错误";

                }
                JSONObject jsonObjectRoot = JSON.parseObject(json);

                int code = jsonObjectRoot.getIntValue("code");
                if (code != 200) {

                    return "服务器已失效 code=" + code;
                }
                String keyDataResult = "result";

                if (!jsonObjectRoot.containsKey(keyDataResult)) {
                    return "结果为空,请更换歌曲名!";
                }
                JSONObject jsonObject = jsonObjectRoot.getJSONObject(keyDataResult);


                ArrayList<MusicCardInfo> musicCardInfos = new ArrayList<>();
                String keyData = "songs";
                if (jsonObject.containsKey(keyData)) {


                    JSONArray jsonArray = null;
                    try {
                        jsonArray = JSON.parseArray(jsonObject.getString(keyData));

                    } catch (Exception e) {
                        return "您当前的点的歌曲" + args[ParamParseUtil.sArgFirst] + "太奇葩，服务器返回的数据老夫无法识别，请联系我家主人吧";
                    }
                    if (jsonArray == null || jsonArray.isEmpty()) {
                        return String.format("抱歉,没找到歌曲%s 换一首歌曲吧!", argStr);

                    } else {
                        countArr[0] = jsonArray.size();

                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject firstMusicMusicItem = jsonArray.getJSONObject(i);
//                                        JSONObject firstMusicObj = jsonArray.getJSONObject(0);
                            MusicCardInfo info = new MusicCardInfo();
                            info.setUrl(Cns.MY_URL);
                            info.setActionData(BuildConfig.APPLICATION_ID);
                            info.setSharesource("小冰的朋友(QSSQ)");

                            info.setIcon(Cns.DEFAULT_ROBOT_ICON);
                            String songName = firstMusicMusicItem.getString("name");
//                                    String fileName = firstMusicObj.getString("FileName");//和那个一样


                            String singerName = "情随事迁";
                            if (firstMusicMusicItem.containsKey("artists")) {
                                JSONArray artists = JSON.parseArray(firstMusicMusicItem.getString("artists"));
                                if (artists != null && artists.size() > 0) {
                                    singerName = artists.getJSONObject(0).getString("name");
                                }


                                info.setTitlebrief("[点歌]" + songName + " " + singerName);
                                info.setMusictitle(info.getTitlebrief());
                                info.setAuthor(singerName);
                             /*   String fileHash = firstMusicMusicItem.getString("SQFileHash");
                                if (TextUtils.isEmpty(fileHash) || fileHash.equals("00000000000000000000000000000000")) {
                                    fileHash = firstMusicMusicItem.getString("FileHash");
                                }
                                if (TextUtils.isEmpty(fileHash)) {
                                    fileHash = firstMusicMusicItem.getString("HQFileHash");
                                }*/


//                                String url = firstMusicMusicItem.getString("audio");

                                //其实无效的url  http://music.163.com/song/media/outer/url?id=34730326.mp3
//                           String     url="https://api.hibai.cn/music/Music/Music?id="+firstMusicMusicItem.getString("id")+"&type=url";//必须mp3等结尾
                                //https://api.hibai.cn/music/Music/Music?id=34730326&type=url&end=.mp3
//                                String url = "https://api.hibai.cn/music/Music/Music?id=" + firstMusicMusicItem.getString("id") + "&type=url&end=.mp3";//必须mp3等结尾
                                String url = "http://music.163.com/song/media/outer/url?id=" + firstMusicMusicItem.getString("id") + ".mp3";//必须mp3等结尾
                                if (finalOnlyDownload1) {
                                    String s = songName + " " + singerName + " " + "歌曲下载地址:" + url;
                                    return s;
                                }
                                info.setAudioFile(url);

//                                info.setDuration(4000 * 60);
                                String audioCoverUrl = Cns.DEFAULT_QQ_MUIC_URL;
                                if (firstMusicMusicItem.containsKey("album")) {
                                    JSONObject album = firstMusicMusicItem.getJSONObject("album");
                                    audioCoverUrl = album.getString("picUrl");
                                }

                                info.setAudioCover(audioCoverUrl);
                                musicCardInfos.add(info);
                             /*   if (!finalShowAll) {

                                    return musicCardInfos;
                                }
*/

                            }

                        }


                        return musicCardInfos;
                    }
                } else {

                    return "没有找到此歌曲";
                }

            } else {
                return "服务器数据非法";
            }


        } catch (IOException e) {
            e.printStackTrace();
            return "服务器错误" + e.getMessage();
        }
    }

    public static String formatPjson(String key, String json) {
        if (json.indexOf(key) == -1) {
            return json;

        }
        int lastIndex = json.lastIndexOf(")");

        int startIndex = json.indexOf("(");
        if (lastIndex == -1 || startIndex == -1) {
            return json;
        }
        return json.substring(startIndex + 1, lastIndex);

    }

    /**
     * @param finalCount
     * @param pagesize           几首歌
     * @param finalShowAll
     * @param finalMusic
     * @param args
     * @param argStr
     * @param countArr
     * @param finalOnlyDownload1
     * @return
     */

    public static Object doKuGouMusic(int finalCount, int pagesize, boolean finalShowAll, String finalMusic, String[] args, String argStr, int[] countArr, boolean finalOnlyDownload1) {
        try {


            String json = OkHttpUtil.syncRequest(String.format(Cns.KUGOU_URL, finalMusic, pagesize), new INotify<OkHttpClient.Builder>() {
                @Override
                public void onNotify(OkHttpClient.Builder param) {

                    kuGOuPermissionFix(param);
                }
            });
            if (json != null) {
                JSONObject jsonObject = JSON.parseObject(json);
                String keyData = "data";
                String text = "text";

                ArrayList<MusicCardInfo> musicCardInfos = new ArrayList<>();
                if (jsonObject.containsKey(keyData)) {


                    JSONObject dataJsonObject = JSON.parseObject(jsonObject.getString(keyData));
                    String keyList = "lists";
                    if (dataJsonObject.containsKey(keyList)) {
                        String dataArrayJSON = dataJsonObject.getString(keyList);


                        JSONArray jsonArray = null;
                        try {
                            jsonArray = JSON.parseArray(dataArrayJSON);

                        } catch (Exception e) {
                            return "您当前的点的歌曲" + args[ParamParseUtil.sArgFirst] + "太奇葩，服务器返回的数据老夫无法识别，请联系我家主人吧" + dataArrayJSON;
                        }
                        if (jsonArray == null || jsonArray.isEmpty()) {
                            return String.format("抱歉,没找到歌曲%s 换一首歌曲吧!", argStr);

                        } else {
                            countArr[0] = jsonArray.size();

                            for (int i = 0; i < jsonArray.size(); i++) {
                                JSONObject firstMusicMusicItem = jsonArray.getJSONObject(i);
//                                        JSONObject firstMusicObj = jsonArray.getJSONObject(0);
                                MusicCardInfo info = new MusicCardInfo();
                                info.setUrl(Cns.DEFAULT_MUSIC_URL);
                                info.setActionData(BuildConfig.APPLICATION_ID);
                                info.setSharesource("小冰的朋友(QSSQ)");

                                info.setIcon(Cns.DEFAULT_ROBOT_ICON);
                                String songName = firstMusicMusicItem.getString("SongName");
//                                    String fileName = firstMusicObj.getString("FileName");//和那个一样
                                String singerName = firstMusicMusicItem.getString("SingerName");
                                info.setTitlebrief("[点歌]" + songName + " " + singerName);
                                info.setMusictitle(info.getTitlebrief());
                                info.setAuthor(singerName);
                                String fileHash = null;


                                if (TextUtils.isEmpty(fileHash) || fileHash.equals("00000000000000000000000000000000")) {
                                    fileHash = firstMusicMusicItem.getString("FileHash");
                                }

                                if (TextUtils.isEmpty(fileHash)) {
                                    fileHash = firstMusicMusicItem.getString("HQFileHash");
                                }
                                if (TextUtils.isEmpty(fileHash)) {

                                    fileHash = firstMusicMusicItem.getString("SQFileHash");
                                }

//    http://trackercdn.kugou.com/i/v2/?cmd=25
                                String detailJSON = OkHttpUtil.syncRequest(String.format(Cns.KUGOU_FILE_URL, fileHash), new INotify<OkHttpClient.Builder>() {
                                    @Override
                                    public void onNotify(OkHttpClient.Builder param) {

                                        kuGOuPermissionFix(param);
                                    }
                                });


                                if (detailJSON == null) {
                                    return "无法查询消息文件信息 hash:" + fileHash;
                                }

                                JSONObject jsonObjectDetail = JSON.parseObject(detailJSON);
                                if(jsonObjectDetail.containsKey("error")&&!jsonObjectDetail.containsKey("url")){
                                    return "酷狗音乐资源已失效《"+songName+"》无法播放【" + jsonObjectDetail.getString("url") + "】如您直到能找到分析方法,请联系情迁更新 qq:" + Cns.DEFAULT_QQ;
                                }
                                String url = jsonObjectDetail.getString("url");
                                if (finalOnlyDownload1) {
                                    String s = songName + " " + singerName + " " + "歌曲下载地址:" + url;
                                    return s;
                                }
                                info.setAudioFile(url);
                                info.setDuration(jsonObjectDetail.getIntValue("timeLength"));
                                String keyAudioCover = "album_img";
                                String keyAudioCover1 = "imgUrl";
                                String audioCoverUrl =null;
                                if (jsonObjectDetail.containsKey(keyAudioCover)) {
                                    audioCoverUrl = jsonObjectDetail.getString(keyAudioCover);
                                }else{
                                    audioCoverUrl=Cns.DEFAULT_QQ_MUIC_URL;//"AlbumID" -> "17165001"
                                }
                                //topic_url
                                if (TextUtils.isEmpty(audioCoverUrl)) {
                                    if (jsonObjectDetail.containsKey(keyAudioCover1)) {
                                        audioCoverUrl = jsonObjectDetail.getString("imgUrl");
                                    }

                                }

                                if (audioCoverUrl != null) {
                                    audioCoverUrl = audioCoverUrl.replace("{size}", "400");
                                }
                                //http://m.kugou.com/rank/info/4672
                                info.setAudioCover(audioCoverUrl);
                                musicCardInfos.add(info);
                             /*   if (!finalShowAll) {

                                    return musicCardInfos;
                                }
*/

                            }

                            return musicCardInfos;
                        }
                    } else {
                        return "可能没找到你想要的歌曲!";
                    }
                } else {

                    String message = jsonObject.containsKey(text) ? jsonObject.getString(text) : "未知";
                    return "酷狗服务器已失效【" + message + "】请联系情迁更新 qq:" + Cns.DEFAULT_QQ;
                }

            } else {
                return "服务器数据非法";
            }


        } catch (IOException e) {
            e.printStackTrace();
            return "服务器错误" + e.getMessage();
        }
    }

    // ID搜索歌曲qq https://c.y.qq.com/v8/fcg-bin/fcg_play_single_song.fcg?songmid=213262738&tpl=yqq_song_detail&format=jsonp&callback=getOneSongInfoCallback&g_tk=5381&jsonpCallback=getOneSongInfoCallback&loginUin=0&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0


    //qq里面的真难找 ws.stream.qqmusic.qq.com/C100002wktkW4NRg1M.m4a?fromtag=38

    /*

    {"status":1,"error_code":0,"data":{"page":1,"tab":"全部","lists":[{"SongName":"说散就散","OwnerCount":111755,"MvType":2,"TopicRemark":"","SQFailProcess":4,"Source":"","Bitrate":128,"HQExtName":"mp3","SQFileSize":27219789,"ResFileSize":0,"AudioCdn":100,"MvTrac":3,"SQDuration":242,"ExtName":"mp3","Auxiliary":"《前任3：再见前任》电影片尾曲","SongLabel":"","Scid":35683944,"OriSongName":"说散就散","FailProcess":4,"SQBitrate":897,"HQBitrate":320,"Audioid":35683944,"HiFiQuality":2,"Grp":{},"OriOtherName":"","AlbumPrivilege":8,"TopicUrl":"","SuperFileHash":"","ASQPrivilege":10,"M4aSize":996106,"IsOriginal":0,"Privilege":8,"ResBitrate":0,"FileHash":"1543CB6EEBB01A574FB2957481445C9D","SQPayType":3,"HQPrice":200,"trans_param":{"roaming_astrict":0,"pay_block_tpl":1,"musicpack_advance":0,"display_rate":0,"display":0,"cid":30525938},"Type":"audio","FoldType":0,"SourceID":0,"A320Privilege":10,"FileName":"袁娅维 - 说散就散","AlbumID":"8025931","ID":"103880178","SuperFileSize":0,"QualityLevel":3,"SQFileHash":"BFFFF05F6BB9F13EBDBE06ED4E6D8B4E","AlbumName":"说散就散","HQPrivilege":10,"SuperBitrate":0,"SuperDuration":0,"MixSongID":"103880178","ResFileHash":"","PublishAge":255,"SuperExtName":"","HQFileHash":"19763630BB46158040692D7395BCDA3D","HQPkgPrice":1,"Duration":242,"FileSize":3882101,"OtherName":"","SQPkgPrice":1,"PkgPrice":1,"HQFileSize":9707219,"HQFailProcess":4,"OldCpy":0,"SQPrivilege":10,"SQPrice":200,"ResDuration":0,"SingerId":[6792],"Price":200,"HQPayType":3,"SingerName":"袁娅维","Publish":1,"MvHash":"0BB8D1C6BFE07C1F370AD3C2F1A09D1F","SQExtName":"flac","HQDuration":242,"PayType":3,"HasAlbum":1,"mvTotal":0,"Accompany":1}],"chinesecount":4,"searchfull":1,"correctiontype":0,"subjecttype":0,"aggregation":[{"key":"DJ","count":0},{"key":"现场","count":0},{"key":"广场舞","count":0},{"key":"伴奏","count":0},{"key":"铃声","count":0}],"allowerr":0,"correctionsubject":"","correctionforce":0,"total":463,"istagresult":0,"istag":0,"correctiontip":"","pagesize":1}}

     */
}
