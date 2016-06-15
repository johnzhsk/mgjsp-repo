//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.mybatis.generator.api.dom;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class OutputUtilities {
    private static final String lineSeparator;

    private OutputUtilities() {
    }

    public static void javaIndent(StringBuilder sb, int indentLevel) {
        for(int i = 0; i < indentLevel; ++i) {
            sb.append("    ");
        }

    }

    public static void xmlIndent(StringBuilder sb, int indentLevel) {
        for(int i = 0; i < indentLevel; ++i) {
            sb.append("    ");
        }

    }

    public static void newLine(StringBuilder sb) {
        sb.append(lineSeparator);
    }

    public static Set<String> calculateImports(Set<FullyQualifiedJavaType> importedTypes) {
        StringBuilder sb = new StringBuilder();
        TreeSet importStrings = new TreeSet();
        Iterator i$ = importedTypes.iterator();

        while(i$.hasNext()) {
            FullyQualifiedJavaType fqjt = (FullyQualifiedJavaType)i$.next();
            Iterator i$1 = fqjt.getImportList().iterator();

            while(i$1.hasNext()) {
                String importString = (String)i$1.next();
                sb.setLength(0);
                sb.append("import ");
                sb.append(importString);
                sb.append(';');
                importStrings.add(sb.toString());
            }
        }

        return importStrings;
    }

    static {
        String ls = System.getProperty("line.separator");
        if(ls == null) {
            ls = "\n";
        }

        lineSeparator = ls;
    }
}
