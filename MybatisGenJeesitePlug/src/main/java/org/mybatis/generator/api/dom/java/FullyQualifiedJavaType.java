//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.mybatis.generator.api.dom.java;

import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class FullyQualifiedJavaType implements Comparable<FullyQualifiedJavaType> {
    private static final String JAVA_LANG = "java.lang";
    private static FullyQualifiedJavaType intInstance = null;
    private static FullyQualifiedJavaType stringInstance = null;
    private static FullyQualifiedJavaType booleanPrimitiveInstance = null;
    private static FullyQualifiedJavaType objectInstance = null;
    private static FullyQualifiedJavaType dateInstance = null;
    private static FullyQualifiedJavaType criteriaInstance = null;
    private static FullyQualifiedJavaType generatedCriteriaInstance = null;
    private String baseShortName;
    private String baseQualifiedName;
    private boolean explicitlyImported;
    private String packageName;
    private boolean primitive;
    private PrimitiveTypeWrapper primitiveTypeWrapper;
    private List<FullyQualifiedJavaType> typeArguments = new ArrayList();
    private boolean wildcardType;
    private boolean boundedWildcard;
    private boolean extendsBoundedWildcard;

    public FullyQualifiedJavaType(String fullTypeSpecification) {
        this.parse(fullTypeSpecification);
    }

    public void setBaseName(String baseShortName){
        this.baseQualifiedName.replace(this.baseShortName,baseShortName);
        this.baseShortName = baseShortName;
    }


    public boolean isExplicitlyImported() {
        return this.explicitlyImported;
    }

    public String getFullyQualifiedName() {
        StringBuilder sb = new StringBuilder();
        if(this.wildcardType) {
            sb.append('?');
            if(this.boundedWildcard) {
                if(this.extendsBoundedWildcard) {
                    sb.append(" extends ");
                } else {
                    sb.append(" super ");
                }

                sb.append(this.baseQualifiedName);
            }
        } else {
            sb.append(this.baseQualifiedName);
        }

        if(this.typeArguments.size() > 0) {
            boolean first = true;
            sb.append('<');

            FullyQualifiedJavaType fqjt;
            for(Iterator i$ = this.typeArguments.iterator(); i$.hasNext(); sb.append(fqjt.getFullyQualifiedName())) {
                fqjt = (FullyQualifiedJavaType)i$.next();
                if(first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
            }

            sb.append('>');
        }

        return sb.toString();
    }

    public List<String> getImportList() {
        ArrayList answer = new ArrayList();
        if(this.isExplicitlyImported()) {
            int i$ = this.baseShortName.indexOf(46);
            if(i$ == -1) {
                answer.add(this.baseQualifiedName);
            } else {
                StringBuilder fqjt = new StringBuilder();
                fqjt.append(this.packageName);
                fqjt.append('.');
                fqjt.append(this.baseShortName.substring(0, i$));
                answer.add(fqjt.toString());
            }
        }

        Iterator i$1 = this.typeArguments.iterator();

        while(i$1.hasNext()) {
            FullyQualifiedJavaType fqjt1 = (FullyQualifiedJavaType)i$1.next();
            answer.addAll(fqjt1.getImportList());
        }

        return answer;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public String getShortName() {
        StringBuilder sb = new StringBuilder();
        if(this.wildcardType) {
            sb.append('?');
            if(this.boundedWildcard) {
                if(this.extendsBoundedWildcard) {
                    sb.append(" extends ");
                } else {
                    sb.append(" super ");
                }

                sb.append(this.baseShortName);
            }
        } else {
            sb.append(this.baseShortName);
        }

        if(this.typeArguments.size() > 0) {
            boolean first = true;
            sb.append('<');

            FullyQualifiedJavaType fqjt;
            for(Iterator i$ = this.typeArguments.iterator(); i$.hasNext(); sb.append(fqjt.getShortName())) {
                fqjt = (FullyQualifiedJavaType)i$.next();
                if(first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
            }

            sb.append('>');
        }

        return sb.toString();
    }

    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if(!(obj instanceof FullyQualifiedJavaType)) {
            return false;
        } else {
            FullyQualifiedJavaType other = (FullyQualifiedJavaType)obj;
            return this.getFullyQualifiedName().equals(other.getFullyQualifiedName());
        }
    }

    public int hashCode() {
        return this.getFullyQualifiedName().hashCode();
    }

    public String toString() {
        return this.getFullyQualifiedName();
    }

    public boolean isPrimitive() {
        return this.primitive;
    }

    public PrimitiveTypeWrapper getPrimitiveTypeWrapper() {
        return this.primitiveTypeWrapper;
    }

    public static final FullyQualifiedJavaType getIntInstance() {
        if(intInstance == null) {
            intInstance = new FullyQualifiedJavaType("int");
        }

        return intInstance;
    }

    public static final FullyQualifiedJavaType getNewMapInstance() {
        return new FullyQualifiedJavaType("java.util.Map");
    }

    public static final FullyQualifiedJavaType getNewListInstance() {
        return new FullyQualifiedJavaType("java.util.List");
    }

    public static final FullyQualifiedJavaType getNewHashMapInstance() {
        return new FullyQualifiedJavaType("java.util.HashMap");
    }

    public static final FullyQualifiedJavaType getNewArrayListInstance() {
        return new FullyQualifiedJavaType("java.util.ArrayList");
    }

    public static final FullyQualifiedJavaType getNewIteratorInstance() {
        return new FullyQualifiedJavaType("java.util.Iterator");
    }

    public static final FullyQualifiedJavaType getStringInstance() {
        if(stringInstance == null) {
            stringInstance = new FullyQualifiedJavaType("java.lang.String");
        }

        return stringInstance;
    }

    public static final FullyQualifiedJavaType getBooleanPrimitiveInstance() {
        if(booleanPrimitiveInstance == null) {
            booleanPrimitiveInstance = new FullyQualifiedJavaType("boolean");
        }

        return booleanPrimitiveInstance;
    }

    public static final FullyQualifiedJavaType getObjectInstance() {
        if(objectInstance == null) {
            objectInstance = new FullyQualifiedJavaType("java.lang.Object");
        }

        return objectInstance;
    }

    public static final FullyQualifiedJavaType getDateInstance() {
        if(dateInstance == null) {
            dateInstance = new FullyQualifiedJavaType("java.util.Date");
        }

        return dateInstance;
    }

    public static final FullyQualifiedJavaType getCriteriaInstance() {
        if(criteriaInstance == null) {
            criteriaInstance = new FullyQualifiedJavaType("Criteria");
        }

        return criteriaInstance;
    }

    public static final FullyQualifiedJavaType getGeneratedCriteriaInstance() {
        if(generatedCriteriaInstance == null) {
            generatedCriteriaInstance = new FullyQualifiedJavaType("GeneratedCriteria");
        }

        return generatedCriteriaInstance;
    }

    public int compareTo(FullyQualifiedJavaType other) {
        return this.getFullyQualifiedName().compareTo(other.getFullyQualifiedName());
    }

    public void addTypeArgument(FullyQualifiedJavaType type) {
        this.typeArguments.add(type);
    }

    private void parse(String fullTypeSpecification) {
        String spec = fullTypeSpecification.trim();
        if(spec.startsWith("?")) {
            this.wildcardType = true;
            spec = spec.substring(1).trim();
            if(spec.startsWith("extends ")) {
                this.boundedWildcard = true;
                this.extendsBoundedWildcard = true;
                spec = spec.substring(8);
            } else if(spec.startsWith("super ")) {
                this.boundedWildcard = true;
                this.extendsBoundedWildcard = false;
                spec = spec.substring(6);
            } else {
                this.boundedWildcard = false;
            }

            this.parse(spec);
        } else {
            int index = fullTypeSpecification.indexOf(60);
            if(index == -1) {
                this.simpleParse(fullTypeSpecification);
            } else {
                this.simpleParse(fullTypeSpecification.substring(0, index));
                this.genericParse(fullTypeSpecification.substring(index));
            }
        }

    }

    private void simpleParse(String typeSpecification) {
        this.baseQualifiedName = typeSpecification.trim();
        if(this.baseQualifiedName.contains(".")) {
            this.packageName = getPackage(this.baseQualifiedName);
            this.baseShortName = this.baseQualifiedName.substring(this.packageName.length() + 1);
            int index = this.baseShortName.lastIndexOf(46);
            if(index != -1) {
                this.baseShortName = this.baseShortName.substring(index + 1);
            }

            if("java.lang".equals(this.packageName)) {
                this.explicitlyImported = false;
            } else {
                this.explicitlyImported = true;
            }
        } else {
            this.baseShortName = this.baseQualifiedName;
            this.explicitlyImported = false;
            this.packageName = "";
            if("byte".equals(this.baseQualifiedName)) {
                this.primitive = true;
                this.primitiveTypeWrapper = PrimitiveTypeWrapper.getByteInstance();
            } else if("short".equals(this.baseQualifiedName)) {
                this.primitive = true;
                this.primitiveTypeWrapper = PrimitiveTypeWrapper.getShortInstance();
            } else if("int".equals(this.baseQualifiedName)) {
                this.primitive = true;
                this.primitiveTypeWrapper = PrimitiveTypeWrapper.getIntegerInstance();
            } else if("long".equals(this.baseQualifiedName)) {
                this.primitive = true;
                this.primitiveTypeWrapper = PrimitiveTypeWrapper.getLongInstance();
            } else if("char".equals(this.baseQualifiedName)) {
                this.primitive = true;
                this.primitiveTypeWrapper = PrimitiveTypeWrapper.getCharacterInstance();
            } else if("float".equals(this.baseQualifiedName)) {
                this.primitive = true;
                this.primitiveTypeWrapper = PrimitiveTypeWrapper.getFloatInstance();
            } else if("double".equals(this.baseQualifiedName)) {
                this.primitive = true;
                this.primitiveTypeWrapper = PrimitiveTypeWrapper.getDoubleInstance();
            } else if("boolean".equals(this.baseQualifiedName)) {
                this.primitive = true;
                this.primitiveTypeWrapper = PrimitiveTypeWrapper.getBooleanInstance();
            } else {
                this.primitive = false;
                this.primitiveTypeWrapper = null;
            }
        }

    }

    private void genericParse(String genericSpecification) {
        int lastIndex = genericSpecification.lastIndexOf(62);
        if(lastIndex == -1) {
            throw new RuntimeException(Messages.getString("RuntimeError.22", genericSpecification));
        } else {
            String argumentString = genericSpecification.substring(1, lastIndex);
            StringTokenizer st = new StringTokenizer(argumentString, ",<>", true);
            int openCount = 0;
            StringBuilder sb = new StringBuilder();

            String finalType;
            while(st.hasMoreTokens()) {
                finalType = st.nextToken();
                if("<".equals(finalType)) {
                    sb.append(finalType);
                    ++openCount;
                } else if(">".equals(finalType)) {
                    sb.append(finalType);
                    --openCount;
                } else if(",".equals(finalType)) {
                    if(openCount == 0) {
                        this.typeArguments.add(new FullyQualifiedJavaType(sb.toString()));
                        sb.setLength(0);
                    } else {
                        sb.append(finalType);
                    }
                } else {
                    sb.append(finalType);
                }
            }

            if(openCount != 0) {
                throw new RuntimeException(Messages.getString("RuntimeError.22", genericSpecification));
            } else {
                finalType = sb.toString();
                if(StringUtility.stringHasValue(finalType)) {
                    this.typeArguments.add(new FullyQualifiedJavaType(finalType));
                }

            }
        }
    }

    private static String getPackage(String baseQualifiedName) {
        int index = baseQualifiedName.lastIndexOf(46);
        return baseQualifiedName.substring(0, index);
    }
}
