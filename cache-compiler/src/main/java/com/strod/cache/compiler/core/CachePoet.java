package com.strod.cache.compiler.core;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.strod.cache.compiler.utils.CacheConsts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.annotation.processing.FilerException;
import javax.lang.model.element.Modifier;

/**
 * Created by laiying on 2019/3/28.
 */
public abstract class CachePoet {

    protected final String FILE_HEADER = "\nFiles under the \"build\" folder are generated and should not be edited!";


    /**
     * inner class(If 'Test' is a innerclass, so com.test.MainActivity.Test, for'Test' its SimpleClassName is 'Test', ComplexClassName is 'MainActivity.Test')
     */

    protected Filer mFiler;

    protected String mHostPackageName;
    protected String mHostComplexClassName;
    protected ClassName mHostClassName;

    protected String mGeneratePackageName;
    protected String mGenerateComplexClassName;
    protected ClassName mGenerateClassName;

    public CachePoet(Filer filer, String generatePackageName, String generateComplexClassName) {
        mFiler = filer;

        mGeneratePackageName = generatePackageName;
        mGenerateComplexClassName = generateComplexClassName;
        mGenerateClassName = ClassName.get(mGeneratePackageName, mGenerateComplexClassName);
    }


    public CachePoet(Filer filer, String hostPackageName, String hostComplexClassName, String generatePackageName, String generateComplexClassName) {
        mFiler = filer;

        mHostPackageName = hostPackageName;
        mHostComplexClassName = hostComplexClassName;
        mHostClassName = ClassName.get(mHostPackageName, mHostComplexClassName);

        mGeneratePackageName = generatePackageName;
        mGenerateComplexClassName = generateComplexClassName.replace(".", CacheConsts.INNER_LINK);
        mGenerateClassName = ClassName.get(mGeneratePackageName, mGenerateComplexClassName);
    }


    public void generate() {

        TypeSpec.Builder bTypeSpec;
        if (TypeSpec.Kind.CLASS == getKind()) {
            bTypeSpec = TypeSpec.classBuilder(mGenerateComplexClassName+ CacheConsts.SUFFIX);
        } else {
            bTypeSpec = TypeSpec.interfaceBuilder(mGenerateComplexClassName+ CacheConsts.SUFFIX);
        }
        bTypeSpec.addModifiers(Modifier.PUBLIC);
        if (null != getParent()) {
            bTypeSpec.addSuperinterface(getParent());
        }
        //接口
        bTypeSpec.addSuperinterface(ParameterizedTypeName.get(ClassName.get(CacheConsts.API_PKG, "CacheBinder"), ClassName.get(mGeneratePackageName,mGenerateComplexClassName)));

        CodeBlock staticBlock = getStaticBlock();
        if (null != staticBlock) {
            bTypeSpec.addStaticBlock(staticBlock);
        }

        for (FieldSpec fieldSpec : getFields()) {
            bTypeSpec.addField(fieldSpec);
        }
        for (MethodSpec methodSpec : getMethods()) {
            bTypeSpec.addMethod(methodSpec);
        }
        JavaFile javaFile = JavaFile.builder(mGeneratePackageName, bTypeSpec.build()).addFileComment(FILE_HEADER).
                build();
        try {
            javaFile.writeTo(mFiler);
        } catch (FilerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected ClassName getParent() {
        return null;
    }

    public String getHostPackageName() {
        return mHostPackageName;
    }

    public String getHostComplexClassName() {
        return mHostComplexClassName;
    }

    public ClassName getHostClassName() {
        return mHostClassName;
    }

    public String getGeneratePackageName() {
        return mGeneratePackageName;
    }

    public String getGenerateComplexClassName() {
        return mGenerateComplexClassName;
    }

    public ClassName getGenerateClassName() {
        return mGenerateClassName;
    }

    protected List<FieldSpec> getFields() {
        return new ArrayList<>();
    }

    protected List<MethodSpec> getMethods() {
        return new ArrayList<>();
    }

    protected CodeBlock getStaticBlock() {
        return null;
    }

    protected TypeSpec.Kind getKind() {
        return TypeSpec.Kind.CLASS;
    }

    protected String convertType(String type) {
        String fullType = null;
        if ("int".equals(type)) {
            fullType = "java.lang.Integer";
        } else if ("long".equals(type)) {
            fullType = "java.lang.Long";
        } else if ("float".equals(type)) {
            fullType = "java.lang.Float";
        } else if ("boolean".equals(type)) {
            fullType = "java.lang.Boolean";
        }else {
            fullType = type;
        }
        return fullType;
    }
}
