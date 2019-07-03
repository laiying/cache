package com.strod.cache.compiler.utils;


import com.strod.cache.compiler.enums.CustomTypeKind;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static com.strod.cache.compiler.utils.CacheConsts.BOOLEAN;
import static com.strod.cache.compiler.utils.CacheConsts.BYTE;
import static com.strod.cache.compiler.utils.CacheConsts.DOUBEL;
import static com.strod.cache.compiler.utils.CacheConsts.FLOAT;
import static com.strod.cache.compiler.utils.CacheConsts.INTEGER;
import static com.strod.cache.compiler.utils.CacheConsts.LIST;
import static com.strod.cache.compiler.utils.CacheConsts.LONG;
import static com.strod.cache.compiler.utils.CacheConsts.PARCELABLE;
import static com.strod.cache.compiler.utils.CacheConsts.SHORT;
import static com.strod.cache.compiler.utils.CacheConsts.STRING;


/**
 * Utils for type exchange
 *
 * @author zhilong <a href="mailto:zhilong.lzl@alibaba-inc.com">Contact me.</a>
 * @version 1.0
 * @since 2017/2/21 下午1:06
 */
public class TypeUtils {

    private Types types;
    private Elements elements;
    private TypeMirror parcelableType;
    private TypeMirror listType;
    private Logger logger;


    public TypeUtils(Types types, Elements elements, Logger logger) {
        this.types = types;
        this.elements = elements;
        this.logger = logger;

        parcelableType = this.elements.getTypeElement(PARCELABLE).asType();
        listType = types.erasure(this.elements.getTypeElement(LIST).asType());
    }

    /**
     * Diagnostics out the true java type
     *
     * @param element Raw type
     * @return Type class of java
     */
    public int typeExchange(Element element) {
        TypeMirror typeMirror = element.asType();

        // Primitive
        if (typeMirror.getKind().isPrimitive()) {
            return element.asType().getKind().ordinal();
        }

        switch (typeMirror.toString()) {
            case CacheConsts.BYTE:
                return CustomTypeKind.BYTE.ordinal();
            case SHORT:
                return CustomTypeKind.SHORT.ordinal();
            case INTEGER:
                return CustomTypeKind.INT.ordinal();
            case LONG:
                return CustomTypeKind.LONG.ordinal();
            case FLOAT:
                return CustomTypeKind.FLOAT.ordinal();
            case DOUBEL:
                return CustomTypeKind.DOUBLE.ordinal();
            case BOOLEAN:
                return CustomTypeKind.BOOLEAN.ordinal();
            case STRING:
                return CustomTypeKind.STRING.ordinal();
            default:    // Other side, maybe the PARCELABLE or OBJECT.
                if (types.isSubtype(typeMirror, parcelableType)) {  // PARCELABLE
                    logger.info(String.format("is parcelableType:%s", typeMirror.toString()));
                    return CustomTypeKind.PARCELABLE.ordinal();
                } else {    // For others
                    //erasure return List for List<E>
                    //List
//                    TypeMirror erasedType = types.erasure(typeMirror);
//                    logger.info(String.format("listType:%s", listType.toString()));
//                    logger.info(String.format("erasedType:%s", erasedType.toString()));
//
//                    //java.util.List<com.agile.library.model.User>
//                    boolean isListType =  types.isSameType(erasedType, listType);
//                    boolean contains =  types.contains(erasedType, listType);
//                    boolean isSubtype =  types.isSubtype(erasedType, listType);
//                    boolean isList =  types.isAssignable(erasedType, listType);/* List is Assingnble List<E> */
//                    logger.info(String.format("isListType:%s, contains:%s , isSubtype:%s , isAssignable:%s", isListType,contains,isSubtype,isList));
//                    if (isListType){
//                        return CustomTypeKind.LIST.ordinal();
//                    }
//                    if (isSubtype){
//                        return CustomTypeKind.LIST_SUB.ordinal();
//                    }

                    return CustomTypeKind.OBJECT.ordinal();
                }
        }
    }

    public List<? extends TypeMirror> typeArguments(Element element){
        TypeMirror typeMirror = element.asType();

        // Primitive
        if (typeMirror.getKind().isPrimitive()) {
            return null;
        }

        switch (typeMirror.toString()) {
            case BYTE:
            case SHORT:
            case INTEGER:
            case LONG:
            case FLOAT:
            case DOUBEL:
            case BOOLEAN:
            case STRING:
                return null;
            default:
                //List
                TypeMirror erasedType = types.erasure(typeMirror);
                logger.info(String.format("erasedType:%s", erasedType.toString()));
                boolean isList =  types.isAssignable(erasedType, listType);
//                if (isList){
                    javax.lang.model.type.TypeKind typeKind = typeMirror.getKind();
                    if (typeKind == javax.lang.model.type.TypeKind.DECLARED) {
                        DeclaredType declaredType = (DeclaredType) typeMirror;
                        List<? extends TypeMirror> mirrors = declaredType.getTypeArguments();
                        for (TypeMirror mirror : mirrors) {
                            logger.info(String.format("getTypeArguments:%s", mirror.toString()));
//                            //com.agile.library.model.User
                        }
                        return mirrors;
                    }
//                }

                return null;
        }
    }
}
