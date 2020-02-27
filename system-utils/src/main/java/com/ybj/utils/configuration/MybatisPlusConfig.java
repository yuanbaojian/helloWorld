package com.ybj.utils.configuration;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.DynamicTableNameParser;
import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * MybatisPlus分页插件配置
 * @author caicai.gao
 */
@EnableTransactionManagement
@Configuration
@MapperScan("com.atoz.mpm.*.*.dao*")
public class MybatisPlusConfig {
    public static final ThreadLocal<String> dynamicTableName = new ThreadLocal<>();

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        ArrayList<ISqlParser> sqlParserList = new ArrayList<>();
        // 动态表名解析器
        DynamicTableNameParser dynamicTableNameParser = new DynamicTableNameParser();
        Map<String, ITableNameHandler> tableNameHandlerMap = new HashMap<>();
        tableNameHandlerMap.put("MPM_PROC_PROCESSVIEW", (metaObject, sql, tableName) -> dynamicTableName.get());
        tableNameHandlerMap.put("MPM_PROC_STATION", (metaObject, sql, tableName) -> dynamicTableName.get());
        tableNameHandlerMap.put("MPM_PROC_OPERATION", (metaObject, sql, tableName) -> dynamicTableName.get());
        tableNameHandlerMap.put("MPM_PROC_HISTORY", (metaObject, sql, tableName) -> dynamicTableName.get());
        tableNameHandlerMap.put("MPM_PROC_PCI", (metaObject, sql, tableName) -> dynamicTableName.get());
        tableNameHandlerMap.put("MPM_PROC_MATERIAL", (metaObject, sql, tableName) -> dynamicTableName.get());
        tableNameHandlerMap.put("MPM_PROC_AO", (metaObject, sql, tableName) -> dynamicTableName.get());
        tableNameHandlerMap.put("MPM_PROC_ACCESSORY", (metaObject, sql, tableName) -> dynamicTableName.get());
        tableNameHandlerMap.put("MPM_PROC_FO", (metaObject, sql, tableName) -> dynamicTableName.get());
        dynamicTableNameParser.setTableNameHandlerMap(tableNameHandlerMap);
        sqlParserList.add(dynamicTableNameParser);

        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(false));
        paginationInterceptor.setSqlParserList(sqlParserList);
        return paginationInterceptor;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer(){
        return builder -> builder.featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    }

}

