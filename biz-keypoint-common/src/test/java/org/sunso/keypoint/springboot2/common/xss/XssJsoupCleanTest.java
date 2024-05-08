package org.sunso.keypoint.springboot2.common.xss;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/**
 * @author sunso520
 * @Title:XssJsoupCleanTest
 * @Description: <br>
 * @Created on 2024/4/16 10:31
 */
public class XssJsoupCleanTest {

    public static void cleanImage() {
        String html = "<img STYLE=\"background-image:url(javascript:alert('XSS'))\">";
        String result = XssJsoupClean.clean(html);
        print(result);
    }

    public static void cleanScript() {
        String html = "<script>alert('ddd')</script>";
        print(XssJsoupClean.clean(html));
    }

    public static void cleanATarget() {
        String html = "<p><a href='http://www.baidu/' target='stealCookies()'> 百度一下，你就知道 </a></p>";
        print(XssJsoupClean.clean(html));
    }

    private static void print(String msg) {
        System.out.println("msg:" +msg);
    }

    public static void clearA() {
        String html = "<p><a href='http://www.baidu/' οnclick='javascript:alert('XSS');stealCookies()'> 百度一下，你就知道 </a></p>";
        Safelist safelist = XssJsoupClean.buildSafelist();
        System.out.println(Jsoup.isValid(html, safelist));
        System.out.println(Jsoup.clean(html, safelist));
    }

    public static void cleanScript2() {
        String html = "<script>alert('Hello, World!');</script><p>Some <b>bold</b> text.</p>";
        String result = Jsoup.clean(html, Safelist.relaxed());
        print(result);
    }

    public static void cleanJavaScript2() {
        String html = "<img STYLE=\"background-image:url(javascript:alert('XSS'))\"> <p>Some <b>bold</b> text.</p>";
        String result = Jsoup.clean(html, Safelist.relaxed());
        print(result);
    }

    public static void main(String[] args) {
        //cleanImage();
        //cleanScript();
        //cleanATarget();
        clearA();
        //cleanJavaScript2();
    }
}
