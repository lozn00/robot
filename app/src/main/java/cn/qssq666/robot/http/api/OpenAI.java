package cn.qssq666.robot.http.api;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
/*@FormUrlEncoded
@POST ("url/myurl")
Call<JsonArray> sendParameters (@Header("x-imei") String imei, @Header("x-id-cliente") String idCliente,
@Field("param1") JsonObject param1, @Field("param2") JsonArray param2, @Field("param3") JsonArray param3,
@Field("param4") JsonArray param4,@Field("param5") JsonArray param5);*/

public interface OpenAI {
    //http://www.baidu.com/aaa?key=123&qq=aaa  https://chat.openai.com/backend-api/conversation
   /* {
        "action": "next",
            "messages": [
        {
            "id": "d6352238-637f-48cb-9e71-f6dcf343c2f4",
                "role": "user",
                "content": {
            "content_type": "text",
                    "parts": [
            "can you speak english"
                ]
        }
        }
    ],
        "model": "text-davinci-002-render",
            "parent_message_id": "d6252238-637f-48cb-9e71-f6dcf343c5f4"
    }
    */

    /**  Content-Type  application/json Authorization
     * Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36
     * @param contentType
     * @param userAgent
     * @param authorization [{"key":"Authorization","value":"Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik1UaEVOVUpHTkVNMVFURTRNMEZCTWpkQ05UZzVNRFUxUlRVd1FVSkRNRU13UmtGRVFrRXpSZyJ9.eyJodHRwczovL2FwaS5vcGVuYWkuY29tL3Byb2ZpbGUiOnsiZW1haWwiOiJxc3NxNTIxQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJnZW9pcF9jb3VudHJ5IjoiVVMifSwiaHR0cHM6Ly9hcGkub3BlbmFpLmNvbS9hdXRoIjp7InVzZXJfaWQiOiJ1c2VyLWxkM05Rb2FlZU5nZHJGZ0pMQ3d2T2ljNCJ9LCJpc3MiOiJodHRwczovL2F1dGgwLm9wZW5haS5jb20vIiwic3ViIjoiZ29vZ2xlLW9hdXRoMnwxMDMwMTcwNjkxMDE4NDE3NTAzMDgiLCJhdWQiOlsiaHR0cHM6Ly9hcGkub3BlbmFpLmNvbS92MSIsImh0dHBzOi8vb3BlbmFpLmF1dGgwLmNvbS91c2VyaW5mbyJdLCJpYXQiOjE2NzA2ODEwMzIsImV4cCI6MTY3MDcyNDIzMiwiYXpwIjoiVGRKSWNiZTE2V29USHROOTVueXl3aDVFNHlPbzZJdEciLCJzY29wZSI6Im9wZW5pZCBlbWFpbCBwcm9maWxlIG1vZGVsLnJlYWQgbW9kZWwucmVxdWVzdCBvcmdhbml6YXRpb24ucmVhZCBvZmZsaW5lX2FjY2VzcyJ9.NMvaBNfdnaE_IXl3mJwlNoTldpk0Y93nUKCiJkmzpL3ti-RQx3GnXQxKkaNqD4fEPQq5UVzVFvXd8lLNwSavETXElW_yQwhtu0D446u3XRgulsD2Vq8tvKrFHj1ozESyMFDbXV2e2Oa-CQsbC-UzCsYbSU99rvClkwABzSwuw4dWNcrcBPnmM6ImA9Rj9fwg8hM11GQhIbHhQYLeBgNs6R6ttOXlNKekdxFsPiy1e0ryYR2HiMeZsXW5MFy-y7udsbCxLMndwPCESr3Ogrtd9tuH1f1fFoqXpnCi3-egJpYgv4aLABQTcd22XPUCuo3ZhpUVRswYIG7btUvmwfQD4A","description":null,"type":"text","enabled":true}]
     * @param data
     * @return
     */
//    @FormUrlEncoded
    @POST("/backend-api/conversation")
    Call<String> query(
            @Header("Content-Type") String contentType,
            @Header("User-Agent") String userAgent,
            @Header("Authorization") String authorization,
                       @Body RequestBody data);
}
