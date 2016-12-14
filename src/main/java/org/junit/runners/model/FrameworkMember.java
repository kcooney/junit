package org.junit.runners.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parent class for {@link FrameworkField} and {@link FrameworkMethod}
 *
 * @since 4.7
 */
public abstract class FrameworkMember<T extends FrameworkMember<T>> implements
        Annotatable {
    private final Map<Class<? extends Annotation>, Annotation> annotationMap;

    FrameworkMember(AccessibleObject member) {
        if (member == null) {
            throw new NullPointerException(
                    "FrameworkMember cannot be created without an underlying member.");
        }
        annotationMap = new HashMap<Class<? extends Annotation>, Annotation>();
        for (Annotation annotation : member.getAnnotations()) {
            annotationMap.put(annotation.annotationType(), annotation);
        }
    }

    // Legacy constructor
    public FrameworkMember() {
        annotationMap = null;
    }

    abstract boolean isShadowedBy(T otherMember);

    boolean isShadowedBy(List<T> members) {
        for (T each : members) {
            if (isShadowedBy(each)) {
                return true;
            }
        }
        return false;
    }

    protected abstract int getModifiers();

    /**
     * Returns true if this member is static, false if not.
     */
    public boolean isStatic() {
        return Modifier.isStatic(getModifiers());
    }

    void addSyntheticAnnotation(Annotation annotation) {
        annotationMap.put(annotation.annotationType(), annotation);
    }

    /**
     * Returns the annotation of type {@code annotationType} on this method, if
     * one exists.
     */
    @SuppressWarnings("unchecked")
    public final <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        return (A) annotationMap.get(annotationType);
    }

    /**
     * Returns true if this member is public, false if not.
     */
    public boolean isPublic() {
        return Modifier.isPublic(getModifiers());
    }

    public abstract String getName();

    public abstract Class<?> getType();

    public abstract Class<?> getDeclaringClass();

    static <V> V notNull(V value, String message) {
        if (value == null) {
            throw new NullPointerException(message);
        }
        return value;
    }
}
