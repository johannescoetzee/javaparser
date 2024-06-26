/*
 * Copyright (C) 2013-2024 The JavaParser Team.
 *
 * This file is part of JavaParser.
 *
 * JavaParser can be used either under the terms of
 * a) the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * b) the terms of the Apache License
 *
 * You should have received a copy of both licenses in LICENCE.LGPL and
 * LICENCE.APACHE. Please refer to those files for details.
 *
 * JavaParser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 */

package com.github.javaparser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.javaparser.ast.CompilationUnit;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

public class Issue3064Test {

    @Test
    public void test0() {
        String str = "import java.util.function.Supplier;\n" + "\n"
                + "public class MyClass {\n"
                + "\n"
                + "    public MyClass() {\n"
                + "        Supplier<String> aStringSupplier = false ? () -> \"\" : true ? () -> \"\" : () -> \"path\";\n"
                + "    }\n"
                + "}\n";

        JavaParser parser = new JavaParser();
        ParseResult<CompilationUnit> unitOpt = parser.parse(new StringReader(str));
        unitOpt.getProblems().stream().forEach(p -> System.err.println(p.toString()));
        CompilationUnit unit = unitOpt.getResult().orElseThrow(() -> new IllegalStateException("Could not parse file"));

        assertEquals(str, unit.toString());
    }

    @Test
    public void test1() {
        String str = "public class MyClass {\n" + "    {\n"
                + "        Supplier<String> aStringSupplier = false ? () -> \"F\" : true ? () -> \"T\" : () -> \"path\";\n"
                + "    }\n"
                + "}";
        CompilationUnit unit = StaticJavaParser.parse(str);
        assertEquals(str.replace("\n", ""), unit.toString().replace("\n", ""));
    }
}
