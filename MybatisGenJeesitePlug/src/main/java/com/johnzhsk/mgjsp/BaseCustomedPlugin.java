package com.johnzhsk.mgjsp;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.config.ColumnOverride;

import java.util.List;

/**
 * Created by johnzhsk on 2016/6/3.
 */
public class BaseCustomedPlugin extends PluginAdapter {

    /** 是否将deleteByPrimaryKey转为delete并改为逻辑删除 */
    private static boolean REPLACE_DELETE_ELEMENT = true;
    /** 是否删除insertSelective的Mapper方法与SQL */
    private static boolean REMOVE_INSERT_SELECTIVE = true;
    /** 是否删除updateSelective的Mapper方法与SQL */
    private static boolean REMOVE_UPDATE_SELECTIVE = true;
    /** 是否将updateByPrimaryKey转为update */
    private static boolean REPLACE_UPDATE_ELEMENT = true;
    /** 是否将基础查询列更改为自定义jeesite风格 */
    private static boolean REPLACE_BASE_COLUMN_WITH_JEESITE_STYLE = true;
    /** 是否将文件名称更改为自定义jeesite风格 */
    private static boolean REPLACE_FILE_NAME_STYLE_WITH_JEESITE_STYLE = true;


    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * 初始化设定，可以在此预设定各种名称
     * @param introspectedTable
     */
    @Override
    public void initialized(IntrospectedTable introspectedTable){
        if (REPLACE_FILE_NAME_STYLE_WITH_JEESITE_STYLE){

            // 自定义sql Mapper文件名称
            introspectedTable.setMyBatis3XmlMapperFileName(introspectedTable.getMyBatis3XmlMapperFileName().replace("Mapper", "Dao"));
            // 自定义java Mapper文件名称
            introspectedTable.setMyBatis3JavaMapperType(introspectedTable.getMyBatis3JavaMapperType().replace("Mapper", "Dao"));
            // 自定义sql Mapper中的namespace
            introspectedTable.setMyBatis3FallbackSqlMapNamespace(introspectedTable.getMyBatis3FallbackSqlMapNamespace().replace("Mapper", "Dao"));
        }
        if (REPLACE_UPDATE_ELEMENT){

            // 自定义根据主键更新updateByPrimaryKey方法的名称与mapper的id
            introspectedTable.setUpdateByPrimaryKeyStatementId("update");
        }
        if (REPLACE_DELETE_ELEMENT){

            // 自定义根据主键删除deleteByPrimaryKey方法的名称与mapper的id
            introspectedTable.setDeleteByPrimaryKeyStatementId("delete");
        }
        if (REPLACE_BASE_COLUMN_WITH_JEESITE_STYLE){

            // 自定义BaseColumn mapper的id
            introspectedTable.setBaseColumnListId(getCustomedBaseColumnRefId(introspectedTable));
            // 自定义根据主键查询SelectByPrimaryKey方法的名称与mapper的id
            introspectedTable.setSelectByPrimaryKeyStatementId("findById");
        }
    }

    /**
     * 最终的XML Mapper生成
     * @param document
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        if (REPLACE_BASE_COLUMN_WITH_JEESITE_STYLE) {
            XmlElement parentElement = document.getRootElement();
            parentElement.addElement(1, generateXmlDataScopeFilterJoinsElement(introspectedTable));
            parentElement.addElement(2, generateXmlWhereClauseElement(introspectedTable));
            parentElement.addElement(3, generateXmlWhereClauseElement(introspectedTable, false));
            parentElement.addElement(generateXmlFindList(parentElement, introspectedTable));

        }
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    private Element generateXmlFindList(XmlElement parentElement,IntrospectedTable introspectedTable) {
        List<Element> elements =  parentElement.getElements();
        XmlElement findListElement = new XmlElement("");
        XmlElement findByIdElement = null;
        out:for (Element element: elements){
            if (element instanceof XmlElement){
                List<Attribute> attributes = ((XmlElement) element).getAttributes();
                for (Attribute attribute: attributes){
                    if (attribute.getValue().equals("findById")){
                        try {
                            findByIdElement =(XmlElement)((XmlElement) element).deepCopy();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break out;
                    }
                }
            }
        }


        if (findByIdElement!=null){
            findListElement.replaceXmlElement(findByIdElement);
            findListElement.setAttribute(new Attribute("id", "findList"));
            List<Element> findListChildElements = findListElement.getElements();
            for (Element element:findListChildElements){
                if (element instanceof TextElement){
                    if (((TextElement) element).getContent().contains("where")){
                        findListElement.removeChildElement(element);

                        XmlElement includeWhereClauseXmlElement = new XmlElement("include");
                        includeWhereClauseXmlElement.addAttribute(new Attribute("refid", "whereClause"));
                        findListElement.addElement(includeWhereClauseXmlElement);
                        findListElement.addElement(generateJeesiteStylePageSql());
                        // 到这里如果不作处理，会报java.util.ConcurrentModificationException，初步判断为由于循环体对象本身的元素增加变动导致循环异常。
                        break;
                    }
                }
            }
            return findListElement;
        }

        return new TextElement("");
    }

    private XmlElement generateJeesiteStylePageSql() {
        /**
         * 例：
         *  <choose>
         *      <when test="page !=null and page.orderBy != null and page.orderBy != ''">
         *          ORDER BY ${page.orderBy}
         *      </when>
         *      <otherwise>
         *          ORDER BY a.create_date DESC, a.update_date DESC
         *      </otherwise>
         *  </choose>
         */
        XmlElement chooseElement = new XmlElement("choose");

        XmlElement whenElement = new XmlElement("when");
        whenElement.addAttribute(new Attribute("test", "page !=null and page.orderBy != null and page.orderBy != ''"));
        whenElement.addElement(new TextElement("ORDER BY ${page.orderBy}"));

        XmlElement otherwiseElement = new XmlElement("otherwise");
        otherwiseElement.addElement(new TextElement("ORDER BY a.create_date DESC, a.update_date DESC"));

        chooseElement.addElement(whenElement);
        chooseElement.addElement(otherwiseElement);
        return chooseElement;


    }

    private Element generateXmlWhereClauseElement(IntrospectedTable introspectedTable) {
        return generateXmlWhereClauseElement(introspectedTable, true);
    }
    /**
     * 生成jeesite风格sql片段：WhereClause
     * @param introspectedTable
     * @param isBlur 是否模糊查询
     * @return
     */
    private Element generateXmlWhereClauseElement(IntrospectedTable introspectedTable,boolean isBlur) {
        XmlElement whereClauseElement = new XmlElement("sql");
        whereClauseElement.addAttribute(new Attribute("id", "whereClause" + (isBlur?"":"NotBlur")));

        XmlElement whereElement = new XmlElement("where");
        whereElement.addElement(new TextElement("a.del_flag = #{DEL_FLAG_NORMAL}"));

        List<ColumnOverride> columnOverrides = introspectedTable.getTableConfiguration().getColumnOverrides();
        List<IntrospectedColumn> introspectedColumns = introspectedTable.getBaseColumns();
        // 列复写配置(columnOverride)是否为空
        boolean isColumnOverridesNotEmpty = columnOverrides!=null && columnOverrides.size()!=0 && !columnOverrides.isEmpty();

        out:for (IntrospectedColumn introspectedColumn: introspectedColumns){
            String javaPropertyName = introspectedColumn.getJavaProperty();
            XmlElement isEmptyElement;

            if (isColumnOverridesNotEmpty) {
                // 轮询是否能查得到跟introspectedColumn.getActualColumnName()相同的列复写配置中的columnOverride.getColumnName()，
                // 查到了生成适配于java对象判空的xml DOM
                for (ColumnOverride columnOverride : columnOverrides) {
                    if (introspectedColumn.getActualColumnName().equals(columnOverride.getColumnName())){

                        isEmptyElement = generateIsEmptyElement(introspectedColumn, javaPropertyName, true, isBlur);
                        whereElement.addElement(isEmptyElement);
                        continue out;
                    }
                }

                // 在循环内没有被continue而执行到了这一步说明没有对应的列复写配置，生成适配于字符串的判空的xml DOM
                isEmptyElement = generateIsEmptyElement(introspectedColumn, javaPropertyName, false, isBlur);
                whereElement.addElement(isEmptyElement);
                continue out;

            } else {
                // 没有列复写配置，生成适配于字符串的判空的xml DOM
                isEmptyElement = generateIsEmptyElement(introspectedColumn, javaPropertyName, false, isBlur);
                whereElement.addElement(isEmptyElement);
                continue out;
            }
        }

        // 生成jeesite风格权限控制动态参数
        whereElement.addElement(new TextElement(
                "${sqlMap.dsf}"
        ));

        whereClauseElement.addElement(whereElement);
        return whereClauseElement;
    }

    private XmlElement generateIsEmptyElement(IntrospectedColumn introspectedColumn, String javaPropertyName,
                                              boolean isColumnOverrided, boolean isBlur) {
        /**
         * 例:
         *      列被复写时：
         * <if test="createBy != null and createBy.id != null and createBy.id != ''">
         *      AND a.create_by  = #{createBy.id}
         * </if>
         *      列未被复写时：
         * <if test="isBack != null and isBack != ''">
         *      AND a.is_back  = #{isBack}
         * </if>
         *      列未被复写，是模糊查询，并且数据库列类型为VARCHAR时：
         * <if test="stockNumber != null and stockNumber != ''">
         *      AND a.stock_number LIKE
         *      <if test="dbName == 'oracle'">'%'||#{stockNumber}||'%'</if>
         *      <if test="dbName == 'mysql'">CONCAT('%', #{stockNumber}, '%')</if>
         * </if>
         */
        String nullCondition;
        if (isColumnOverrided){
            nullCondition = javaPropertyName + " != null and " + javaPropertyName + ".id != null and "
                    + javaPropertyName + ".id != ''";
        } else {
            nullCondition = javaPropertyName + " != null and " + javaPropertyName + " != ''";
        }
        XmlElement isEmptyElement = new XmlElement("if");
        Attribute attribute = new Attribute("test",nullCondition);
        isEmptyElement.addAttribute(attribute);

        if (!isBlur || !introspectedColumn.getJdbcTypeName().equals("VARCHAR")){
            isEmptyElement.addElement(new TextElement(
                    "and a." + introspectedColumn.getActualColumnName() + " = #{" + javaPropertyName + (isColumnOverrided?".id":"") + "}"
            ));
        } else {
            isEmptyElement.addElement(new TextElement(
                    "and a." + introspectedColumn.getActualColumnName() + " like"
            ));

            XmlElement isDbNameOracleElement = new XmlElement("if");
            Attribute isDbNameOracleAttributeTest = new Attribute("test", "dbName == 'oracle'");
            isDbNameOracleElement.addAttribute(isDbNameOracleAttributeTest);
            isDbNameOracleElement.addElement(new TextElement(
                    "'%'||#{" + javaPropertyName + "}||'%'"
            ));

            isEmptyElement.addElement(isDbNameOracleElement);

            XmlElement isDbNameMysqlElement = new XmlElement("if");
            Attribute isDbNameMysqlAttributeTest = new Attribute("test", "dbName == 'mysql'");
            isDbNameMysqlElement.addAttribute(isDbNameMysqlAttributeTest);
            isDbNameMysqlElement.addElement(new TextElement(
                    "CONCAT('%', #{" + javaPropertyName + "}, '%')"
            ));

            isEmptyElement.addElement(isDbNameMysqlElement);
        }

        return isEmptyElement;
    }

    /**
     * 生成jeesite风格sql片段：DataScopeFilter
     * @param introspectedTable
     * @return
     */
    private Element generateXmlDataScopeFilterJoinsElement(IntrospectedTable introspectedTable) {
        XmlElement dataScopeFilterJoinsElement = new XmlElement("sql");
        dataScopeFilterJoinsElement.addAttribute(new Attribute("id", "dataScopeFilterJoins"));
        dataScopeFilterJoinsElement.addElement(new TextElement(
                "LEFT JOIN sys_user u ON u.id = a.create_by "
        ));
        dataScopeFilterJoinsElement.addElement(new TextElement(
                "JOIN sys_office o ON o.id =  u.office_id "
        ));

        char autoTableAlias = 'a';
        // 获取配置文件中的列复写配置，以生成JOIN
        List<ColumnOverride> columnOverrides = introspectedTable.getTableConfiguration().getColumnOverrides();
        if (columnOverrides!=null && columnOverrides.size()!=0 && !columnOverrides.isEmpty()){
            for (ColumnOverride columnOverride:columnOverrides){

                // 过滤掉两个特殊的
                if (columnOverride.getColumnName().equals("create_by")||
                        columnOverride.getColumnName().equals("update_by")) continue;

                // 获取配置文件中对复写此列的对象的绑定表名。如果无法获取则给予提示。
                String tableName = columnOverride.getProperty("tableName")!=null||columnOverride.getProperty("tableName").equals("")?
                        columnOverride.getProperty("tableName"):
                        "<!-- TODO Very Important!Please replace the content in this angle brackets with corresponding table name-->";

                // 获取配置的上述表名的别名。如果无法获取则自动生成
                String tableAlias = columnOverride.getProperty("tableAlias")!=null||columnOverride.getProperty("tableAlias").equals("")?
                        columnOverride.getProperty("tableAlias"):
                        // 遇到特殊的预配置的o和u进行跳过处理
                        String.valueOf(autoTableAlias + 1 == 'o' || autoTableAlias + 1 =='u'?++autoTableAlias+1:++autoTableAlias);


                dataScopeFilterJoinsElement.addElement(new TextElement(
                        "LEFT JOIN " + tableName +" "
                                + tableAlias +" " + " ON " + tableAlias+ ".id =  a."+ columnOverride.getColumnName()
                ));
            }
        }

        return dataScopeFilterJoinsElement;
    }

    /**
     * 最终的java Mapper方法生成
     * @param interfaze
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (REPLACE_BASE_COLUMN_WITH_JEESITE_STYLE) {
            interfaze.addMethod(generateFindListMethod(introspectedTable));
            interfaze.addImportedType(new FullyQualifiedJavaType("java.util.List"));
        }
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    private Method generateFindListMethod(IntrospectedTable introspectedTable) {
        Method findListMethod = new Method("findList");
        findListMethod.setConstructor(false);

        FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType("List<" + introspectedTable.getFullyQualifiedTable().getDomainObjectName()+">");
        findListMethod.setReturnType(fullyQualifiedJavaType);
        findListMethod.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "record"));
        findListMethod.setVisibility(JavaVisibility.PUBLIC);
        findListMethod.setStatic(false);
        findListMethod.setFinal(false);

        return findListMethod;
    }

    /**********************************************查询***************************************************************/
    @Override
     public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable){
        if (REPLACE_BASE_COLUMN_WITH_JEESITE_STYLE) {
//            interfaze.addMethod(generateFindListMethod(introspectedTable));
        }
        return true;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable){
        if (REPLACE_BASE_COLUMN_WITH_JEESITE_STYLE) {
//            topLevelClass.addMethod(generateFindListMethod(introspectedTable));
        }
        return true;
    }


    /**
     * 根据主键查询 sqlMapper生成
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        if (REPLACE_BASE_COLUMN_WITH_JEESITE_STYLE) {
            element.replaceXmlElement(replaceXmlElementByFindByIdElement(element, introspectedTable));
        }
        return true;
    }
    /**********************************************基础片段***************************************************************/
    /**
     * ResultMap sqlMapper生成
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapResultMapWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        if (REPLACE_BASE_COLUMN_WITH_JEESITE_STYLE) return false;
        return true;
    }

    /**
     * ResultMap sqlMapper生成
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapResultMapWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        if (REPLACE_BASE_COLUMN_WITH_JEESITE_STYLE) return false;
        return true;
    }

    /**
     * 基础查询列 sql片段生成
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapBaseColumnListElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        if (REPLACE_BASE_COLUMN_WITH_JEESITE_STYLE) {
            element.replaceXmlElement(replaceXmlByCustomedBaseColumnElement(element, introspectedTable));
        }
        return true;
    }

    /**********************************************更新****************************************************************/
    /**
     * 选择性更新 java方法生成
     * @param method
     * @param interfaze
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        if (REMOVE_UPDATE_SELECTIVE) return false;
        return true;
    }

    /**
     * 选择性更新 sqlMap
     * @param element
     * @param introspectedTable
     * @return
     */
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        if (REMOVE_UPDATE_SELECTIVE) return false;
        return true;
    }

    /**********************************************插入****************************************************************/
    /**
     * 选择性插入 sqlMap
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        if (REMOVE_INSERT_SELECTIVE) return false;
        return true;
    }

    /**
     * 选择性插入 java方法生成
     * @param method
     * @param interfaze
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        if (REMOVE_INSERT_SELECTIVE) return false;
        return true;
    }

    /**********************************************删除****************************************************************/
    /**
     * 删除的SQL生成
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        if (REPLACE_DELETE_ELEMENT)  element.replaceXmlElement(generateXmlDeleteElement(introspectedTable));
        return true;
    }

    /**
     * 生成delete XmlElement
     * @param introspectedTable
     */
    private XmlElement generateXmlDeleteElement(IntrospectedTable introspectedTable) {
        String tableName = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();//数据库表名

        XmlElement element = new XmlElement("update");
        element.addAttribute(new Attribute("id", "delete"));

        StringBuilder tabString = new StringBuilder("");
        OutputUtilities.xmlIndent(tabString, 2);
        element.addElement(
                new TextElement(
                        "UPDATE " + tableName + " SET \n" +
                                tabString.toString()+"del_flag = #{DEL_FLAG_DELETE} \n" +
                                tabString.toString()+"where id = #{id}"
                ));
        return element;
    }

    /**
     * 生成baseColums XmlElement
     * @param element
     * @param introspectedTable
     * @return
     */
    private XmlElement replaceXmlByCustomedBaseColumnElement(XmlElement element, IntrospectedTable introspectedTable) {
        element.clearChildElements();

        List<IntrospectedColumn> introspectedColumns = introspectedTable.getAllColumns();
        for (int i = 0 ; i < introspectedColumns.size() ; i ++){
            String javaProperty = introspectedColumns.get(i).getJavaProperty();
            TextElement te = new TextElement("a." + introspectedColumns.get(i).getActualColumnName() + " as \""
                    + javaProperty
                    + (javaProperty.equals("createBy")||javaProperty.equals("updateBy")?".id":"")
                    + "\"" + (i==introspectedColumns.size()-1?"":","));
            element.addElement(te);
        }

        return element;
    }



    private XmlElement replaceXmlElementByFindByIdElement(XmlElement element, IntrospectedTable introspectedTable) {
        Attribute attribute = new Attribute("id","findById");
        element.setAttribute(attribute);
        attribute = new Attribute("resultType",introspectedTable.getBaseRecordType());
        element.setAttribute(attribute);
        element.removeAttributeByName("resultMap");

        List<Element> elements = element.getElements();
        for (int i = 0 ; i < elements.size() ; i ++){

            Element element1 = elements.get(i);
            if (element1 instanceof XmlElement){
                // 查找是否有refId="Base_Column_List"的Attribution的XmlElement，如果有，把refId改为jeesite风格
                for (Attribute attribute2:((XmlElement) element1).getAttributes()){
                    if (attribute2.getName().equals("Base_Column_List")){
                        Attribute attribute1 = new Attribute("refid",getCustomedBaseColumnRefId(introspectedTable));
                        ((XmlElement) element1).setAttribute(attribute1);
                    }
                    break;
                }

            } else if (element1 instanceof TextElement){
                // 查找是否有包含"from"字符串的TextElement,并取出位置序号，在其下方添加jeesite风格的include dataScopeFilterJoins sql片段
                if (REPLACE_BASE_COLUMN_WITH_JEESITE_STYLE){
                    if (((TextElement) element1).getContent().contains("from")){
                        XmlElement dataScopeFilterJoinsXmlElement = new XmlElement("include");
                        dataScopeFilterJoinsXmlElement.addAttribute(new Attribute("refid","dataScopeFilterJoins"));
                        element.addElement(i+1,dataScopeFilterJoinsXmlElement);
                    }
                }
            }
        }

        return element;
    }


    /**
     * 获取sqlxml中jeesite风格自定义baseColumn dom的ID
     * 例如 labelStorageInColumns
     * @param introspectedTable
     * @return
     */
    private String getCustomedBaseColumnRefId(IntrospectedTable introspectedTable) {
        String domainObjectName = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        domainObjectName = domainObjectName.replaceFirst(String.valueOf(domainObjectName.charAt(0)), String.valueOf(domainObjectName.charAt(0)).toLowerCase());
        domainObjectName = domainObjectName+"Columns";
        return domainObjectName;
    }
}
