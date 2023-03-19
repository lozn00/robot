package cn.qssq666.robot.http.api.translate;

import android.text.TextUtils;

import java.util.UUID;

import cn.qssq666.robot.utils.AppUtils;

public class OpenAIUtil {
    public static String parentMessageID = "";

    public static String getIDRadom() {
        return UUID.randomUUID().toString();// TextUtils.isEmpty(parentMessageID)?UUID.fromString("47f670d1-d0eb-4c93-8713-1be69cedfbaa").toString():UUID.fromString(parentMessageID).toString();
    }

    public static String conversation_id = "";

    public static String GenereateBodyByText(String conversation_id, String parentMessageID, String text) {
      String conversation_idField= TextUtils.isEmpty(conversation_id)?"":  "    \"conversation_id\": \"" + conversation_id + "\",\n" ;
        return "{\n" +
                "    \"action\": \"next\",\n" +
                "    \"messages\": [\n" +
                "        {\n" +
                "            \"id\": \"" + getIDRadom() + "\",\n" +
                "            \"role\": \"user\",\n" +
                "            \"content\": {\n" +
                "                \"content_type\": \"text\",\n" +
                "                \"parts\": [\n" +
                "                    \"" + text + "\"\n" +
                "                ]\n" +
                "            }\n" +
                "        }\n" +
                "    ],\n" +
              ""+conversation_idField+
                "    \"model\": \"text-davinci-002-render\",\n" +
                "    \"parent_message_id\": \"" + parentMessageID + "\"\n" +
                "}";
    }

    public static String GenereateBodyByTextUseApi( String text) {
        return "{\n" +
                "    \"model\": \"text-davinci-003\",\n" +
                "    \"prompt\": \""+text+"\",\n" +
                "    \"temperature\": 1,\n" +
                "    \"max_tokens\": 1000\n" +
                "}";
    }
    public static String GenereateBodyByTextUseApiGPT3_5( String text) {
        return "{\n" +
                "  \"model\": \"gpt-3.5-turbo\",\n" +
                "  \"messages\": [{\"role\": \"system\", \"content\": \""+ AppUtils.encodeUrl(text) +"\"}],\n" +
                "  \"temperature\": 0.7\n" +
                "}";


    }


}
