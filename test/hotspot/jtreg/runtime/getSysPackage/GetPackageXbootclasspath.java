/*
 * Copyright (c) 2017, 2023, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * @test
 * @bug 8187436
 * @summary Test that getPackage() works with a class loaded via -Xbootclasspath/a.
 * @library /test/lib
 * @run driver GetPackageXbootclasspath
 */

// This is a regression test for a bug with the exploded build but should pass
// when run with either the normal or exploded build.
import jdk.test.lib.compiler.InMemoryJavaCompiler;
import jdk.test.lib.process.ProcessTools;
import jdk.test.lib.process.OutputAnalyzer;
import jdk.test.lib.helpers.ClassFileInstaller;

public class GetPackageXbootclasspath {

    public static void main(String args[]) throws Exception {

        String Test_src =
            "package P; " +
            "public class Test { " +
                "public static void main(String[] args) throws Exception { " +
                    "Package p = Test.class.getPackage(); " +
                    "System.out.println(\"Test Passed\"); " +
                "} " +
            "}";

        String test_classes = System.getProperty("test.classes");
        ClassFileInstaller.writeClassToDisk("P/Test",
            InMemoryJavaCompiler.compile("P.Test", Test_src), test_classes);

        new OutputAnalyzer(ProcessTools.createLimitedTestJavaProcessBuilder(
                "-Xbootclasspath/a:" + test_classes, "P.Test")
            .start()).shouldContain("Test Passed")
            .shouldHaveExitValue(0);
    }
}
