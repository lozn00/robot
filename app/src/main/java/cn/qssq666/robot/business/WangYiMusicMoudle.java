package cn.qssq666.robot.business;
import cn.qssq666.CoreLibrary0;
/**
 * Created by qssq on 2018/3/17 qssq666@foxmail.com
 */

public class WangYiMusicMoudle {
/*
    *//**
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
     * @param s
     * @param context
     * @return
     * 注意废数字才用‘’符号，要不不能用，否则出错！！
     *//*
    public static void SearchMusic(Context context, String s, int limit, int type, int offset){
        String url = UrlConstants.CLOUD_MUSIC_API_SEARCH + EncryptUtilN.a7(new int[]{3954,4418,4438,4402,4358,4198})+type+EncryptUtilN.a7(new int[]{895,1047,1355,1139,1051}) + s + EncryptUtilN.a7(new int[]{4009,4165,4161,4441,4429,4445,4429,4473,4253})+limit+EncryptUtilN.a7(new int[]{4988,5140,5432,5396,5396,5448,5392,5452,5232})+offset;
        RequestQueue requestQueue = InternetUtil.getmRequestqueue(context);
        StringRequest straingRequest = new StringRequest(url,new Response.Listener<String>(){
            @Override
            public void onResponse(String s){
                try {
                    JSONObject json = new JSONObject(s);
                    Log.i(EncryptUtilN.a7(new int[]{1624,2068,2064,1952,2028,2084,2072,2068,2064,2084,2028,1856,1752}),json.toString());
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError){
                Log.i(EncryptUtilN.a7(new int[]{2365,2809,2805,2693,2769,2825,2813,2809,2805,2825,2769,2597,2493}),volleyError.toString());
            }
        });
        requestQueue.add(straingRequest);
    }*/
/*
    *//**
     * 网易云音乐歌曲信息API
     * @param context
     * @param id 歌曲id
     * @param ids 用[]包裹起来的歌曲id 写法%5B %5D
     * @return
     *//*
    public static void Cloud_Music_MusicInfoAPI(Context context,String id,String ids)
    {
        String url = UrlConstants.CLOUD_MUSIC_API_MUSICINGO + EncryptUtilN.a7(new int[]{3076,3496,3476,3320})+id+EncryptUtilN.a7(new int[]{1302,1454,1722,1702,1762,1546,1450,1514,1566})+ids+EncryptUtilN.a7(new int[]{2067,2215,2279,2339});
        RequestQueue requestQueue = InternetUtil.getmRequestqueue(context);
        StringRequest straingRequest = new StringRequest(url,new Response.Listener<String>(){
            @Override
            public void onResponse(String s){
                try {
                    JSONObject json = new JSONObject(s);
                    Log.i(EncryptUtilN.a7(new int[]{1658,2102,2098,1986,2062,2118,2106,2102,2098,2118,2062,1890,1786}),json.toString());
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError){
                Log.i(EncryptUtilN.a7(new int[]{851,1295,1291,1179,1255,1311,1299,1295,1291,1311,1255,1083,979}),volleyError.toString());
            }
        });
        requestQueue.add(straingRequest);
    }

    *//**
     * 获取歌曲歌词的API
     *URL：

     GET http://music.163.com/api/song/lyric

     必要参数：

     id：歌曲ID

     lv：值为-1，我猜测应该是判断是否搜索lyric格式

     kv：值为-1，这个值貌似并不影响结果，意义不明

     tv：值为-1，是否搜索tlyric格式
     * @param context
     * @param os
     * @param id
     *//*
    public static void Cloud_Muisc_getLrcAPI(Context context,String os,String id)
    {
        String url = UrlConstants.CLOUD_MUSIC_API_MUSICLRC + EncryptUtilN.a7(new int[]{4408,4852,4868,4652})+os+EncryptUtilN.a7(new int[]{3794,3946,4214,4194,4038})+id+EncryptUtilN.a7(new int[]{4025,4177,4457,4497,4269,4205,4221,4177,4453,4497,4269,4205,4221,4177,4489,4497,4269,4205,4221});
        RequestQueue requestQueue = InternetUtil.getmRequestqueue(context);
        StringRequest straingRequest = new StringRequest(url,new Response.Listener<String>(){
            @Override
            public void onResponse(String s){
                try {
                    JSONObject json = new JSONObject(s);
                    Log.i(EncryptUtilN.a7(new int[]{790,1234,1230,1118,1194,1250,1238,1234,1230,1250,1194,1022,918}),json.toString());
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError){
                Log.i(EncryptUtilN.a7(new int[]{5072,5516,5512,5400,5476,5532,5520,5516,5512,5532,5476,5304,5200}),volleyError.toString());
            }
        });
        requestQueue.add(straingRequest);
    }

    *//**
     * 获取歌单的API
     * @param context
     * @param id 歌单ID
     *//*
    public static void Cloud_Muisc_MusicListSearch(Context context,String id)
    {
        String url = UrlConstants.CLOUD_MUSIC_API_MUSICLIST + EncryptUtilN.a7(new int[]{2343,2763,2743,2587})+id+EncryptUtilN.a7(new int[]{1905,2057,2373,2353,2305,2293,2369,2309,2241,2325,2341,2309,2149,2085,2101});
        RequestQueue requestQueue = InternetUtil.getmRequestqueue(context);
        StringRequest straingRequest = new StringRequest(url,new Response.Listener<String>(){
            @Override
            public void onResponse(String s){
                try {
                    JSONObject json = new JSONObject(s);
                    Log.i(EncryptUtilN.a7(new int[]{6223,6667,6663,6551,6627,6683,6671,6667,6663,6683,6627,6455,6351}),json.toString());
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError){
                Log.i(EncryptUtilN.a7(new int[]{4719,5163,5159,5047,5123,5179,5167,5163,5159,5179,5123,4951,4847}),volleyError.toString());
            }
        });
        requestQueue.add(straingRequest);
    }


    public static JSONObject json = null;
    public static JSONObject getInfoFromUrl_Volley(String url,Context context)
    {
        json = null;
        RequestQueue requestQueue = InternetUtil.getmRequestqueue(context);
        StringRequest straingRequest = new StringRequest(url,new Response.Listener<String>(){
            @Override
            public void onResponse(String s){
                try {
                    json = new JSONObject(s);
                    Log.i(EncryptUtilN.a7(new int[]{656,1100,1096,984,1060,1116,1104,1100,1096,1116,1060,888,784}),json.toString());
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError){
                Log.i(EncryptUtilN.a7(new int[]{5062,5506,5502,5390,5466,5522,5510,5506,5502,5522,5466,5294,5190}),volleyError.toString());
            }
        });
        requestQueue.add(straingRequest);
        return json;
    }
    */

        /**
         * 云音乐搜索API网址
         */
        public static final String CLOUD_MUSIC_API_SEARCH = "https://s.music.163.com/search/get/?";
        /**
         * 歌曲信息API网址
         */
        public static final String CLOUD_MUSIC_API_MUSICINGO = "http://music.163.com/api/song/detail/?";
        /**
         * 获取歌曲的歌词
         */
        public static final String CLOUD_MUSIC_API_MUSICLRC = "http://music.163.com/api/song/lyric?";
        /**
         * 获取歌单
         */
        public static final String CLOUD_MUSIC_API_MUSICLIST = "http://music.163.com/api/playlist/detail?";

//https://www.dongyonghui.com/default/20180128-%E7%BD%91%E6%98%93%E4%BA%91%E3%80%81%E9%85%B7%E7%8B%97%E3%80%81QQ%E9%9F%B3%E4%B9%90%E6%AD%8C%E5%8D%95%E6%8E%A5%E5%8F%A3API.html
        /*
        联通音乐接口：
//www.10155.com/player/playContentId.do?songIds={$SongId}
//www.10155.com/player/playSongInfos.do?contentIds={$contentIds}_{$SongId}_not_not_not_not_not

酷狗51Sing接口1：//mobileapi.5sing.kugou.com/song/transcoding?songid=$SongId}&songtype={$SongType}
酷狗51Sing接口2：//5sing.kugou.com/m/detail/{$SongType}-{$SongId}-1.html

酷我音乐接口1：//antiserver.kuwo.cn/anti.s?format={$SongType}%7Cmp3&rid=MUSIC_{$SongId}&response=url&type=convert_url
酷我音乐接口2：//player.kuwo.cn/webmusic/st/getNewMuiseByRid?rid=MUSIC_{$SongId}

网易云音乐接口：//music.163.com/api/song/detail/?id={$SongId}&ids=%5B{$SongId}%5D&csrf_token=

百度音乐接口1：
①：//music.baidu.com/data/music/fmlink?songIds={$SongId}&type={$SongType}
②：//music.baidu.com/data/music/fmlink?songIds={$SongId}&type={$SongType}&rate={$Rate}

百度音乐接口2：
①：//ting.baidu.com/data/music/links?songIds={$SongId}&type={$SongType}
②：//ting.baidu.com/data/music/links?songIds={$SongId}&type={$SongType}&rate={$Rate}

咪咕音乐接口：//music.migu.cn/webfront/player/findsong.do?itemid={$SongId}&type=song

搜狗音乐：//mp3.sogou.com/tiny/song?query=getlyric&json=1&tid={$Tid}

Echo回声音乐：//echosystem.kibey.com/sound/info?sound_id={$SongId}

QQ音乐接口：
//s.plcloud.music.qq.com/fcgi-bin/fcg_yqq_song_detail_info.fcg?songmid={$SongMid}
//tsmusic24.tc.QQ.com/{$SongId}.mp3
//stream.QQmusic.tc.qq.com/{$SongId}.mp3
//tsmusic128.tc.qq.com/{$SongId+30000000}.mp3 （请计算出结果）
//tsmusic128.tc.qq.com/{$SongId+40000000}.ogg （请计算出结果）
//tsmusic24.tc.qq.com/{$SongId}.m4a
//thirdparty.gtimg.com/{$SongId}.m4a?fromtag=38
//thirdparty.gtimg.com/C100{$SongId}.m4a?fromtag=38
         */

}
