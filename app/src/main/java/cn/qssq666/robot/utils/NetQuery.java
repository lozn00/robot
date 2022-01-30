package cn.qssq666.robot.utils;
import android.os.AsyncTask;
import androidx.core.util.Pair;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class NetQuery {
    /**
     * String Base url
     */
    private static final String baseurl = "http://localhost/net.php";

    private static final String Dataencoding = "UTF-8";
    private static final String TAG = "NetQuery";
    /**
     * int timeout 超时时间 默认 6000ms
     */
    public int timeout = 1800000;
    /**
     * boolean doinput 是否向服务器端发送数据 默认为 true
     */
    public boolean doinput = true;
    /**
     * boolean dooutput 是否接收服务器端发送的数据 默认为 true
     */
    public boolean dooutput = true;

    /**
     * The HttpURLConnection to connect the website.
     */

    /**
     * sendCoding String 发送请求的编码方式。
     */
    public String sendCoding = "UTF-8";

    /**
     * Parsecode_GBK String 本地解析时的编码方式。
     */
    public static final String Parsecode_GBK = "GBK";
    public static final String Parsecode_UTF_8 = "UTF-8";
    public static String default_encode = Parsecode_UTF_8;


    /**
     * 返回的cookie
     */
    String backCookie;


    String backProtocol;

    public String getBackProtocol() {
        return backProtocol;
    }

    public void setBackProtocol(String backProtocol) {
        this.backProtocol = backProtocol;
    }

    public String getBackCookie() {
        return backCookie;
    }

    public void setBackCookie(String backCookie) {
        this.backCookie = backCookie;
    }

    public static String getDefault_encode() {
        return default_encode;
    }

    public static void setDefault_encode(String default_encode) {
        NetQuery.default_encode = default_encode;
    }

    /**
     * @throws Exception
     */
    public String sendPost(String str, String postData, String submitCookie, boolean redirect) throws Exception {
        return sendPost(str, null, postData, submitCookie, redirect);
    }

    public String sendPost(String str, String postData) throws Exception {
        return sendPost(str, null, postData, null, false);
    }

    public String sendPost(String str, HashMap<String, String> requestHeaderMap, String postData) throws Exception {
        return sendPost(str, requestHeaderMap, postData, null, false);
    }

    /*
    HurlStack
        Iterator responseCode = map.keySet().iterator();

        while(responseCode.hasNext()) {
            String protocolVersion = (String)responseCode.next();
            connection.addRequestProperty(protocolVersion, (String)map.get(protocolVersion));
        }
     */
    /**
     * 覆盖java默认的证书验证
     */
    private static final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

        }

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }


    }};

    /**
     * 设置不验证主机
     */
    private static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * http://blog.csdn.net/u012527802/article/details/70172357
     * 信任所有
     *
     * @param connection
     * @return
     */
    private static SSLSocketFactory trustAllHosts(HttpsURLConnection connection) {
        SSLSocketFactory oldFactory = connection.getSSLSocketFactory();
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory newFactory = sc.getSocketFactory();
            connection.setSSLSocketFactory(newFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oldFactory;
    }

    public String sendPost(String urls, HashMap<String, String> requestHeaderMap, String postData, String submitCookie, boolean redirect) throws Exception {
        URL url = new URL(urls);
        HttpURLConnection.setFollowRedirects(redirect);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");

        boolean useHttps = urls.startsWith("https");
        if (useHttps) {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
            SSLSocketFactory oldSocketFactory = trustAllHosts(httpsURLConnection);
            HostnameVerifier oldHostnameVerifier = httpsURLConnection.getHostnameVerifier();
            httpsURLConnection.setHostnameVerifier(DO_NOT_VERIFY);
        }

        if (submitCookie != null) {
            httpURLConnection.setRequestProperty("Cookie", submitCookie);
        }
//        httpURLConnection.connect();
        // Post 请求不能使用缓存
        httpURLConnection.setUseCaches(false);//同样 .IllegalStateException: Already connected
        httpURLConnection.setDoInput(true);//如果已经连接了conntext就会出差错。
        boolean isUnzip = initRequestHeaderPropertyIsUnzip(requestHeaderMap, httpURLConnection);
        httpURLConnection.setDoOutput(true);
        if (postData != null) {
            OutputStream outputStream = httpURLConnection.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, this.sendCoding);
            outputStreamWriter.write(postData);
            outputStreamWriter.flush();
            outputStreamWriter.close();
            outputStream.close();
        }
        initBackProtectHeads(httpURLConnection);

        String backData = getStringData(httpURLConnection, isUnzip);
        httpURLConnection.disconnect();
        return backData;
    }

    public byte[] sendGetBytes(String str) throws IOException {
        System.out.println(TAG + "url:" + str);
        return sendGetBytes(str, null, false);
    }

    public String sendGet(String str) throws IOException {
        return sendGet(str, null, false);
    }

    /**
     * 发送异步请求 非耗时操作,因此可以在主线程调用
     *
     * @param url
     * @param submitCookie
     * @param canRedirect
     * @param redirectUrl
     * @param callBack
     */
    public void sendAsyncGet(String url, String submitCookie, boolean canRedirect, String redirectUrl, ICallBack<Pair<Boolean, String>> callBack) {
        System.out.println(TAG + "异步提交:" + url);
        new AsyncNetQueryTask(submitCookie, canRedirect, redirectUrl, callBack).execute(url);
    }

    class AsyncNetQueryTask extends AsyncTask<String, Integer, Object> {
        String submitCookie = null;
        private final ICallBack<Pair<Boolean, String>> pairICallBack;
        boolean canRedirect = true;
        String redirectUrl;

        public AsyncNetQueryTask(String submitCookie, boolean canRedirect, String redirectUrl, ICallBack<Pair<Boolean, String>> callBack) {
            pairICallBack = callBack;
            this.submitCookie = submitCookie;
            this.canRedirect = canRedirect;
            this.redirectUrl = redirectUrl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            if (o == null) {
                pairICallBack.onCallBack(Pair.create(false, "未知错误"));
            } else if (o instanceof Exception) {
                pairICallBack.onCallBack(Pair.create(false, o.toString()));
            } else {
                pairICallBack.onCallBack(Pair.create(true, o.toString()));
            }
        }

        @Override
        protected Object doInBackground(String... params) {
            try {
                return sendGet(params[0], submitCookie, canRedirect, redirectUrl, null);

            } catch (Exception e) {
                e.printStackTrace();
                return e;
            }
        }
    }

    public interface ICallBack<T> {
        void onCallBack(T t);
    }


    public String sendGet(String url, String submitCookie, boolean redirect) throws IOException {
        return sendGet(url, submitCookie, redirect, null, null);
    }

    public String sendGet(String url, HashMap<String, String> requestHeaderMap) throws IOException {
        return sendGet(url, null, false, null, requestHeaderMap);
    }

    /**
     * @param urlStr
     * @param submitCookie
     * @param redirect
     * @param redirectUrl      来源地址
     * @param requestHeaderMap 请求头
     * @return
     * @throws Exception
     */
    public String sendGet(String urlStr, String submitCookie, boolean redirect, String redirectUrl, HashMap<String, String> requestHeaderMap) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection.setFollowRedirects(redirect);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//        httpUrlConnection.setDoOutput(true);以后就可以使用conn.getOutputStream().write()
//        httpUrlConnection.setDoInput(true);以后就可以使用conn.getInputStream().read();

        httpURLConnection.setRequestMethod("GET");
        if (redirectUrl != null) {
            httpURLConnection.setRequestProperty("Referer", "" + redirectUrl);
        }

        boolean isUnzip = initRequestHeaderPropertyIsUnzip(requestHeaderMap, httpURLConnection);
        if (submitCookie != null) {
            httpURLConnection.setRequestProperty("Cookie", submitCookie);
        }
        httpURLConnection.setUseCaches(false);
//        httpURLConnection.setDoOutput(true);//可能跑出文件没有发现异常
        if ("https".equals(url.getProtocol())) {
            ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault());
        }
        initBackProtectHeads(httpURLConnection);

        String backData = getStringData(httpURLConnection, isUnzip);
        httpURLConnection.disconnect();
        return backData;
    }

    /**
     * 不包含R eferer
     *
     * @param extraProperty
     * @param httpURLConnection
     */
    private static boolean initRequestHeaderPropertyIsUnzip(HashMap<String, String> extraProperty, HttpURLConnection httpURLConnection) {
        httpURLConnection.setRequestProperty("Connection", "keep-alive");
        httpURLConnection.setRequestProperty("Cache-Control", "no-cache");
        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");

        if (extraProperty == null) {
            extraProperty = new HashMap<>();
        }

        Set<Map.Entry<String, String>> entries = extraProperty.entrySet();
        if (extraProperty.get("Content-Type") == null) {
            //  httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");

//            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Content-Type", " application/x-www-form-urlencoded; charset=" + default_encode);

        }

        if (extraProperty.get("User-Agent") == null) {
            httpURLConnection.setRequestProperty("User-Agent", defaultUserAgent);
        } else {
            System.out.println(TAG + "自定义User-Agent");
        }
        if (extraProperty.get("Content-Language") == null) {
            httpURLConnection.setRequestProperty("Content-Language", defaultAcceptLanguage);
        }
        boolean needUnzip = false;
        for (Map.Entry<String, String> entry : entries) {

            if (entry.getKey().equals("Accept-Encoding")) {
                if (entry.getValue().contains("gzip")) {
                    needUnzip = true;
                }
            }
            httpURLConnection.setRequestProperty("" + entry.getKey(), "" + entry.getValue());
        }
        return needUnzip;
    }
    /*
    plication/xhtml+xml ：XHTML格式
   application/xml     ： XML数据格式
   application/atom+xml  ：Atom XML聚合格式
   application/json    ： JSON数据格式
   application/pdf       ：pdf格式
   application/msword  ： Word文档格式
   application/octet-stream ： 二进制流数据（如常见的文件下载）
   application/x-www-form-urlencoded ： <form encType=””>中
     */

    public static String defaultUserAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0";
    public static String defaultAcceptLanguage = "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3";

    public byte[] sendGetBytes(String str, String submitCookie, boolean redirect, HashMap<String, String> extraProtect) throws IOException {
        URL url = new URL(str);
        HttpURLConnection.setFollowRedirects(redirect);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        boolean isUnzip = initRequestHeaderPropertyIsUnzip(extraProtect, httpURLConnection);
        if (submitCookie != null) {
            httpURLConnection.setRequestProperty("Cookie", submitCookie);
        }
//        putRequest.addHeader("Content-Type", request.getBodyContentType());
//        httpURLConnection.setDoOutput(true);////        httpURLConnection.setDoOutput(true);//可能跑出文件没有发现异常
        initBackProtectHeads(httpURLConnection);
        byte[] backData = getByteData(httpURLConnection, isUnzip);
        httpURLConnection.disconnect();
        return backData;
    }

    public byte[] sendGetBytes(String str, String submitCookie, boolean redirect) throws IOException {
        return sendGetBytes(str, submitCookie, redirect, null);
    }

    /**
     * 读取数据
     *
     * @return String 读取的内容。
     */
    public static String getStringData(HttpURLConnection httpURLConnection, boolean isUnzip) throws IOException {
        //getthefiled2(code);
        InputStream is = httpURLConnection.getInputStream();// 获取输入流


        int responseCode = httpURLConnection.getResponseCode();
//        httpURLConnection.
        if (responseCode == HttpURLConnection.HTTP_OK) {

            StringBuffer sb = new StringBuffer();
            if (isUnzip) {

                byte[] uncompress = uncompress(is);
                is = new ByteArrayInputStream(uncompress);
            } else {


            }
            InputStreamReader isr = new InputStreamReader(is,
                    default_encode);// 包装流并且指定编码方式。


            BufferedReader br = new BufferedReader(isr);
            //getthefiled(code);
            //getthefiled2(code);
            String line;
            do {
                line = br.readLine();// 读取内容
                if (line != null && !line.equals("")) {
                    sb.append(line);
                }
            } while (line != null);
            // 关闭流
            br.close();
            isr.close();
            is.close();
            //System.out.println(sb.toString());
            return sb.toString();
        } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            throw new IOException("HTTP_UNAUTHORIZED");
        } else {
            throw new IOException("状态码不正确,code=" + responseCode);
        }
    }


    public static byte[] uncompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        return uncompress(in);

    }

    public static byte[] uncompress(InputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            Log.w(TAG, "gzip uncompress error.", e);
        }

        return out.toByteArray();
    }

    public static byte[] getByteData(HttpURLConnection httpURLConnection, boolean isUnzip) throws IOException {
        int code = httpURLConnection.getResponseCode();
        //getthefiled2(code);
        byte[] bytes = null;
        if (code == HttpURLConnection.HTTP_OK) {
            InputStream is = httpURLConnection.getInputStream();// 获取输入流

            if (isUnzip) {
                bytes = uncompress(is);
            } else {

                bytes = input2byte(is);
            }
            //getthefiled(code);
            //getthefiled2(code);
            // 关闭流
            is.close();
            return bytes;
            //System.out.println(sb.toString());
        } else {
            return null;
        }
    }

    public static final byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    public void initBackProtectHeads(HttpURLConnection httpURLConnection) {
        StringBuffer stringBuffer = new StringBuffer();
        Map<String, List<String>> maps = httpURLConnection.getHeaderFields();
        for (Map.Entry<String, List<String>> stringListEntry : maps.entrySet()) {
            List<String> valueList = stringListEntry.getValue();
            String key = stringListEntry.getKey();
            if ("Set-Cookie".equalsIgnoreCase(key)) {
                setBackCookie(listToString(valueList));
            } else {
                if (key != null) {
                    //HTTP/1.1 200 OK; j键值为空
                    stringBuffer.append(key + ":" + listToString(valueList));
                }
            }
        }
        setBackProtocol(stringBuffer.toString());
    }

    public String listToString(List<String> value) {
        Iterator<String> it = value.iterator();
        StringBuffer values = new StringBuffer();
        while (it.hasNext()) {
            values.append(it.next() + ";");
        }
        return values.toString();
    }

    public static void main(String str[]) {
        NetQuery netQuery = new NetQuery();
        String result = null;
        try {
            result = netQuery.sendPost("", "post=3罗正", "info=你的负担复旦dfdfffdfddf", false);
        } catch (Exception e) {
        }
        System.out.println("" + str);
        System.out.println("back cookie:" + netQuery.getBackCookie());
        System.out.println("back productes:" + netQuery.getBackProtocol());

    }
}
