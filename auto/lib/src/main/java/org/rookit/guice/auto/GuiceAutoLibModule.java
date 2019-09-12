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
package org.rookit.guice.auto;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.squareup.javapoet.AnnotationSpec;
import org.rookit.auto.guice.GuiceBindAnnotation;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactories;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javax.naming.NamingFactory;
import org.rookit.guice.auto.annotation.Guice;
import org.rookit.guice.auto.config.GuiceConfig;
import org.rookit.utils.guice.Self;
import org.rookit.utils.string.template.Template1;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@SuppressWarnings("MethodMayBeStatic")
public final class GuiceAutoLibModule extends AbstractModule {

    private static final Module MODULE = new GuiceAutoLibModule();

    public static Module getModule() {
        return MODULE;
    }

    private GuiceAutoLibModule() {}

    @Override
    protected void configure() {
        final Multibinder<AnnotationSpec> annotations = Multibinder
                .newSetBinder(binder(), AnnotationSpec.class, GuiceBindAnnotation.class);
        annotations.addBinding().toInstance(AnnotationSpec.builder(BindingAnnotation.class).build());
        // TODO this might be injected instead of created right here.
        annotations.addBinding().toInstance(AnnotationSpec.builder(Retention.class)
                .addMember("value", "$T.$L", RetentionPolicy.class, RUNTIME)
                .build());
        // TODO same as above
        annotations.addBinding().toInstance(AnnotationSpec.builder(Target.class)
                .addMember("value", "{$T.$L, $T.$L, $T.$L}",
                        ElementType.class, FIELD,
                        ElementType.class, METHOD,
                        ElementType.class, PARAMETER)
                .build());

        bind(NamingFactory.class).annotatedWith(Guice.class).to(Key.get(JavaPoetNamingFactory.class, Guice.class));
    }

    @Provides
    @Singleton
    @Guice
    JavaPoetNamingFactory namingFactory(final JavaPoetNamingFactories factories,
                                        final GuiceConfig config,
                                        @Self final Template1 noopTemplate) {
        return factories.create(config.basePackage(), noopTemplate, noopTemplate);
    }
}
