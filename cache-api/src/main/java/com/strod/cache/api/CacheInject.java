package com.strod.cache.api;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.util.Log;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;

/**
 * Created by laiying on 2019/6/21.
 */
public final class CacheInject {
    private CacheInject() {
        throw new AssertionError("No instances.");
    }

    private static final String TAG = "CacheInject";
    private static boolean debug = false;

    static final Map<Class<?>, Constructor<? extends CacheBinder>> BINDINGS = new LinkedHashMap<>();

    /** Control whether debug logging is enabled. */
    public static void setDebug(boolean debug) {
        CacheInject.debug = debug;
    }

    /**
     * Cacheable annotated fields in the specified {@code target} using the {@code source}
     *
     * @param target Target class for cache inject.
     */
    @NonNull @UiThread
    public static CacheBinder read(@NonNull Object target) {
        Class<?> targetClass = target.getClass();
        if ((targetClass.getModifiers() & PRIVATE) != 0) {
            throw new IllegalArgumentException(targetClass.getName() + " must not be private.");
        }
        if (debug) Log.d(TAG, "Looking up binding for " + targetClass.getName());

//        Field[] fields = target.getClass().getDeclaredFields();
//        for (Field field : fields) {
//            Cacheable cacheable = field.getAnnotation(Cacheable.class);
//                if (cacheable == null){
//                    continue;
//                }
//            validateMember(field);
//        }

        Constructor<? extends CacheBinder> constructor = findBindingConstructorForClass(targetClass);
        if (constructor == null) {
            return CacheBinder.EMPTY;
        }

        //noinspection TryWithIdenticalCatches Resolves to API 19+ only type.
        try {
            return constructor.newInstance(target);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new RuntimeException("Unable to create binding instance.", cause);
        }
    }


    @Nullable
    @CheckResult
    @UiThread
    private static Constructor<? extends CacheBinder> findBindingConstructorForClass(Class<?> cls) {
        Constructor<? extends CacheBinder> bindingCtor = BINDINGS.get(cls);
        if (bindingCtor != null || BINDINGS.containsKey(cls)) {
            if (debug) Log.d(TAG, "HIT: Cached in binding map.");
            return bindingCtor;
        }
        String clsName = cls.getName();
        if (clsName.startsWith("android.") || clsName.startsWith("java.")
                || clsName.startsWith("androidx.")) {
            if (debug) Log.d(TAG, "MISS: Reached framework class. Abandoning search.");
            return null;
        }
        try {
            Class<?> bindingClass = cls.getClassLoader().loadClass(clsName + "$$CACHE");
            if (debug) Log.d(TAG, "bindingClass:"+bindingClass.getName());
            //noinspection unchecked
            bindingCtor = (Constructor<? extends CacheBinder>) bindingClass.getConstructor(cls);
            if (debug) Log.d(TAG, "HIT: Loaded binding class and constructor.");
        } catch (ClassNotFoundException e) {
            if (debug) Log.d(TAG, "Not found. Trying superclass " + cls.getSuperclass().getName());
            bindingCtor = findBindingConstructorForClass(cls.getSuperclass());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find binding constructor for " + clsName, e);
        }
        BINDINGS.put(cls, bindingCtor);
        return bindingCtor;
    }

    private static <T extends AccessibleObject & Member> void validateMember(T object) {
        int modifiers = object.getModifiers();
        if ((modifiers & (PRIVATE | STATIC)) != 0) {
            throw new IllegalStateException(object.getDeclaringClass().getName()
                    + "."
                    + object.getName()
                    + " must not be private or static");
        }
        if ((modifiers & PUBLIC) == 0) {
            object.setAccessible(true);
        }
    }
}
