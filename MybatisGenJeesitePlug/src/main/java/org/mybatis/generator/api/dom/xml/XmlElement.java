//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.mybatis.generator.api.dom.xml;

import org.mybatis.generator.api.dom.OutputUtilities;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XmlElement extends Element implements Serializable{

    // serialVersionUID
    // 如果你的对象序列化后存到硬盘上面后，可是后来你却更改了类的field(增加或减少或改名)，当你反序列化时，就会出现Exception的，这样就会造成不兼容性的问题。
    // 但当serialVersionUID相同时，它就会将不一样的field以type的缺省值赋值(如int型的是0,String型的是null等)，这个可以避开不兼容性的问题。所以最好给serialVersionUID赋值
    private static final long serialVersionUID = 7991552226614088458L;

    private List<Attribute> attributes = new ArrayList();
    private List<Element> elements;
    private String name;

    public XmlElement(String name) {
        this.elements = new ArrayList();
        this.name = name;
    }

    public XmlElement(XmlElement original) {
        this.attributes.addAll(original.attributes);
        this.elements = new ArrayList();
        this.elements.addAll(original.elements);
        this.name = original.name;
    }

    public List<Attribute> getAttributes() {
        return this.attributes;
    }

    public void addAttribute(Attribute attribute) {
        this.attributes.add(attribute);
    }

    public void setAttribute(Attribute attribute) {
        for (Attribute attribute1:this.attributes){
            if (attribute1.getName().equals(attribute.getName())){
                this.attributes.remove(attribute1);
                break;
            }
        }
        this.attributes.add(attribute);
    }

    public void removeAttributeByName(String attributeName) {
        for (Attribute attribute1:this.attributes){
            if (attribute1.getName().equals(attributeName)){
                this.attributes.remove(attribute1);
                break;
            }
        }
    }

    public List<Element> getElements() {
        return this.elements;
    }

    public void addElement(Element element) {
        this.elements.add(element);
    }

    public void addElement(int index, Element element) {
        this.elements.add(index, element);
    }

    public void removeChildElement (Element element) {
        this.elements.remove(element);
    }

    public void clearChildElements () {
        this.elements.clear();
    }

    public void replaceXmlElement(XmlElement element) {
        this.attributes = element.getAttributes();
        this.name = element.getName();
        this.elements = element.getElements();
    }

    public String getName() {
        return this.name;
    }

    public String getFormattedContent(int indentLevel) {
        StringBuilder sb = new StringBuilder();
        OutputUtilities.xmlIndent(sb, indentLevel);
        sb.append('<');
        sb.append(this.name);
        Iterator i$ = this.attributes.iterator();

        while(i$.hasNext()) {
            Attribute element = (Attribute)i$.next();
            sb.append(' ');
            sb.append(element.getFormattedContent());
        }

        if(this.elements.size() > 0) {
            sb.append(" >");
            i$ = this.elements.iterator();

            while(i$.hasNext()) {
                Element element1 = (Element)i$.next();
                OutputUtilities.newLine(sb);
                sb.append(element1.getFormattedContent(indentLevel + 1));
            }

            OutputUtilities.newLine(sb);
            OutputUtilities.xmlIndent(sb, indentLevel);
            sb.append("</");
            sb.append(this.name);
            sb.append('>');
        } else {
            sb.append(" />");
        }

        return sb.toString();
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object deepCopy() throws Exception
    {
        // 将该对象序列化成流,因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        ObjectOutputStream oos = new ObjectOutputStream(bos);

        oos.writeObject(this);

        // 将流序列化成对象
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

        ObjectInputStream ois = new ObjectInputStream(bis);

        return ois.readObject();
    }
}
