//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.mybatis.generator.api.dom.xml;

import java.io.Serializable;

public class Attribute implements Serializable{

    private static final long serialVersionUID = 7991552226614088000L;

    private String name;
    private String value;

    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFormattedContent() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append("=\"");
        sb.append(this.value);
        sb.append('\"');
        return sb.toString();
    }
}
