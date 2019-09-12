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
package org.rookit.guice.auto.annotation.config;

import org.rookit.auto.javax.pack.ExtendedPackageElement;
import org.rookit.auto.javax.pack.ExtendedPackageElementFactory;
import org.rookit.utils.object.DynamicObject;
import org.rookit.guice.auto.config.GuiceConfig;

final class GuiceConfigImpl implements GuiceConfig {

    private final DynamicObject configuration;
    private final ExtendedPackageElementFactory packageFactory;
    private final String name;

    GuiceConfigImpl(final DynamicObject configuration,
                    final ExtendedPackageElementFactory packageFactory,
                    final String name) {
        this.configuration = configuration;
        this.packageFactory = packageFactory;
        this.name = name;
    }

    @Override
    public ExtendedPackageElement basePackage() {
        return this.packageFactory.create(this.configuration.getString("basePackage"));
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public boolean isEnabled() {
        return this.configuration.getBoolean("enabled");
    }

    @Override
    public String toString() {
        return "GuiceConfigImpl{" +
                "configuration=" + this.configuration +
                ", packageFactory=" + this.packageFactory +
                ", name='" + this.name + '\'' +
                "}";
    }
}
