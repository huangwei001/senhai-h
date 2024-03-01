package com.senhai.common.datasources;

import org.apache.commons.lang3.StringUtils;

/**
 * @description:
 * @projectName:senhai-huang
 * @see:com.senhai.common.datasources
 * @author:HW
 * @createTime:2024/2/21 11:43
 * @version:1.0
 */
public enum DataSourceType {

    //主数据源
    MASTER("masterDataSource",true),
    //从数据源
    SLAVE("slaveDataSource",false),;

    private String name;
    private boolean master;

    DataSourceType(String name, boolean master){
        this.master = master;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }


    //获取默认数据源名称
    public String getDefault() {
        String defaultDataSource = "";
        for (DataSourceType dataSourceType : DataSourceType.values()) {
            if (!StringUtils.isBlank(defaultDataSource)) {
                break;
            }
            if (dataSourceType.master) {
                defaultDataSource = dataSourceType.getName();
            }
        }
        return defaultDataSource;
    }

}
