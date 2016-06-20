# Mybatis Generator for Jeesite Style Plugin #


 This is a plugin of org.mybatis.generator
> http://mvnrepository.com/artifact/org.mybatis.generator/mybatis-generator-core

 for generate the DAO mapper and entity in thinkgem.jeesite code style.
> https://github.com/thinkgem/jeesite

 Yes, this is a plugin of another plugin orz...  

## Usage ##

###Step 1###

First of All, set the maven repository source in  `pom.xml` like this:
    
    <repository>
		<id>johnzhsk-mgjsp-repo</id>
		<url>https://raw.githubusercontent.com/johnzhsk/mgjsp-repo/mvn-repo/</url>
	</repository>

###Step 2###
If you import the Mybatis Generator Plugin into  `pom.xml`  directly, you can dependent MGJSP like this:

    
	<plugin>
		<groupId>org.mybatis.generator</groupId>
		<artifactId>mybatis-generator-maven-plugin</artifactId>
		<version>1.3.2</version>
		<configuration>
			...
		</configuration>
		<dependencies>
			<dependency>
				<groupId>mysql</groupId>
				...
			</dependency>
			**<dependency>
				<groupId>com.johnzhsk.mgjsp</groupId>
				<artifactId>mybatis-generator-jeesite-style-plugin</artifactId>
				<version>1.0</version>
			</dependency>**
		</dependencies>
	</plugin>
    
    
**Or** if you import the Mybatis Generator Core and wrote a main method to drive Mybatis Generator Plugin, you can dependent MGJSP like this:

    <dependencies>
    	...
		<dependency>
			<groupId>org.mybatis.generator</groupId>
			<artifactId>mybatis-generator-core</artifactId>
			<version>1.3.2</version>
		</dependency>

		**<dependency>
			<groupId>com.johnzhsk.mgjsp</groupId>
			<artifactId>mybatis-generator-jeesite-style-plugin</artifactId>
			<version>1.0</version>
		</dependency>**
    </dependencies>

###Step 3###
Now let's config your  `generatorConfig.xml` :

    <context id="context" targetRuntime="MyBatis3">

		<plugin type="com.johnzhsk.mgjsp.BaseCustomedPlugin"/>

		<commentGenerator>
			...
		</commentGenerator>
		...
    </context>

Attention to the consequence of `<plugin>` dom, it **should always be placed before the `<commentGenerator>` dom.**

Then switch the generation like this:

    <table tableName="${your_table_name}" domainObjectName="${domain_object_name}" enableCountByExample="false" enableDeleteByExample="false" enableSelectByExample="false"
               enableUpdateByExample="false" enableDeleteByPrimaryKey="true">
    	<columnOverride column="create_by" javaType="com.thinkgem.jeesite.modules.sys.entity.User" property="createBy" jdbcType="VARCHAR"/>
        <columnOverride column="update_by" javaType="com.thinkgem.jeesite.modules.sys.entity.User" property="updateBy" jdbcType="VARCHAR"/>
    </table>

And then **if you have database columns related to other object classes you've already defined** ,then you can config like this:

    <table tableName="${your_table_name}" domainObjectName="${domain_object_name}" enableCountByExample="false" enableDeleteByExample="false" enableSelectByExample="false"
               enableUpdateByExample="false" enableDeleteByPrimaryKey="true">
        <columnOverride column="create_by" javaType="com.thinkgem.jeesite.modules.sys.entity.User" property="createBy" jdbcType="VARCHAR"/>
        <columnOverride column="update_by" javaType="com.thinkgem.jeesite.modules.sys.entity.User" property="updateBy" jdbcType="VARCHAR"/>
        **<columnOverride column="${your_column_name}" javaType="com.thinkgem.jeesite.modules.${your_class_path}" property="${class_camel_name}" jdbcType="VARCHAR">
            <property name="tableName" value="${the_table_related}"/>
            <property name="tableAlias" value="b"/>
        </columnOverride>**
    </table>

Here are something you have to be noticed about join in tables:

1. All tables join in `id` column by default.

2. Be notice that the `<columnOverride column="create_by"/>` and `<columnOverride column="update_by"/>` is **necessary**!Unless your table doesn't have the column `create_by` and `update_by`,and then you have to remove the code in `xxxDAO.xml ` relate to these two columns. 


###Step 4###
Run the Mybatis Generator Plugin, free your hands :D.