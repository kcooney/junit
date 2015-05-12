package org.junit.internal.processors;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JUnitProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton("org.junit.JUnit");
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_6;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        logElementsAnnotatedWith(roundEnv, Before.class);
        logElementsAnnotatedWith(roundEnv, Test.class);
        logElementsAnnotatedWith(roundEnv, After.class);
        return true; // no further processing of this annotation type
    }

    private void logElementsAnnotatedWith(RoundEnvironment roundEnv, Class<? extends Annotation> annotation) {
        for (Element elem : roundEnv.getElementsAnnotatedWith(annotation)) {
            String message =  annotation.getSimpleName() + " " + elem.getSimpleName();
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
        }
    }
}
