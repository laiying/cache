package com.strod.cache.compiler.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by laiying on 2019/3/25.
 */
public class CacheMeta {

    private String pkgName;
    private String clsName;

    private List<CacheVariable> mCacheVariables;

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getClsName() {
        return clsName;
    }

    public void setClsName(String clsName) {
        this.clsName = clsName;
    }

    public List<CacheVariable> getCacheVariables() {
        return mCacheVariables;
    }

    public void setCacheVariables(List<CacheVariable> cacheVariables) {
        mCacheVariables = cacheVariables;
    }

    public void addCacheVariable(CacheVariable cacheVariable){
        if (mCacheVariables == null){
            mCacheVariables = new ArrayList<>();
        }
        mCacheVariables.add(cacheVariable);
    }
}
