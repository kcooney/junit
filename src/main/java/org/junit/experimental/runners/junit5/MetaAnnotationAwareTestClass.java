package org.junit.experimental.runners.junit5;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMember;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

class MetaAnnotationAwareTestClass extends TestClass {

    public MetaAnnotationAwareTestClass(Class<?> clazz) {
        super(clazz);
    }

    @Override
    protected Iterable<Annotation> getAnnotations(FrameworkMethod method) {
        return getAllAnnotations(method);
    }

    @Override
    protected Iterable<Annotation> getAnnotations(FrameworkField field) {
        return getAllAnnotations(field);
    }

    private <T extends FrameworkMember<T>> Iterable<Annotation> getAllAnnotations(T member) {
        List<Annotation> result = new ArrayList<Annotation>();
        for (Annotation annotation : member.getAnnotations()) {
            result.add(annotation);
            result.addAll(getMetaAnnotations(annotation.annotationType()));
        }
        return result;
    }

    private static List<Annotation> getMetaAnnotations(Class<? extends Annotation> annotationType) {
        List<Annotation> metaAnnotations = new ArrayList<Annotation>();
        for (Annotation each : annotationType.getAnnotations()) {
            if (each.annotationType().getAnnotation(MetaType.class) != null) {
                metaAnnotations.add(each);
            }
        }
        return metaAnnotations;
    }
}
