//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.mybatis.generator.api.dom.xml;

import org.mybatis.generator.api.dom.OutputUtilities;

import java.io.Serializable;

public class TextElement extends Element implements Serializable{

    private static final long serialVersionUID = 7991552226614088001L;

    private String content;

    public TextElement(String content) {
        this.content = content;
    }

    public String getFormattedContent(int indentLevel) {
        StringBuilder sb = new StringBuilder();
        OutputUtilities.xmlIndent(sb, indentLevel);
        sb.append(this.content);
        return sb.toString();
    }

    public String getContent() {
        return this.content;
    }
}
