package com.strod.cache.compiler.core;

import com.google.gson.reflect.TypeToken;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.strod.cache.compiler.enums.CustomTypeKind;
import com.strod.cache.compiler.utils.CacheConsts;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

/**
 * Created by laiying on 2019/3/28.
 */
public class CachePoetImpl extends CachePoet{


    private CacheMeta cacheMeta;

    public CachePoetImpl(Filer filer,CacheMeta cacheMeta) {
        super(filer, cacheMeta.getPkgName(), cacheMeta.getClsName());
        this.cacheMeta = cacheMeta;
    }

//    @Override
//    protected ClassName getParent() {
//        //ParameterizedTypeName.get(Comparator.class, String.class)
//        return ClassName.get("com.agile.cache", "CacheBinder<"+cacheMeta.getClsName() +">");
//    }


    @Override
    protected List<FieldSpec> getFields() {

        return super.getFields();
    }

    @Override
    protected List<MethodSpec> getMethods() {
        List<MethodSpec> methodSpecs = new ArrayList<>();
        List<MethodSpec> readMethodSpecs = new ArrayList<>();
        List<MethodSpec> writeMethodSpecs = new ArrayList<>();

        for (CacheVariable cacheVariable : cacheMeta.getCacheVariables()){
            MethodSpec readCache = null;
            MethodSpec writeCache = null;
            int typeKind = cacheVariable.getTypeKind();


            //read
            MethodSpec.Builder readBuilder = MethodSpec.methodBuilder(CacheConsts.CACHE_READ+ cacheVariable.getName())
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addParameter(ClassName.get(cacheMeta.getPkgName(), cacheMeta.getClsName()), cacheMeta.getClsName().toLowerCase());
//                        .addStatement("android.util.Log.d($S, $S)", mGenerateComplexClassName+ "$$CACHE", CacheConsts.CACHE_READ+ cacheVariable.getName());
            buildReadStatetement(typeKind, readBuilder, cacheVariable);
            readCache = readBuilder.build();

            //write
            MethodSpec.Builder writeBuilder = MethodSpec.methodBuilder(CacheConsts.CACHE_WRITE+ cacheVariable.getName())
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addParameter(ClassName.get(cacheMeta.getPkgName(), cacheMeta.getClsName()), cacheMeta.getClsName().toLowerCase());
            buildWriteStatetement(typeKind, writeBuilder, cacheVariable);
            writeCache = writeBuilder.build();

            readMethodSpecs.add(readCache);
            writeMethodSpecs.add(writeCache);

            methodSpecs.add(readCache);
            methodSpecs.add(writeCache);
        }

        //interface method
        MethodSpec.Builder saveBuilder = MethodSpec.methodBuilder(CacheConsts.CACHE_BINDER_METHOD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get(cacheMeta.getPkgName(), cacheMeta.getClsName()), cacheMeta.getClsName().toLowerCase())
                .returns(TypeName.VOID);
        buildSaveStatement(saveBuilder, writeMethodSpecs);
        MethodSpec saveCache = saveBuilder.build();
        methodSpecs.add(saveCache);


        //constructor
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get(cacheMeta.getPkgName(), cacheMeta.getClsName()), cacheMeta.getClsName().toLowerCase());
        buildReadStatement(constructorBuilder, readMethodSpecs);
        MethodSpec constructor = constructorBuilder.build();
        methodSpecs.add(constructor);

        return methodSpecs;
    }

    private MethodSpec.Builder buildReadStatement(MethodSpec.Builder builder, List<MethodSpec> readMethodSpecs){
        if (builder == null){
            return null;
        }
        if (readMethodSpecs == null){
            return builder;
        }
//        builder.addStatement("android.util.Log.d($S, $S)", mGenerateComplexClassName+ "$$CACHE", "readCache->");
        for (MethodSpec methodSpec : readMethodSpecs){
            //writemUser(mainactivity);
            builder.addStatement("$L($L)", methodSpec.name, cacheMeta.getClsName().toLowerCase());
        }

        return builder;
    }

    private MethodSpec.Builder buildSaveStatement(MethodSpec.Builder builder, List<MethodSpec> writeMethodSpecs){
        if (builder == null){
            return null;
        }
        if (writeMethodSpecs == null){
            return builder;
        }
//        builder.addStatement("android.util.Log.d($S, $S)", mGenerateComplexClassName+ "$$CACHE", "writeCache->");
        for (MethodSpec methodSpec : writeMethodSpecs){
            //writemUser(mainactivity);
            builder.addStatement("$L($L)", methodSpec.name, cacheMeta.getClsName().toLowerCase());
        }

        return builder;
    }

    private MethodSpec.Builder buildReadStatetement(int type, MethodSpec.Builder builder, CacheVariable cacheVariable){
        if (builder == null){
            return null;
        }
        ClassName className = ClassName.get(CacheConsts.API_PKG, CacheConsts.API_CACHE_MANAGER);
        if (type == CustomTypeKind.BOOLEAN.ordinal()) {
            builder.addStatement("$L.$L = $T.$L().getBoolean($L, $S)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    className,
                    CacheConsts.API_GETINSTANCE,
                    cacheVariable.getCacheType(),
                    cacheVariable.getKey());
        } else if (type == CustomTypeKind.BYTE.ordinal()) {
        } else if (type == CustomTypeKind.SHORT.ordinal()) {
        } else if (type == CustomTypeKind.INT.ordinal()) {
            builder.addStatement("$L.$L = $T.$L().getInt($L, $S)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    className,
                    CacheConsts.API_GETINSTANCE,
                    cacheVariable.getCacheType(),
                    cacheVariable.getKey());
        } else if (type == CustomTypeKind.LONG.ordinal()) {
            builder.addStatement("$L.$L = $T.$L().getLong($L, $S)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    className,
                    CacheConsts.API_GETINSTANCE,
                    cacheVariable.getCacheType(),
                    cacheVariable.getKey());
        }else if(type == CustomTypeKind.CHAR.ordinal()){
        } else if (type == CustomTypeKind.FLOAT.ordinal()) {
            builder.addStatement("$L.$L = $T.$L().getFloat($L, $S)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    className,
                    CacheConsts.API_GETINSTANCE,
                    cacheVariable.getCacheType(),
                    cacheVariable.getKey());
        } else if(type == CustomTypeKind.DOUBLE.ordinal()){
            builder.addStatement("$L.$L = $T.$L().getDouble($L, $S)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    className,
                    CacheConsts.API_GETINSTANCE,
                    cacheVariable.getCacheType(),
                    cacheVariable.getKey());
        }else if (type == CustomTypeKind.STRING.ordinal()) {
            builder.addStatement("$L.$L = $T.$L().getString($L, $S)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    className,
                    CacheConsts.API_GETINSTANCE,
                    cacheVariable.getCacheType(),
                    cacheVariable.getKey());
        } else if (type == CustomTypeKind.PARCELABLE.ordinal()) {
            builder.addStatement("$L.$L = $T.$L().get($L, $S,  $T.class)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    className,
                    CacheConsts.API_GETINSTANCE,
                    cacheVariable.getCacheType(),
                    cacheVariable.getKey(),
                    cacheVariable.getTypeMirror());
        } else if (type == CustomTypeKind.OBJECT.ordinal()) {
            //
            List<? extends TypeMirror> subTypeMirror = cacheVariable.getTypeArgs();
            if (subTypeMirror != null && !subTypeMirror.isEmpty()){
                builder.addStatement("$T type = new $T<$L>(){}.getType();\n$L.$L = $T.$L().get($L, $S,  type)",
                        Type.class,
                        TypeToken.class,
                        cacheVariable.getTypeMirror(),
                        cacheMeta.getClsName().toLowerCase(),
                        cacheVariable.getName().toString(),
                        className,
                        CacheConsts.API_GETINSTANCE,
                        cacheVariable.getCacheType(),
                        cacheVariable.getKey());

            }else {
                builder.addStatement("$L.$L = $T.$L().get($L, $S,  $T.class)",
                        cacheMeta.getClsName().toLowerCase(),
                        cacheVariable.getName().toString(),
                        className,
                        CacheConsts.API_GETINSTANCE,
                        cacheVariable.getCacheType(),
                        cacheVariable.getKey(),
                        cacheVariable.getTypeMirror());
            }
        }

        return builder;
    }

    private MethodSpec.Builder buildWriteStatetement(int type, MethodSpec.Builder builder, CacheVariable cacheVariable){
        if (builder == null){
            return null;
        }
        ClassName className = ClassName.get(CacheConsts.API_PKG, CacheConsts.API_CACHE_MANAGER);
        if (type == CustomTypeKind.BOOLEAN.ordinal()) {
            builder.addStatement("$T.$L().putBoolean($L, $S,  $L.$L)",
                    className,
                    CacheConsts.API_GETINSTANCE,
                    cacheVariable.getCacheType(),
                    cacheVariable.getKey(),
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString());
        } else if (type == CustomTypeKind.BYTE.ordinal()) {
        } else if (type == CustomTypeKind.SHORT.ordinal()) {
        } else if (type == CustomTypeKind.INT.ordinal()) {
            builder.addStatement("$T.$L().putInt($L, $S,  $L.$L)",
                    className,
                    CacheConsts.API_GETINSTANCE,
                    cacheVariable.getCacheType(),
                    cacheVariable.getKey(),
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString());
        } else if (type == CustomTypeKind.LONG.ordinal()) {
            builder.addStatement("$T.$L().putLong($L, $S,  $L.$L)",
                    className,
                    CacheConsts.API_GETINSTANCE,
                    cacheVariable.getCacheType(),
                    cacheVariable.getKey(),
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString());
        }else if(type == CustomTypeKind.CHAR.ordinal()){
        } else if (type == CustomTypeKind.FLOAT.ordinal() ) {
            builder.addStatement("$T.$L().putFloat($L, $S,  $L.$L)",
                    className,
                    CacheConsts.API_GETINSTANCE,
                    cacheVariable.getCacheType(),
                    cacheVariable.getKey(),
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString());
        }  else if (type == CustomTypeKind.DOUBLE.ordinal()) {
            builder.addStatement("$T.$L().putDouble($L, $S,  $L.$L)",
                    className,
                    CacheConsts.API_GETINSTANCE,
                    cacheVariable.getCacheType(),
                    cacheVariable.getKey(),
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString());
        } else if (type == CustomTypeKind.STRING.ordinal()) {
            builder.addStatement("$T.$L().putString($L, $S,  $L.$L)",
                    className,
                    CacheConsts.API_GETINSTANCE,
                    cacheVariable.getCacheType(),
                    cacheVariable.getKey(),
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString());
        } else if (type == CustomTypeKind.PARCELABLE.ordinal()) {
            builder.addStatement("$T.$L().put($L, $S,  $L.$L)",
                    className,
                    CacheConsts.API_GETINSTANCE,
                    cacheVariable.getCacheType(),
                    cacheVariable.getKey(),
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString());
        } else if (type == CustomTypeKind.OBJECT.ordinal()) {
            builder.addStatement("$T.$L().put($L, $S,  $L.$L)",
                    className,
                    CacheConsts.API_GETINSTANCE,
                    cacheVariable.getCacheType(),
                    cacheVariable.getKey(),
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString());
        }

        return builder;
    }

}

