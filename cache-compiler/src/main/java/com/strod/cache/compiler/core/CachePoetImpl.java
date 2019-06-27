package com.strod.cache.compiler.core;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.strod.cache.annotation.Cacheable;
import com.strod.cache.compiler.enums.CustomTypeKind;
import com.strod.cache.compiler.utils.CacheConsts;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/**
 * Created by laiying on 2019/3/28.
 */
public class CachePoetImpl extends CachePoet{


    private CacheMeta cacheMeta;

    private static String DISKCACHE = CacheConsts.API_PKG + ".DiskCacheManager.getInstance()";
    private static String SHAREPREFERS = CacheConsts.API_PKG + ".SharePrefersManager.getInstance()";

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
    protected List<MethodSpec> getMethods() {
        List<MethodSpec> methodSpecs = new ArrayList<>();
        List<MethodSpec> readMethodSpecs = new ArrayList<>();
        List<MethodSpec> writeMethodSpecs = new ArrayList<>();

        for (CacheVariable cacheVariable : cacheMeta.getCacheVariables()){
            MethodSpec readCache = null;
            MethodSpec writeCache = null;
            int typeKind = cacheVariable.getTypeKind();

            if (cacheVariable.getCacheType() == Cacheable.CACHETYPE.DISK){
                //read
                MethodSpec.Builder builder = MethodSpec.methodBuilder("read"+ cacheVariable.getName())
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.VOID)
                        .addParameter(ClassName.get(cacheMeta.getPkgName(), cacheMeta.getClsName()), cacheMeta.getClsName().toLowerCase());

                buildDiskReadStatetement(typeKind, builder, cacheVariable);
                readCache = builder.build();

                //write
                writeCache = MethodSpec.methodBuilder("write"+ cacheVariable.getName())
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.VOID)
                        .addParameter(ClassName.get(cacheMeta.getPkgName(), cacheMeta.getClsName()), cacheMeta.getClsName().toLowerCase())
                        .addStatement("$L.writeCache($S, $L.$L)",
                                DISKCACHE,
                                cacheVariable.getKey(),
                                cacheMeta.getClsName().toLowerCase(),
                                cacheVariable.getName().toString())
                        .build();
            }else {
                //read

                MethodSpec.Builder readBuilder = MethodSpec.methodBuilder("read"+ cacheVariable.getName())
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.VOID)
                        .addParameter(ClassName.get(cacheMeta.getPkgName(), cacheMeta.getClsName()), cacheMeta.getClsName().toLowerCase());
//                        .addStatement("android.util.Log.d($S, $S)", mGenerateComplexClassName+ "$$CACHE", "read"+ cacheVariable.getName());
                buildSPReadStatetement(typeKind, readBuilder, cacheVariable);
                readCache = readBuilder.build();

                //write
                MethodSpec.Builder writeBuilder = MethodSpec.methodBuilder("write"+ cacheVariable.getName())
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.VOID)
                        .addParameter(ClassName.get(cacheMeta.getPkgName(), cacheMeta.getClsName()), cacheMeta.getClsName().toLowerCase());
                buildSPWriteStatetement(typeKind, writeBuilder, cacheVariable);
                writeCache = writeBuilder.build();
            }

            readMethodSpecs.add(readCache);
            writeMethodSpecs.add(writeCache);

            methodSpecs.add(readCache);
            methodSpecs.add(writeCache);
        }

        //interface method
        MethodSpec.Builder saveBuilder = MethodSpec.methodBuilder("saveCache")
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

    private MethodSpec.Builder buildSPReadStatetement(int type,MethodSpec.Builder builder, CacheVariable cacheVariable){
        if (builder == null){
            return null;
        }
        if (type == CustomTypeKind.BOOLEAN.ordinal()) {
            builder.addStatement("$L.$L = $L.getBoolean($S)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    SHAREPREFERS,
                    cacheVariable.getKey());
        } else if (type == CustomTypeKind.BYTE.ordinal()) {
        } else if (type == CustomTypeKind.SHORT.ordinal()) {
        } else if (type == CustomTypeKind.INT.ordinal()) {
            builder.addStatement("$L.$L = $L.getInt($S)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    SHAREPREFERS,
                    cacheVariable.getKey());
        } else if (type == CustomTypeKind.LONG.ordinal()) {
            builder.addStatement("$L.$L = $L.getLong($S)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    SHAREPREFERS,
                    cacheVariable.getKey());
        }else if(type == CustomTypeKind.CHAR.ordinal()){
        } else if (type == CustomTypeKind.FLOAT.ordinal()) {
            builder.addStatement("$L.$L = $L.getFloat($S)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    SHAREPREFERS,
                    cacheVariable.getKey());
        } else if(type == CustomTypeKind.DOUBLE.ordinal()){
            builder.addStatement("$L.$L = $L.getDouble($S)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    SHAREPREFERS,
                    cacheVariable.getKey());
        }else if (type == CustomTypeKind.STRING.ordinal()) {
            builder.addStatement("$L.$L = $L.getString($S)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    SHAREPREFERS,
                    cacheVariable.getKey());
        } else if (type == CustomTypeKind.PARCELABLE.ordinal()) {
            builder.addStatement("$L.$L = $L.get($S,  $T.class)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    SHAREPREFERS,
                    cacheVariable.getKey(),
                    cacheVariable.getTypeMirror());
        } else if (type == CustomTypeKind.OBJECT.ordinal()) {
            builder.addStatement("$L.$L = $L.get($S,  $T.class)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    SHAREPREFERS,
                    cacheVariable.getKey(),
                    cacheVariable.getTypeMirror());
        } else if (type == CustomTypeKind.LIST.ordinal()) {
            builder.addStatement("$L.$L = $L.getList($S,  $T.class)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    SHAREPREFERS,
                    cacheVariable.getKey(),
                    cacheVariable.getTypeArgs().get(0));
        }

        return builder;
    }

    private MethodSpec.Builder buildSPWriteStatetement(int type,MethodSpec.Builder builder, CacheVariable cacheVariable){
        if (builder == null){
            return null;
        }
        if (type == CustomTypeKind.BOOLEAN.ordinal()) {
            builder.addStatement("$L.putBoolean($S,  $L.$L)",
                    SHAREPREFERS,
                    cacheVariable.getKey(),
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString());
        } else if (type == CustomTypeKind.BYTE.ordinal()) {
        } else if (type == CustomTypeKind.SHORT.ordinal()) {
        } else if (type == CustomTypeKind.INT.ordinal()) {
            builder.addStatement("$L.putInt($S,  $L.$L)",
                    SHAREPREFERS,
                    cacheVariable.getKey(),
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString());
        } else if (type == CustomTypeKind.LONG.ordinal()) {
            builder.addStatement("$L.putLong($S,  $L.$L)",
                    SHAREPREFERS,
                    cacheVariable.getKey(),
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString());
        }else if(type == CustomTypeKind.CHAR.ordinal()){
        } else if (type == CustomTypeKind.FLOAT.ordinal() ) {
            builder.addStatement("$L.putFloat($S,  $L.$L)",
                    SHAREPREFERS,
                    cacheVariable.getKey(),
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString());
        }  else if (type == CustomTypeKind.DOUBLE.ordinal()) {
            builder.addStatement("$L.putDouble($S,  $L.$L)",
                    SHAREPREFERS,
                    cacheVariable.getKey(),
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString());
        } else if (type == CustomTypeKind.STRING.ordinal()) {
            builder.addStatement("$L.putString($S,  $L.$L)",
                    SHAREPREFERS,
                    cacheVariable.getKey(),
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString());
        } else if (type == CustomTypeKind.PARCELABLE.ordinal()) {
            builder.addStatement("$L.put($S,  $L.$L)",
                    SHAREPREFERS,
                    cacheVariable.getKey(),
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString());
        } else if (type == CustomTypeKind.OBJECT.ordinal()) {
            builder.addStatement("$L.put($S,  $L.$L)",
                    SHAREPREFERS,
                    cacheVariable.getKey(),
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString());
        }else if (type == CustomTypeKind.LIST.ordinal()) {
            builder.addStatement("$L.putList($S,  $L.$L)",
                    SHAREPREFERS,
                    cacheVariable.getKey(),
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString());
        }

        return builder;
    }

    /**
     * DiskCacheManager.getInstance().read()
     * @param type
     * @param builder
     * @param cacheVariable
     * @return
     */
    private MethodSpec.Builder buildDiskReadStatetement(int type,MethodSpec.Builder builder, CacheVariable cacheVariable) {
        if (builder == null){
            return null;
        }
        if (type == CustomTypeKind.BOOLEAN.ordinal()) {
            builder.addStatement("$L.$L = $L.readBooleanCache($S)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    DISKCACHE,
                    cacheVariable.getKey());
        } else if (type == CustomTypeKind.BYTE.ordinal()) {
        } else if (type == CustomTypeKind.SHORT.ordinal()) {
        } else if (type == CustomTypeKind.INT.ordinal()) {
            builder.addStatement("$L.$L = $L.readIntCache($S)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    DISKCACHE,
                    cacheVariable.getKey());
        } else if (type == CustomTypeKind.LONG.ordinal()) {
            builder.addStatement("$L.$L = $L.readLongCache($S)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    DISKCACHE,
                    cacheVariable.getKey());
        }else if(type == CustomTypeKind.CHAR.ordinal()){
        } else if (type == CustomTypeKind.FLOAT.ordinal()) {
            builder.addStatement("$L.$L = $L.readFloatCache($S)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    DISKCACHE,
                    cacheVariable.getKey());
        } else if (type == CustomTypeKind.DOUBLE.ordinal()) {
            builder.addStatement("$L.$L = $L.readDoubleCache($S)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    DISKCACHE,
                    cacheVariable.getKey());
        } else if (type == CustomTypeKind.STRING.ordinal()) {
            builder.addStatement("$L.$L = $L.readCache($S)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    DISKCACHE,
                    cacheVariable.getKey());
        } else if (type == CustomTypeKind.PARCELABLE.ordinal()) {
            builder.addStatement("$L.$L = $L.readCache($S,  $T.class)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    DISKCACHE,
                    cacheVariable.getKey(),
                    cacheVariable.getTypeMirror());
        } else if (type == CustomTypeKind.OBJECT.ordinal()) {
            builder.addStatement("$L.$L = $L.readCache($S,  $T.class)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    DISKCACHE,
                    cacheVariable.getKey(),
                    cacheVariable.getTypeMirror());
        }else if (type == CustomTypeKind.LIST.ordinal()) {
            builder.addStatement("$L.$L = $L.readListCache($S,  $T.class)",
                    cacheMeta.getClsName().toLowerCase(),
                    cacheVariable.getName().toString(),
                    DISKCACHE,
                    cacheVariable.getKey(),
                    cacheVariable.getTypeArgs().get(0));
        }

        return builder;
    }

}

