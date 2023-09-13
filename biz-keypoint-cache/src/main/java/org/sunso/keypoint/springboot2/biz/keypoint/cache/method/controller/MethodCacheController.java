package org.sunso.keypoint.springboot2.biz.keypoint.cache.method.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/method/cache/")
public class MethodCacheController {

    @GetMapping("/remove/one/{cacheType}/{key}")
    public void removeKey(@PathVariable String cacheType, @PathVariable String key) {

    }
}
