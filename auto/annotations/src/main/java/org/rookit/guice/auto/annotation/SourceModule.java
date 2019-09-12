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
package org.rookit.guice.auto.annotation;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.util.Modules;
import org.rookit.auto.SourceUtilsModule;
import org.rookit.auto.javapoet.SourceJavaPoetLibModule;
import org.rookit.auto.javax.JavaxLibModule;
import org.rookit.auto.javax.naming.NamingFactory;
import org.rookit.auto.source.CodeSourceFactories;
import org.rookit.auto.source.CodeSourceFactory;
import org.rookit.auto.source.SourceLibModule;
import org.rookit.auto.source.spec.SpecFactory;
import org.rookit.auto.source.type.TypeSource;
import org.rookit.failsafe.FailsafeModule;
import org.rookit.guice.auto.GuiceAutoLibModule;
import org.rookit.guice.auto.annotation.config.ConfigurationModule;
import org.rookit.guice.auto.annotation.type.TypeModule;
import org.rookit.io.IOLibModule;
import org.rookit.io.path.PathModule;
import org.rookit.serializer.SerializationBundleModule;
import org.rookit.utils.guice.UtilsModule;


@SuppressWarnings("MethodMayBeStatic")
public final class SourceModule extends AbstractModule {

    private static final Module MODULE = Modules.override(
            SourceLibModule.getModule(),
            SourceUtilsModule.getModule()
    ).with(
            new SourceModule(),
            ConfigurationModule.getModule(),
            FailsafeModule.getModule(),
            GuiceAutoLibModule.getModule(),
            IOLibModule.getModule(),
            JavaxLibModule.getModule(),
            PathModule.getModule(),
            SerializationBundleModule.getModule(),
            SourceJavaPoetLibModule.getModule(),
            TypeModule.getModule(),
            UtilsModule.getModule()
    );

    public static Module getModule() {
        return MODULE;
    }

    private SourceModule() {}

    @Override
    protected void configure() {
        bind(NamingFactory.class).to(Key.get(NamingFactory.class, Guice.class)).in(Singleton.class);
    }

    @Provides
    @Singleton
    CodeSourceFactory sourceFactory(final SpecFactory<TypeSource> specFactory,
                                    final CodeSourceFactories factories) {
        return factories.specCodeSourceFactory(specFactory);
    }

}
