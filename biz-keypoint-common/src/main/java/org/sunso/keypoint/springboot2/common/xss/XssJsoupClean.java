package org.sunso.keypoint.springboot2.common.xss;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

/**
 * @author sunso520
 * @Title:XssJsoupClean
 * @Description: <br>
 * @Created on 2024/4/16 10:27
 */
public class XssJsoupClean {

    public static String clean(String html) {
        String baseUri = "";
        Safelist safelist = buildSafelist();
        System.out.println("isValid:" + Jsoup.isValid(html, safelist));
        return Jsoup.clean(html, baseUri, safelist, new Document.OutputSettings().prettyPrint(false));
    }


    public static Safelist buildSafelist() {
        // 使用 jsoup 提供的默认的
        Safelist relaxedSafelist = Safelist.relaxed();
        // 富文本编辑时一些样式是使用 style 来进行实现的
        // 比如红色字体 style="color:red;", 所以需要给所有标签添加 style 属性
        // 注意：style 属性会有注入风险 <img STYLE="background-image:url(javascript:alert('XSS'))">
        relaxedSafelist.addAttributes(":all", "style", "class");
        // 保留 a 标签的 target 属性
        relaxedSafelist.addAttributes("a", "target");
        // 支持img 为base64
        relaxedSafelist.addProtocols("img", "src", "data");

        // 保留相对路径, 保留相对路径时，必须提供对应的 baseUri 属性，否则依然会被删除
        // WHITELIST.preserveRelativeLinks(false);

        // 移除 a 标签和 img 标签的一些协议限制，这会导致 xss 防注入失效，如 <img src=javascript:alert("xss")>
        // 虽然可以重写 WhiteList#isSafeAttribute 来处理，但是有隐患，所以暂时不支持相对路径
        // WHITELIST.removeProtocols("a", "href", "ftp", "http", "https", "mailto");
        // WHITELIST.removeProtocols("img", "src", "http", "https");
        return relaxedSafelist;
    }
}
