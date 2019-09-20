package com.ctrip.framework.apollo.portal.entity.bo;


public  class SearchItemBO {
    private String appId;
    private String namespaceName;
    private String key;
    public SearchItemBO(){
    }

    public SearchItemBO(String appId, String namespaceName, String key){
        this.appId = appId;
        this.namespaceName = namespaceName;
        this.key = key;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }


    public String getNamespaceName() {
        return namespaceName;
    }

    public void setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
