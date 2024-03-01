package com.senhai.common.datasources;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:数据源配置文件
 * @projectName:senhai-huang
 * @see:com.senhai.common.datasources
 * @author:HW
 * @createTime:2024/2/29 15:05
 * @version:1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.druid",ignoreInvalidFields = true)
@PropertySource("classpath:application.properties")
public class DataSourceProperties {
    //连接池的配置信息
    private String masterUrl;

    private String slaveUrl;
    private String username;
    private String password;
    private String driverClassName;
    //初始化连接大小
    private int initialSize;
    //最小连接池数量
    private int minIdle;
    //最大连接池数量
    private int maxActive;
    //配置获取连接等待超时的时间
    private int maxWait;
    //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
    private int timeBetweenEvictionRunsMillis;
    //配置一个连接在池中最小生存的时间，单位是毫秒
    private int minEvictableIdleTimeMillis;
    //测试连接
    private String validationQuery;
    //申请连接时检测，建议配置为true，不影响性能，并且保证安全性
    private boolean testWhileIdle;
    //获取连接时检测，建议关闭，影响性能
    private boolean testOnBorrow;
    //归还连接时检测，建议关闭，影响性能
    private boolean testOnReturn;
    //预编译池化计划：PsCache，只对游标类数据库（oracle）起作用，mysql建议关闭
    private boolean poolPreparedStatements;
    //开启poolPreparedStatements时有效，当数值大于0时会默认打开poolPreparedStatements
    private int maxPoolPreparedStatementPerConnectionSize;
    //配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
    private String filters;
    //通过connectProperties属性来打开mergeSql功能；慢SQL记录
    private String connectionProperties;



    /**
     * description 主库
     * param []
     * return javax.sql.DataSource
     * author HW
     * createTime 2024/3/1 10:26
     **/
    public DataSource getMaster() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(masterUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setInitialSize(initialSize);
        dataSource.setMaxActive(maxActive);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxWait(maxWait);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);
        dataSource.setPoolPreparedStatements(poolPreparedStatements);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        try {
            dataSource.setFilters(filters);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }


    /**
     * description 从库
     * param []
     * return javax.sql.DataSource
     * author HW
     * createTime 2024/3/1 10:25
     **/
    public DataSource getSlave() {
        DruidDataSource slaveDataSource = new DruidDataSource();
        slaveDataSource.setDriverClassName(driverClassName);
        slaveDataSource.setUrl(masterUrl);
        slaveDataSource.setUsername(username);
        slaveDataSource.setPassword(password);
        slaveDataSource.setInitialSize(initialSize);
        slaveDataSource.setMaxActive(maxActive);
        slaveDataSource.setMinIdle(minIdle);
        slaveDataSource.setMaxWait(maxWait);
        slaveDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        slaveDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        slaveDataSource.setValidationQuery(validationQuery);
        slaveDataSource.setTestWhileIdle(testWhileIdle);
        slaveDataSource.setTestOnBorrow(testOnBorrow);
        slaveDataSource.setTestOnReturn(testOnReturn);
        slaveDataSource.setPoolPreparedStatements(poolPreparedStatements);
        slaveDataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        try {
            slaveDataSource.setFilters(filters);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slaveDataSource;
    }

    /**
     * description 配置数据源
     * param []
     * return com.senhai.common.datasources.DynamicDataSource
     * author HW
     * createTime 2024/3/1 10:25
     **/
    @Bean
    public DynamicDataSource dataSource () {
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DataSourceType.MASTER.getName(),getMaster());
        dataSourceMap.put(DataSourceType.SLAVE.getName(),getSlave());
        DynamicDataSource.addDataSource(DataSourceType.MASTER.getName());
        DynamicDataSource.addDataSource(DataSourceType.SLAVE.getName());
        return new DynamicDataSource(getMaster(),dataSourceMap);
    }

    @Bean
    public ServletRegistrationBean druidStateViewServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        //初始化参数initParams
        //添加白名单
        servletRegistrationBean.addInitParameter("allow","");
        //添加ip黑名单
        servletRegistrationBean.addInitParameter("deny","192.168.0.11");
        //登录查看信息的账号密码
        servletRegistrationBean.addInitParameter("loginUsername","admin");
        servletRegistrationBean.addInitParameter("loginPassword","123");
        //是否能够重置数据
        servletRegistrationBean.addInitParameter("resetEnable","false");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean druidStatFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        //添加过滤规则
        filterRegistrationBean.addUrlPatterns("/*");
        //添加不需要忽略的格式信息
        filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

}
