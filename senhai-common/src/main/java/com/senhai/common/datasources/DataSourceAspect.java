package com.senhai.common.datasources;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @description: 多数据源拦截器：拦截带有TargetDataSource标签的方法，动态赋予数据源
 * @projectName:senhai-huang
 * @see:com.senhai.common.datasources
 * @author:HW
 * @createTime:2024/3/1 13:44
 * @version:1.0
 */
@Order(-1)
@Aspect
@Component
@Slf4j
public class DataSourceAspect {


    /**
     * description AOP拦截，在执行当前方法前拦截带有TargetDataSource标签的方法，查看当前数据源
     * param [point, targetDataSource]
     * return void
     * author HW
     * createTime 2024/3/1 14:12
     **/
    @Before("@annotation(TargetDataSource)")
    public void changeDataSource(JoinPoint point, TargetDataSource targetDataSource) {
        //获取标签标识的数据源
        String dataSourceType = targetDataSource.value();
        //判断标签标识的数据源是否存在
        if (DynamicDataSource.isDataSourceExist(dataSourceType)) {
            //如果存在的话，则当前数据源置为dataSourceType
            log.info("正在使用{}数据源 -> {}", dataSourceType, point.getSignature());
            DynamicDataSource.setDataSource(dataSourceType);
        }else {
            //当前标识的数据源不存在，则使用默认数据源
            log.info("数据源{}不存在，使用默认数据源 -> {}", dataSourceType,point.getSignature());
        }
    }


    /**
     * description 方法执行完毕后释放当前数据源
     * param [point, targetDataSource]
     * return void
     * author HW
     * createTime 2024/3/1 14:43
     **/
    @After("@annotation(TargetDataSource)")
    public void removeDataSource(JoinPoint point, TargetDataSource targetDataSource) {
        if (DynamicDataSource.getDataSouce() != null) {
            DynamicDataSource.removeDataSource();
            log.info("释放数据源{}对ThreadLocal的锁定", targetDataSource.value());
        }
    }


}
