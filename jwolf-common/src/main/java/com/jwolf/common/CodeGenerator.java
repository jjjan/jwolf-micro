package com.jwolf.common;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class CodeGenerator {


    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置

        GlobalConfig gc = new GlobalConfig();
        String projectRootPath = System.getProperty("user.dir");
        String moduleName = scanner("请输入模块名:[user,msg,goods,travel,payment,activity]");
        gc.setOutputDir(projectRootPath + "/jwolf-service/jwolf-service-" + moduleName + "/src/main/java");
        gc.setAuthor("jwolf");
        gc.setOpen(false);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(false);
        gc.setFileOverride(true);//重新生成时是否覆盖
        //gc.setSwagger2(true);
        gc.setServiceName("I%sService");  // 默认service接口名IXXXService 自定义指定之后就不会用I开头了
        gc.setControllerName("%sController");
        gc.setServiceImplName("%sServiceImpl");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setUrl("jdbc:mysql://192.168.154.137:3306/jwolf_" + moduleName + "?useSSL=false&serverTimezone=UTC");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        mpg.setDataSource(dsc);
        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(moduleName);
        pc.setEntity("api.entity");
        pc.setParent("com.jwolf.service");
        mpg.setPackageInfo(pc);
        // 自定义配置
        InjectionConfig cfg =
                new InjectionConfig() {
                    @Override
                    public void initMap() {
                    }
                };

        // 模板引擎 freemarker/velocity/beetlsql
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(
                new FileOutConfig("/templates/mapper.xml.ftl") {
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                        return projectRootPath + "/jwolf-service/jwolf-service-" + moduleName
                                + "/src/main/resources/mapper/"
                                + tableInfo.getEntityName()
                                + "Mapper"
                                + StringPool.DOT_XML;
                    }
                });
        focList.add(
                new FileOutConfig("/templates/entity.java.ftl") {
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        return projectRootPath + "/jwolf-service-api/jwolf-service-" + moduleName + "-api"
                                + "/src/main/java/com/jwolf/service/" + moduleName + "/api/entity/"
                                + tableInfo.getEntityName()
                                + StringPool.DOT_JAVA;
                    }
                });
        focList.add(
                new FileOutConfig("/templatesMybatisPlus/controller.java.ftl") { //自定义的controller模板
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        return projectRootPath + "/jwolf-service/jwolf-service-" + moduleName
                                + "/src/main/java/com/jwolf/service/" + moduleName + "/controller/"
                                + tableInfo.getEntityName()
                                + "Controller"
                                + StringPool.DOT_JAVA;
                    }
                });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        templateConfig.setEntity(null); //上面配置了自定义输出，不输出默认的咯
        templateConfig.setController(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setSuperEntityClass("com.baomidou.mybatisplus.extension.activerecord.Model");
        strategy.setSuperMapperClass("com.baomidou.mybatisplus.core.mapper.BaseMapper");
        strategy.setSuperServiceClass("com.baomidou.mybatisplus.extension.service.IService");
        strategy.setSuperServiceImplClass("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl");
        //strategy.setSuperControllerClass("com.jwolf.common.base.controller.BaseController");
        strategy.setInclude(scanner("请输入表名,多个表名可逗号分隔"));
        strategy.setControllerMappingHyphenStyle(false); //影响 @RequestMapping的路径格式
        strategy.setTablePrefix("t_");
        strategy.setLogicDeleteFieldName("deleted"); // 逻辑删除字段
        TableFill createTime = new TableFill("create_time", FieldFill.INSERT);
        TableFill updateTime = new TableFill("update_time", FieldFill.UPDATE);
        strategy.setTableFillList(Lists.newArrayList(createTime, updateTime));// 设置填充自动与规则
        strategy.setVersionFieldName("version"); // 设置乐观锁
        mpg.setStrategy(strategy);
        mpg.execute();
    }


    /**
     * 读取控制台内容
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

}
