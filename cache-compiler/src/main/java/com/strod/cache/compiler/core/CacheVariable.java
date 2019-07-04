package com.strod.cache.compiler.core;


import com.strod.cache.annotation.Cacheable;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.type.TypeMirror;

/**
 * Created by laiying on 2019/6/21.
 */
public class CacheVariable {
    private Element rawType;        // Raw type of cache
    private String key;             // Cache key
    private Cacheable.CACHETYPE cacheType;    // Cache type
    private Cacheable.RW rw;        // cache read or write
//    private Cacheable.OPTTYPE optType;
    private Name name;
    private TypeMirror typeMirror;
    private int typeKind;

    private TypeMirror mErasedTypeMirror;
    private List<? extends TypeMirror> typeArgs;


    public Element getRawType() {
        return rawType;
    }

    public void setRawType(Element rawType) {
        this.rawType = rawType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Cacheable.CACHETYPE getCacheType() {
        return cacheType;
    }

    public void setCacheType(Cacheable.CACHETYPE cacheType) {
        this.cacheType = cacheType;
    }

   /* public Cacheable.OPTTYPE getOptType() {
        return optType;
    }

    public void setOptType(Cacheable.OPTTYPE optType) {
        this.optType = optType;
    }*/

    public Cacheable.RW getRw() {
        return rw;
    }

    public void setRw(Cacheable.RW rw) {
        this.rw = rw;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public TypeMirror getTypeMirror() {
        return typeMirror;
    }

    public void setTypeMirror(TypeMirror typeMirror) {
        this.typeMirror = typeMirror;
    }

    public int getTypeKind() {
        return typeKind;
    }

    public void setTypeKind(int typeKind) {
        this.typeKind = typeKind;
    }

    public TypeMirror getErasedTypeMirror() {
        return mErasedTypeMirror;
    }

    public void setErasedTypeMirror(TypeMirror erasedTypeMirror) {
        mErasedTypeMirror = erasedTypeMirror;
    }

    public List<? extends TypeMirror> getTypeArgs() {
        return typeArgs;
    }

    public void setTypeArgs(List<? extends TypeMirror> typeArgs) {
        this.typeArgs = typeArgs;
    }
}
