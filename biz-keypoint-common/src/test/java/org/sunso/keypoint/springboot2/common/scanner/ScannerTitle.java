package org.sunso.keypoint.springboot2.common.scanner;

/**
 * @author sunso520
 * @Title:ScannerTitle
 * @Description: <br>
 * @Created on 2024/4/7 16:06
 */
public class ScannerTitle {
    @Scanner(key = "title",name = "标题", parentId = 101)
    public void scannerTitle() {

    }

    @Scanner(key = "subTitle",name = "子标题", parentId = 102)
    public void scannerSubTitle() {

    }
}
