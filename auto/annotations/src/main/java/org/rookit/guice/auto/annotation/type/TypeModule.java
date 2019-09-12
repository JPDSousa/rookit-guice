/*******************************************************************************
 * Copyright (C) 2018 Joao Sousa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.rookit.guice.auto.annotation.type;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.squareup.javapoet.TypeSpec;
import one.util.streamex.StreamEx;
import org.rookit.auto.guice.GuiceBindAnnotation;
import org.rookit.auto.javapoet.doc.JavaPoetJavadocTemplate1;
import org.rookit.auto.javapoet.identifier.JavaPoetIdentifierFactory;
import org.rookit.auto.javapoet.type.ExtendedElementTypeSpecVisitors;
import org.rookit.auto.javax.ExtendedElement;
import org.rookit.auto.javax.visitor.ExtendedElementVisitor;
import org.rookit.auto.source.spec.SpecFactories;
import org.rookit.auto.source.spec.SpecFactory;
import org.rookit.guice.auto.annotation.BindingAnnotationGenerator;
import org.rookit.utils.registry.Registry;

import javax.lang.model.type.TypeMirror;
import java.util.function.Function;

@SuppressWarnings("MethodMayBeStatic")
public final class TypeModule extends AbstractModule {

    private static final Module MODULE = new TypeModule();

    public static Module getModule() {
        return MODULE;
    }

    private TypeModule() {}

    @SuppressWarnings({"AnonymousInnerClassMayBeStatic", "AnonymousInnerClass", "EmptyClass"})
    @Override
    protected void configure() {
        bind(new TypeLiteral<Function<ExtendedElement, TypeMirror>>() {}).to(AnnotationExtractionFunction.class)
                .in(Singleton.class);
    }

    @Provides
    @Singleton
    ExtendedElementVisitor<StreamEx<TypeSpec>, Void> visitor(
            final ExtendedElementTypeSpecVisitors visitors,
            final JavaPoetIdentifierFactory idFactory,
            final Function<ExtendedElement, TypeMirror> extractionFunction,
            @GuiceBindAnnotation final JavaPoetJavadocTemplate1 javadoc) {
        return visitors.annotationBuilder(idFactory, Void.class)
                .withRecursiveVisiting(StreamEx::append)
                .filterIfAnnotationAbsent(BindingAnnotationGenerator.class)
                .bindingAnnotation()
                .withClassJavadoc(javadoc)
                .copyBodyFrom(extractionFunction)
                .buildTypeSpec()
                .build();
    }

    @Provides
    @Singleton
    SpecFactory<TypeSpec> typeSpecFactory(final SpecFactories factories,
                                          final ExtendedElementVisitor<StreamEx<TypeSpec>, Void> visitor) {
        return factories.fromVisitor(visitor);
    }

    @Provides
    @Singleton
    @GuiceBindAnnotation
    JavaPoetJavadocTemplate1 javadocTemplateRaw(final Registry<String, JavaPoetJavadocTemplate1> templates) {
        return templates.fetch("bindingAnnotation.json").blockingGet();
    }

}
