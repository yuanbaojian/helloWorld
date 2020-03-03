package com.ybj.crawler.controller;


import com.baomidou.mybatisplus.core.injector.methods.SelectPage;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ybj.crawler.common.Constants;
import com.ybj.crawler.model.IpBean;
import com.ybj.crawler.model.IpBeanList;
import com.ybj.crawler.model.JsonResult;
import com.ybj.crawler.service.IpBeanService;
import com.ybj.crawler.utils.Crawler.IPCrawler.IPCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.ybj.crawler.utils.Crawler.IPCrawler.IPCrawler.getIpBeanList;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ybj
 * @since 2020-02-10
 */
@RestController
@RequestMapping("/IpBean")
public class IpBeanController {

    private static String HTTP_API = "https://www.xicidaili.com/wt/";


    @Autowired
    IpBeanService ipBeanService;

    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("/save")
    public void saveIpBeanToDB(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<IpBean> IpBeanList=null;
        IpBeanList = IPCrawler.getIpList(Constants.HTTPS_URL, 1);
        ipBeanService.saveBatch(IpBeanList);
    }

    @GetMapping("/test")
    public JsonResult test() throws IOException {
        JsonResult result=new JsonResult();
        return result;
    }

    @GetMapping("/getAllIp")
    public Page<IpBean> getAllIp(int pageNum, int pageSize) throws IOException {
        Page<IpBean> ipBeanPage = new Page<>(pageNum , pageSize, true);
        Page<IpBean> ipBeanPage1 = ipBeanService.page(ipBeanPage);
        return ipBeanPage1;
    }

    @GetMapping("/getValidIp")
    public void getValidIp() throws IOException {
        List<IpBean> IpBeans = getIpBeanList(HTTP_API,2, redisTemplate);
        List<IpBean> validIpList = IpBeanList.getIpBeanList();
        redisTemplate.opsForList().leftPushAll("validIpList", validIpList);
    }



}

