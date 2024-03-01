package com.senhai.common.datasources;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:动态数据源配置
 * @projectName:senhai-huang
 * @see:com.senhai.common.datasources
 * @author:HW
 * @createTime:2024/2/21 14:03
 * @version:1.0
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    //当前线程使用的数据源存储器
    private final static ThreadLocal<String> DATASOURCE_HOLDER = new ThreadLocal<>();

    private static List<String> dataSourceIds = new ArrayList<>();


    /**
     * description 组装动态数据源
     * param [defaultDataSource, dataSourceMap]
     * return
     * author HW
     * createTime 2024/2/28 18:07
     **/
    public DynamicDataSource(DataSource defaultDataSource, Map<Object, Object> dataSourceMap) {
        super.setDefaultTargetDataSource(defaultDataSource);
        super.setTargetDataSources(dataSourceMap);
        super.afterPropertiesSet();
    }


    //获取当前线程使用的数据源
    @Override
    protected String determineCurrentLookupKey() {
        try {
            Field targetDataSources = this.getClass().getSuperclass().getDeclaredField("targetDataSources");
            //关闭安全检查，提高反射获取属性的效率
            targetDataSources.setAccessible(true);
            Map<Object, Object> dataSourceMap = null;
            //获取当前类targetDataSources属性具体值
            dataSourceMap = (Map<Object, Object>) targetDataSources.get(this);
            //获取当前数据源的名称
            String dataSourceId = DATASOURCE_HOLDER.get();
            //当前数据源名称不为空，匹配出当前数据源的具体地址
            if (!StringUtils.isEmpty(dataSourceId)) {
                log.info("当前数据源:{}，数据源具体地址为:{}", dataSourceId, dataSourceMap.get(dataSourceId));
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return getDataSouce();
    }

    /**
     * description 获取数据源，如果数据源不存在则设置默认数据源
     * param
     * return java.lang.String
     * author HW
     * createTime 2024/2/21 14:09
     **/
    public static String getDataSouce() {
        String dataSource = DATASOURCE_HOLDER.get();
        //当前线程没有数据源，设置默认数据源
        if (StringUtils.isEmpty(dataSource)) {
            DynamicDataSource.setDataSource(DataSourceType.MASTER.getDefault());
        }
        return DATASOURCE_HOLDER.get();
    }

    /**
     * description 设置数据源
     * param dataSource
     * return void
     * author HW
     * createTime 2024/2/21 14:14
     **/
    public static void setDataSource(String dataSource) {
        DATASOURCE_HOLDER.set(dataSource);
    }

    /**
     * description 判断是否包含此类数据源
     * param [dataSource]
     * return boolean
     * author HW
     * createTime 2024/3/1 14:00
     **/
    public static boolean isDataSourceExist(String dataSource) {
        if (dataSourceIds.contains(dataSource)) {
            return true;
        }
        return false;
    }


    /**
     * description 创建数据源时预置全部数据源名称
     * param [dataSourceId]
     * return void
     * author HW
     * createTime 2024/3/1 14:02
     **/
    public static void addDataSource(String dataSourceId) {
        dataSourceIds.add(dataSourceId);
    }


    /**
     * description 释放当前数据源
     * param []
     * return void
     * author HW
     * createTime 2024/3/1 14:15
     **/
    public static void removeDataSource() {
        DATASOURCE_HOLDER.remove();
    }

}
