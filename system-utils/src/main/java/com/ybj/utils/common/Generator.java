package com.ybj.utils.common;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;


/**
 *  1. 注意： 数据库里为Number类型的字段
 *   1~4    --short类型
 *   5~9    --Integer类型
 *   10~18  --Long类型
 *   18+    --BigDecimal类型
 *   所以建表时需要指定Number字段的长度
 *
 *  2. 注意
 *   实体类主键需要手写
 *   如
 *   @TableId(type= IdType.ASSIGN_UUID)
 *
 *  3. 注意
 *    TBL_USER  生成 User对象
 *    需要去除表名的前缀
 *    setTablePrefix("TBL", "SYS","MPM")
 *
 *  4. 注意
 *  getDataSourceConfig函数中需要设置 数据库类型
 *  mysql  --- DbType.MYSQL
 *
 *
 * @author baojian.yuan
 * @date 2020/1/13
 * @time 17:52
 */
public class Generator {

    public static void main(String[] args) {
        Generator g = new Generator();
        boolean startWithI = false;
        // 1.模块名
        String projectName = "system-crawler";
        // 2.代码位置
        String packageName = "com.ybj.crawler";
        // 3.表名
        String[] tableNameArray = {"tbl_folder"};
        g.generateByTables(startWithI,projectName,packageName, tableNameArray);
    }
    /**
     * 根据表自动生成
     *
     * @param serviceNameStartWithI 默认为false
     * @param packageName           包名
     * @param tableNames            表名
     */
    private void generateByTables(boolean serviceNameStartWithI,String projectName, String packageName, String... tableNames) {
        // 1.配置数据源
        DataSourceConfig dataSourceConfig = getDataSourceConfig();
        // 2.策略配置
        StrategyConfig strategyConfig = getStrategyConfig(tableNames);
        // 3.全局变量配置
        GlobalConfig globalConfig = getGlobalConfig(serviceNameStartWithI,projectName);
        // 4.包名配置
        PackageConfig packageConfig = getPackageConfig(packageName);
        // 5.自动生成
        atuoGenerator(dataSourceConfig, strategyConfig, globalConfig, packageConfig);
    }

    /**
     * 集成
     *
     * @param dataSourceConfig 配置数据源
     * @param strategyConfig   策略配置
     * @param config           全局变量配置
     * @param packageConfig    包名配置
     */
    private void atuoGenerator(DataSourceConfig dataSourceConfig, StrategyConfig strategyConfig, GlobalConfig config, PackageConfig packageConfig) {
        new AutoGenerator()
                .setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(packageConfig)
                .setTemplateEngine(new VelocityTemplateEngine())
                .execute();
    }

    /**
     * 设置包名
     *
     * @param packageName 父路径包名
     * @return PackageConfig 包名配置
     */
    private PackageConfig getPackageConfig(String packageName) {
        return new PackageConfig()
                .setParent(packageName)
                .setXml("mapping")
                .setMapper("dao")
                .setController("controller")
                .setService("service")
                .setEntity("model");
    }

    /**
     * 全局配置
     *
     * @param serviceNameStartWithI false
     * @return GlobalConfig
     */
    private GlobalConfig getGlobalConfig(boolean serviceNameStartWithI,String projectName) {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig
                .setBaseColumnList(true)
                .setBaseResultMap(true)
                .setActiveRecord(false)
                .setAuthor("ybj")
                // 设置输出路径
                .setOutputDir(System.getProperty("user.dir")+ "/" +projectName +"/src/main/java/")
                // 是否覆盖文件
                .setFileOverride(true);
        if (!serviceNameStartWithI) {
            //设置service名
            globalConfig.setServiceName("%sService");
        }
        return globalConfig;
    }

    /**
     * 策略配置
     *
     * @param tableNames 表名
     * @return StrategyConfig
     */
    private StrategyConfig getStrategyConfig(String... tableNames) {
        return new StrategyConfig()
                // 全局大写命名 ORACLE 注意
                .setCapitalMode(true)
                .setEntityLombokModel(true)
                .setRestControllerStyle(true)
                //从数据库表到文件的命名策略
                .setNaming(NamingStrategy.underline_to_camel)
                .setColumnNaming(NamingStrategy.underline_to_camel)
                //需要生成的的表名，多个表名传数组
                .setInclude(tableNames)
                .setTablePrefix("TBL", "SYS","MPM");
    }

    /**
     * 配置数据源
     *
     * @return 数据源配置 DataSourceConfig
     */
    private DataSourceConfig getDataSourceConfig() {
        return new DataSourceConfig().setDbType(DbType.MYSQL)
                .setUrl("jdbc:mysql://yuanbaojian.xyz:3306/rain_bow")
                .setUsername("rainBow")
                .setPassword("312ybj")
                .setDriverName("com.mysql.cj.jdbc.Driver");
    }

}