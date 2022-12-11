package cn.qssq666.robot.http.api.translate;

import java.util.List;

public class OpenAIRequestBody {
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getParent_message_id() {
        return parent_message_id;
    }

    public void setParent_message_id(String parent_message_id) {
        this.parent_message_id = parent_message_id;
    }

    public List<OpenAIRequestBodyForMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<OpenAIRequestBodyForMessage> messages) {
        this.messages = messages;
    }

    String action;
    String model;
    String parent_message_id;
    List<OpenAIRequestBodyForMessage> messages;
/*
    {
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
}
