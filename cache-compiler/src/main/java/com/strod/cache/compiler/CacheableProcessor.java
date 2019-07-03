package com.strod.cache.compiler;

import com.google.auto.service.AutoService;
import com.strod.cache.annotation.Cacheable;
import com.strod.cache.compiler.core.CacheMeta;
import com.strod.cache.compiler.core.CachePoetImpl;
import com.strod.cache.compiler.core.CacheVariable;
import com.strod.cache.compiler.utils.Logger;
import com.strod.cache.compiler.utils.TypeUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by laiying on 2019/3/22.
 */
@AutoService(Processor.class)
public class CacheableProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Elements mElementUtils;
    private Types types;
    private TypeUtils typeUtils;

    private Logger logger;

    private Map<String, CacheMeta> mCacheMetas;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        types = processingEnv.getTypeUtils();
        logger = new Logger(processingEnv.getMessager());
        typeUtils = new TypeUtils(types, mElementUtils, logger);
        mCacheMetas = new LinkedHashMap<>();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(Cacheable.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mCacheMetas.clear();
        if (annotations != null && !annotations.isEmpty()) {
            Set<? extends Element> routeElements = roundEnv.getElementsAnnotatedWith(Cacheable.class);
            try {
                logger.info(">>> Found cacheable, start... <<<");
                this.parseCache(routeElements);

            } catch (Exception e) {
                logger.error(e);
            }
            return true;
        }

        return false;

    }


    private void parseCache(Set<? extends Element> cacheableElements) throws IOException {
        if (cacheableElements != null && !cacheableElements.isEmpty()) {
            logger.info(">>> Found caches, size is " + cacheableElements.size() + " <<<");
            for (Element element : cacheableElements) {
                //1.package name
                PackageElement packageElement = mElementUtils.getPackageOf(element);
                String pkName = packageElement.getQualifiedName().toString();
                logger.info(String.format("package = %s", pkName));//com.agile.library


                //2.package type
//                TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
//                String clsName = enclosingElement.getQualifiedName().toString();
//                logger.info(String.format("clsName = %s", clsName));//com.agile.library.BaseApplication

//            Element enclosingElement = element.getEnclosingElement();
                if (element instanceof TypeElement) {//类型元素，注意枚举对应的时类，注解对应的接口
                    logger.info("Element is TypeElement");

                    TypeElement typeElement = (TypeElement) element;
                    logger.info(String.format("TypeElement = %s", typeElement));//com.agile.library.BaseApplication


//                for (Element e : typeElement.getEnclosedElements()){ // iterate over children
//                    Element parent = e.getEnclosingElement();  // parent == fooClass
//                }

                    String enclosingName = typeElement.getQualifiedName().toString();
                    logger.info(String.format("enclosingName = %s", enclosingName));

                    ElementKind elementKind = typeElement.getKind();
                    logger.info(String.format("elementKind = %s", elementKind.toString()));
                    logger.info(String.format("elementKind isField: %s", elementKind.isField()));
                    logger.info(String.format("elementKind isClass: %s", elementKind.isClass()));
                    logger.info(String.format("elementKind isInterface: %s", elementKind.isInterface()));

                    TypeMirror typeMirror = element.asType();
                    logger.info(String.format("typeMirror = %s", typeMirror.toString()));

                    TypeKind typeKind = typeMirror.getKind();
                    logger.info(String.format("CustomTypeKind = %s", typeKind.toString()));

                    Set<Modifier> modifiers = typeElement.getModifiers();
                    for (Modifier modifier : modifiers) {
                        logger.info(String.format("modifier = %s", modifier.toString()));
                    }

                    Cacheable cacheable = typeElement.getAnnotation(Cacheable.class);
                    logger.info(String.format("cacheable key = %s  type = %s", cacheable.key(), cacheable.cacheType()));

                    boolean isPrimitive = typeKind.isPrimitive();
                    logger.info(String.format("isPrimitive = %s", isPrimitive));


                    logger.info("--------------------");

                } else if (element instanceof ExecutableElement) {
                    logger.info("Element is ExecutableElement");
                    ExecutableElement executableElement = (ExecutableElement) element;

                    logger.info(String.format("executableElement = %s", executableElement));//com.agile.library.BaseApplication


                    Name name = executableElement.getSimpleName();
                    logger.info(String.format("name = %s", name));

                    TypeMirror typeMirror = executableElement.getReturnType();
                    logger.info(String.format("typeMirror = %s", typeMirror.toString()));

                    TypeKind typeKind = typeMirror.getKind();
                    logger.info(String.format("CustomTypeKind = %s", typeKind.toString()));

                    ElementKind elementKind = executableElement.getKind();
                    logger.info(String.format("elementKind = %s", elementKind.toString()));

                    Cacheable cacheable = executableElement.getAnnotation(Cacheable.class);
                    logger.info(String.format("cacheable key = %s  type = %s", cacheable.key(), cacheable.cacheType()));


                    logger.info("--------------------");

                } else if (element instanceof VariableElement) {
                    logger.info("Element is VariableElement");
                    VariableElement variableElement = (VariableElement) element;

                    Name name = variableElement.getSimpleName();
                    logger.info(String.format("name = %s", name));//mCache

                    Element e = variableElement.getEnclosingElement();
                    String claName = e.getSimpleName().toString();
                    logger.info(String.format("getEnclosingElement = %s", claName));//

                    TypeMirror typeMirror = variableElement.asType();
                    logger.info(String.format("typeMirror = %s", typeMirror.toString()));//com.agile.library.model.User

                    TypeKind typeKind = typeMirror.getKind();
                    logger.info(String.format("CustomTypeKind = %s", typeKind.toString()));//DECLARED

                    ElementKind elementKind = variableElement.getKind();
                    logger.info(String.format("elementKind = %s", elementKind.name()));//FIELD

                    Set<Modifier> modifiers = variableElement.getModifiers();
                    boolean isPublic = true;
                    for (Modifier modifier : modifiers){
                        if (Modifier.PRIVATE == modifier || Modifier.STATIC == modifier){
                            isPublic = false;
                        }
                    }
                    if (!isPublic){
                        //
                        logger.error(String.format("%s.%s.%s must not be private or static", pkName, claName, name));
                        logger.info("--------------------");
                        continue;
                    }

                    Cacheable cacheable = variableElement.getAnnotation(Cacheable.class);
                    logger.info(String.format("cacheable key = %s  type = %s", cacheable.key(), cacheable.cacheType()));//key = hello  type = FILE

                    CacheMeta cacheMeta = mCacheMetas.get(pkName+claName);
                    if (cacheMeta == null){
                        cacheMeta = new CacheMeta();
                        cacheMeta.setPkgName(pkName);
                        cacheMeta.setClsName(claName);
                    }

                    CacheVariable cacheVariable = new CacheVariable();
                    cacheVariable.setRawType(variableElement);
                    cacheVariable.setCacheType(cacheable.cacheType());
                    cacheVariable.setKey(cacheable.key());
//                    cacheVariable.setOptType(cacheable.optType());
                    cacheVariable.setName(name);
                    cacheVariable.setTypeMirror(typeMirror);
                    cacheVariable.setTypeKind(typeUtils.typeExchange(variableElement));
                    cacheVariable.setTypeArgs(typeUtils.typeArguments(variableElement));

                    cacheMeta.addCacheVariable(cacheVariable);

                    mCacheMetas.put(pkName+claName, cacheMeta);

//                    MethodSpec readCache = null;
//                    if (cacheable.cacheType() == Cacheable.CACHETYPE.DISK){
//                        //User diskUser = DiskCacheManager.getInstance().readCache("diskUser", User.class);
//                        //.addStatement("return new $T()", Date.class)
//                        readCache = MethodSpec.methodBuilder("read"+ name)
//                                .addModifiers(Modifier.PUBLIC)
////                                .returns(ClassName.get(typeMirror))
//                                .returns(TypeName.VOID)
////                                .addParameter(String.class, cacheable.key())
//                                .addParameter(ClassName.get(pkName, claName), claName)
//                                .addStatement(claName+"."+name.toString()+" = com.agile.cache.DiskCacheManager.getInstance().readCache($S,  $T.class)", cacheable.key(), typeMirror)
//                                .build();
//                    }else {
//                        readCache = MethodSpec.methodBuilder("read"+ name)
//                                .addModifiers(Modifier.PUBLIC)
////                                .returns(ClassName.get(typeMirror))
//                                .returns(TypeName.VOID)
////                                .addParameter(String.class, cacheable.key())
//                                .addParameter(ClassName.get(pkName, claName), claName)
//                                .addStatement(claName+"."+name.toString()+" = com.agile.cache.SharePrefersManager.getInstance().get($S,  $T.class)", cacheable.key(), typeMirror)
//                                .build();
//                    }
//
//
//                    TypeSpec typeSpec = TypeSpec.classBuilder(claName + "$$CACHE")
////                            .addSuperinterface(ClassName.get("com.agile.cache", "CacheBinder"))
//                            .addModifiers(Modifier.PUBLIC)
//                            .addMethod(readCache)
//                            .build();
//                    JavaFile javaFile = JavaFile.builder(pkName, typeSpec).build();
//                    try {
//                        javaFile.writeTo(mFiler);
//                    } catch (IOException e1) {
//                        e1.printStackTrace();
//                        logger.error(e1.getMessage());
//                    }


//                    new CachePoetImpl(mFiler).generate();

                    logger.info("--------------------");

                } else if (element instanceof TypeParameterElement) {
                    logger.info("Element is TypeParameterElement");

                    TypeParameterElement typeParameterElement = (TypeParameterElement) element;
                    Name name = typeParameterElement.getSimpleName();
                    logger.info(String.format("name = %s", name));

                    TypeMirror typeMirror = typeParameterElement.asType();
                    logger.info(String.format("typeMirror = %s", typeMirror.toString()));

                    TypeKind typeKind = typeMirror.getKind();
                    logger.info(String.format("CustomTypeKind = %s", typeKind.toString()));

                    Cacheable cacheable = typeParameterElement.getAnnotation(Cacheable.class);
                    logger.info(String.format("cacheable key = %s  type = %s", cacheable.key(), cacheable.cacheType()));


                    logger.info("--------------------");

                }
            }
            //
            for (Map.Entry<String, CacheMeta> entry : mCacheMetas.entrySet()) {
                new CachePoetImpl(mFiler, entry.getValue()).generate();
            }

        }
    }
}
