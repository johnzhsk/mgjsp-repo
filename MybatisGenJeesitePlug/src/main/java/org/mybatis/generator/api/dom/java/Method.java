//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.mybatis.generator.api.dom.java;

import org.mybatis.generator.api.dom.OutputUtilities;

import java.util.*;

public class Method extends JavaElement {
    private List<String> bodyLines;
    private boolean constructor;
    private FullyQualifiedJavaType returnType;
    private String name;
    private List<Parameter> parameters;
    private List<FullyQualifiedJavaType> exceptions;
    private boolean isSynchronized;
    private boolean isNative;

    public Method() {
        this("bar");
    }

    public Method(String name) {
        this.bodyLines = new ArrayList();
        this.parameters = new ArrayList();
        this.exceptions = new ArrayList();
        this.name = name;
    }

    public Method(Method original) {
        super(original);
        this.bodyLines = new ArrayList();
        this.parameters = new ArrayList();
        this.exceptions = new ArrayList();
        this.bodyLines.addAll(original.bodyLines);
        this.constructor = original.constructor;
        this.exceptions.addAll(original.exceptions);
        this.name = original.name;
        this.parameters.addAll(original.parameters);
        this.returnType = original.returnType;
        this.isNative = original.isNative;
        this.isSynchronized = original.isSynchronized;
    }

    public List<String> getBodyLines() {
        return this.bodyLines;
    }

    public void addBodyLine(String line) {
        this.bodyLines.add(line);
    }

    public void addBodyLine(int index, String line) {
        this.bodyLines.add(index, line);
    }

    public void addBodyLines(Collection<String> lines) {
        this.bodyLines.addAll(lines);
    }

    public void addBodyLines(int index, Collection<String> lines) {
        this.bodyLines.addAll(index, lines);
    }

    public String getFormattedContent(int indentLevel, boolean interfaceMethod) {
        StringBuilder sb = new StringBuilder();
        this.addFormattedJavadoc(sb, indentLevel);
        this.addFormattedAnnotations(sb, indentLevel);
        OutputUtilities.javaIndent(sb, indentLevel);
        if(!interfaceMethod) {
            sb.append(this.getVisibility().getValue());
            if(this.isStatic()) {
                sb.append("static ");
            }

            if(this.isFinal()) {
                sb.append("final ");
            }

            if(this.isSynchronized()) {
                sb.append("synchronized ");
            }

            if(this.isNative()) {
                sb.append("native ");
            } else if(this.bodyLines.size() == 0) {
                sb.append("abstract ");
            }
        }

        if(!this.constructor) {
            if(this.getReturnType() == null) {
                sb.append("void");
            } else {
                sb.append(this.getReturnType().getShortName());
            }

            sb.append(' ');
        }

        sb.append(this.getName());
        sb.append('(');
        boolean comma = false;

        Iterator listIter;
        Parameter line;
        for(listIter = this.getParameters().iterator(); listIter.hasNext(); sb.append(line.getFormattedContent())) {
            line = (Parameter)listIter.next();
            if(comma) {
                sb.append(", ");
            } else {
                comma = true;
            }
        }

        sb.append(')');
        if(this.getExceptions().size() > 0) {
            sb.append(" throws ");
            comma = false;

            FullyQualifiedJavaType var10;
            for(listIter = this.getExceptions().iterator(); listIter.hasNext(); sb.append(var10.getShortName())) {
                var10 = (FullyQualifiedJavaType)listIter.next();
                if(comma) {
                    sb.append(", ");
                } else {
                    comma = true;
                }
            }
        }

        if(this.bodyLines.size() != 0 && !this.isNative()) {
            sb.append(" {");
            ++indentLevel;
            ListIterator var8 = this.bodyLines.listIterator();

            while(var8.hasNext()) {
                String var9 = (String)var8.next();
                if(var9.startsWith("}")) {
                    --indentLevel;
                }

                OutputUtilities.newLine(sb);
                OutputUtilities.javaIndent(sb, indentLevel);
                sb.append(var9);
                if(var9.endsWith("{") && !var9.startsWith("switch") || var9.endsWith(":")) {
                    ++indentLevel;
                }

                if(var9.startsWith("break")) {
                    if(var8.hasNext()) {
                        String nextLine = (String)var8.next();
                        if(nextLine.startsWith("}")) {
                            ++indentLevel;
                        }

                        var8.previous();
                    }

                    --indentLevel;
                }
            }

            --indentLevel;
            OutputUtilities.newLine(sb);
            OutputUtilities.javaIndent(sb, indentLevel);
            sb.append('}');
        } else {
            sb.append(';');
        }

        return sb.toString();
    }

    public boolean isConstructor() {
        return this.constructor;
    }

    public void setConstructor(boolean constructor) {
        this.constructor = constructor;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Parameter> getParameters() {
        return this.parameters;
    }

    public void addParameter(Parameter parameter) {
        this.parameters.add(parameter);
    }

    public void addParameter(int index, Parameter parameter) {
        this.parameters.add(index, parameter);
    }

    public void cleaerParameters() {
        this.parameters.clear();
    }

    public FullyQualifiedJavaType getReturnType() {
        return this.returnType;
    }

    public void setReturnType(FullyQualifiedJavaType returnType) {
        this.returnType = returnType;
    }

    public List<FullyQualifiedJavaType> getExceptions() {
        return this.exceptions;
    }

    public void addException(FullyQualifiedJavaType exception) {
        this.exceptions.add(exception);
    }

    public boolean isSynchronized() {
        return this.isSynchronized;
    }

    public void setSynchronized(boolean isSynchronized) {
        this.isSynchronized = isSynchronized;
    }

    public boolean isNative() {
        return this.isNative;
    }

    public void setNative(boolean isNative) {
        this.isNative = isNative;
    }
}
