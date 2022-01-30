package cn.qssq666.robot.xbean;

/**
 * Created by qssq on 2018/1/24 qssq666@foxmail.com
 */

public class KuGouHashInfo {


    public long getFileHead() {
        return fileHead;
    }

    public void setFileHead(long fileHead) {
        this.fileHead = fileHead;
    }

    public long getQ() {
        return q;
    }

    public void setQ(long q) {
        this.q = q;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getCtype() {
        return ctype;
    }

    public void setCtype(long ctype) {
        this.ctype = ctype;
    }

    public long getSingerId() {
        return singerId;
    }

    public void setSingerId(long singerId) {
        this.singerId = singerId;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getStype() {
        return stype;
    }

    public void setStype(long stype) {
        this.stype = stype;
    }

    public long getPrivilege() {
        return privilege;
    }

    public void setPrivilege(long privilege) {
        this.privilege = privilege;
    }

    public long getErrcode() {
        return errcode;
    }

    public void setErrcode(long errcode) {
        this.errcode = errcode;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getBitRate() {
        return bitRate;
    }

    public void setBitRate(long bitRate) {
        this.bitRate = bitRate;
    }

    public long getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(long timeLength) {
        this.timeLength = timeLength;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getChoricSinger() {
        return choricSinger;
    }

    public void setChoricSinger(String choricSinger) {
        this.choricSinger = choricSinger;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getTopic_remark() {
        return topic_remark;
    }

    public void setTopic_remark(String topic_remark) {
        this.topic_remark = topic_remark;
    }

    public String getSingerHead() {
        return singerHead;
    }

    public void setSingerHead(String singerHead) {
        this.singerHead = singerHead;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMvhash() {
        return mvhash;
    }

    public void setMvhash(String mvhash) {
        this.mvhash = mvhash;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getReq_hash() {
        return req_hash;
    }

    public void setReq_hash(String req_hash) {
        this.req_hash = req_hash;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getTopic_url() {
        return topic_url;
    }

    public void setTopic_url(String topic_url) {
        this.topic_url = topic_url;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAlbum_img() {
        return album_img;
    }

    public void setAlbum_img(String album_img) {
        this.album_img = album_img;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /*    extra;// {
                320filesize;// 7074062,
                        sqfilesize;// 21268006,
                        sqhash;// "EF1640A750D7944C7E0E6B22C491CB2A",
                        128hash;// "A44BE48369A43249BD9E37E6EAB5C3E4",
                        320hash;// "40F6FB0C6C837250C0C0823B81774AE4",
                        128filesize;// 2829805,
            },*/
    private long fileHead;//100,
    private long q;//0,
    private long fileSize;//2829805,
    private long ctype;//1009,
    private long singerId;//0,
    private long status;//1,
    private long stype;//11323,
    private long privilege;//8,
    private long errcode;//0,
    private long time;//1516802732,
    private long bitRate;//128,
    private long timeLength;//177,
    private String hash;//EncryptUtilN.a7(new int[]{4629,4905,4909,4825,4845,4837,4821,4889,4849,4841,4821,4901,4849,4857,4837,4837,4897,4849,4905,4821,4905,4845,4893,4829,4829,4897,4837,4857,4825,4897,4893,4829,4889}),
    private String choricSinger;//"",
    private String error;//"",
    private String topic_remark;//"",
    private String singerHead;//"",
    private String url;//EncryptUtilN.a7(new int[]{5067,5483,5531,5531,5515,5303,5255,5255,5255,5255,5475,5527,5251,5511,5515,5471,5507,5251,5495,5535,5479,5511,5535,5251,5463,5511,5503,5255,5283,5475,5467,5471,5287,5279,5295,5459,5455,5459,5279,5295,5275,5471,5275,5463,5475,5267,5455,5295,5467,5283,5279,5467,5275,5259,5471,5455,5275,5455,5279,5283,5255,5279,5455,5283,5291,5295,5267,5455,5463,5255,5351,5263,5263,5279,5255,5375,5259,5267,5255,5259,5327,5255,5263,5263,5255,5343,5275,5463,5331,5327,5347,5507,5515,5367,5335,5247,5327,5455,5379,5415,5507,5327,5335,5527,5531,5287,5395,5463,5535,5483,5275,5259,5283,5267,5267,5251,5503,5515,5271}),
    private String fileName;//EncryptUtilN.a7(new int[]{5662,5962,6066,6094,6094,6146,5790,6010,6050,6094,6122,6078,5790,5842,5790,5966,6050,6106}),
    private String mvhash;//"",
    private String intro;//"",
    private String req_hash;//EncryptUtilN.a7(new int[]{3000,3260,3208,3208,3264,3276,3208,3224,3204,3216,3228,3260,3208,3204,3200,3208,3228,3264,3272,3228,3276,3204,3220,3276,3216,3276,3260,3264,3212,3268,3204,3276,3208}),
    private String singerName;//"",
    private String topic_url;//"",
    private String songName;//EncryptUtilN.a7(new int[]{6932,7236,7320,7376}),
    private String album_img;//EncryptUtilN.a7(new int[]{4834,5250,5298,5298,5282,5070,5022,5022,5022,5022,5254,5270,5246,5238,5018,5262,5302,5246,5278,5302,5018,5230,5278,5270,5022,5294,5298,5234,5270,5302,5294,5254,5230,5022,5326,5294,5254,5322,5238,5334,5022,5034,5026,5030,5054,5030,5026,5034,5026,5022,5034,5026,5030,5054,5030,5026,5034,5026,5026,5050,5042,5026,5034,5030,5062,5050,5034,5038,5034,5058,5018,5258,5282,5246}),
    private String extName;//EncryptUtilN.a7(new int[]{2654,3090,3102,2858}),
    private String imgUrl;//EncryptUtilN.a7(new int[]{149,565,613,613,597,385,337,337,337,337,585,333,577,617,561,593,617,333,545,593,585,337,609,613,537,613,569,545,337,569,585,537,561,553,609,337,609,565,537,605,553,349,341,345,357,337,549,553,557,537,617,581,613,529,609,569,589,561,553,605,333,573,597,561}),

}
