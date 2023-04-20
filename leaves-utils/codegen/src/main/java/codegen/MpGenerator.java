package codegen;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 * @author: ryan
 * @date: 2023/4/4 17:48
 **/
public class MpGenerator {

    private static final Props PROPS = new Props("application.properties");

    /**
     * GlobalConfig 全局策略配置
     */
    private GlobalConfig getGlobalConfig() {
        GlobalConfig gc = new GlobalConfig();
		//生成文件的输出目录
        gc.setOutputDir(PROPS.getStr("project.path") + "/src/main/java");
		// 是否覆盖已有文件
        gc.setFileOverride(false);
		// 是否打开输出目录
        gc.setOpen(false);
		// 开启 swagger2 模式
        gc.setSwagger2(false);
		// 开发人员
        gc.setAuthor(PROPS.getStr("global.author"));
		// 开启 ActiveRecord 模式,默认false
        gc.setActiveRecord(false);
		//指定生成的主键的ID类型，该类型为未设置主键类型(将跟随全局)
        gc.setIdType(IdType.NONE);
		// 开启 BaseResultMap
        gc.setBaseResultMap(true);
		// 开启 baseColumnList
        gc.setBaseColumnList(true);
		//使用JDK8的日期时间API，默认TIME_PACK
        gc.setDateType(DateType.TIME_PACK);
		// 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setEntityName("%s");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");

        return gc;
    }

    /**
     * DataSourceConfig 数据源配置
     */
    private DataSourceConfig getDataSourceConfig() {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDriverName(PROPS.getStr("datasource.driverName"));
        dsc.setUrl(PROPS.getStr("datasource.url"));
        dsc.setUsername(PROPS.getStr("datasource.username"));
        dsc.setPassword(PROPS.getStr("datasource.password"));

        return dsc;
    }

    /**
     * PackageConfig 包配置
     */
    private PackageConfig getPackageConfig() {
        PackageConfig pc = new PackageConfig();
		//父级包名
        pc.setParent(PROPS.getStr("package.parent"));
        String moduleName = PROPS.getStr("package.moduleName", "");
        if (StrUtil.isNotBlank(moduleName)) {
			//模块名
            pc.setModuleName(moduleName);
        }
        pc.setEntity("model");
        pc.setMapper("mapper");
        pc.setXml("mapper");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setController("controller");

        return pc;
    }

    /**
     * 自定义需要填充的字段
     */
    private List<TableFill> getTableFills() {
        List<TableFill> tableFillList = new ArrayList<>();
        tableFillList.add(new TableFill("gmt_create", FieldFill.INSERT));
        tableFillList.add(new TableFill("gmt_update", FieldFill.INSERT_UPDATE));
        return tableFillList;
    }

    /**
     * StrategyConfig 数据表配置
     */
    private StrategyConfig getStrategyConfig() {
        List<TableFill> tableFillList = getTableFills();

        StrategyConfig strategyConfig = new StrategyConfig();
        // 是否大写命名
        strategyConfig.setCapitalMode(false);
        String [] tables = PROPS.getStr("strategy.include").split(",");
        // 需要包含的表名
        strategyConfig.setInclude(tables);
        // 表前缀
        strategyConfig.setTablePrefix(PROPS.getStr("strategy.fieldPrefix"));
        // 表名生成策略
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        // 是否生成字段常量
        strategyConfig.setEntityColumnConstant(false);
        //是否生成Lombok风格
        strategyConfig.setEntityLombokModel(true);
        //Boolean类型的属性是否去除is前缀
        strategyConfig.setEntityBooleanColumnRemoveIsPrefix(true);
        // 是否生成字段注解
        strategyConfig.setEntityTableFieldAnnotationEnable(true);
        // 逻辑删除属性名称
        strategyConfig.setLogicDeleteFieldName("is_deleted");
        //乐观锁属性名称
        strategyConfig.setVersionFieldName("version");
        //表填充字段
        strategyConfig.setTableFillList(tableFillList);
        //自定义基础的Entity类，公共字段
        strategyConfig.setSuperEntityColumns("id", "gmt_create", "gmt_update", "create_by", "update_by");
        // 自定义继承的Entity类全称，带包名
        strategyConfig.setSuperEntityClass("com.github.ryanddu.ext.UpdateSnowFlakeEntity");
        // 自定义继承的Mapper类全称，带包名
        strategyConfig.setSuperMapperClass("com.github.ryanddu.ext.SuperMapper");
        // 自定义继承的Service类全称，带包名
        strategyConfig.setSuperServiceClass("ccom.github.ryanddu.ext.SuperService");
        // 自定义继承的ServiceImpl类全称，带包名
        strategyConfig.setSuperServiceImplClass("com.github.ryanddu.ext.SuperServiceImpl");
        //是否生成RestController风格
        strategyConfig.setRestControllerStyle(true);
        // 驼峰转连字符
        strategyConfig.setControllerMappingHyphenStyle(true);
        //自定义继承的Controller类全称，带包名
        strategyConfig.setSuperControllerClass("com.github.ryanddu.ext.SuperController");

        return strategyConfig;
    }

    /**
     * templateConfig 模板配置
     */
    private TemplateConfig getTemplateConfig() {
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        return templateConfig;
    }

    /**
     * InjectionConfig 注入配置
     */
    private InjectionConfig getInjectionConfig() {
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("apiVersion", PROPS.getStr("api.version"));
                this.setMap(map);
            }
        };

        String templatePath = "/templates/mapper.xml.ftl";

        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return PROPS.getStr("project.path") + "/src/main/resources/mapper/" + PROPS.getStr("package.moduleName", "") + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);

        return cfg;
    }

    /**
     * 代码生成
     */
    void execute() {
        AutoGenerator mpg = new AutoGenerator();

        mpg.setDataSource(getDataSourceConfig());
        mpg.setStrategy(getStrategyConfig());
        mpg.setPackageInfo(getPackageConfig());
        mpg.setTemplate(getTemplateConfig());
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.setGlobalConfig(getGlobalConfig());
        mpg.setCfg(getInjectionConfig());

        mpg.execute();
    }


    /**
     * 执行代码生成
     *
     * @param args
     */
    public static void main(String[] args) {
        MpGenerator mpGenerator = new MpGenerator();
        mpGenerator.execute();
    }
}