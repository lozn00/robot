package cn.qssq666.javatest;

/*
{
status: 0,
content: {
ph_en: "gʊd",
ph_am: "ɡʊd",
ph_en_mp3: "http://res.iciba.com/resource/amp3/oxford/0/28/a2/28a24294fed307cf7e65361b8da4f6e5.mp3",
ph_am_mp3: "http://res.iciba.com/resource/amp3/1/0/75/5f/755f85c2723bb39381c7379a604160d8.mp3",
ph_tts_mp3: "http://res-tts.iciba.com/7/5/5/755f85c2723bb39381c7379a604160d8.mp3",
word_mean: [
"adj. 好的;优秀的;有益的;漂亮的，健全的;",
"n. 好处，利益;善良;善行;好人;",
"adv. 同well;"
]
}
}


{
status: 1,
content: {
from: "zh-CN",
to: "en-US",
out: "hello",
vendor: "ciba",
err_no: 0
}
}

 */
public class Translation {
        private int status;

    private content content;
    private static class content {
        private String from;
        private String to;
        private String vendor;
        private String out;
        private int errNo;
    }

    //定义 输出返回数据 的方法
    public void show() {
        System.out.println(status);

        System.out.println(content.from);
        System.out.println(content.to);
        System.out.println(content.vendor);
        System.out.println(content.out);
        System.out.println(content.errNo);
    }
}

