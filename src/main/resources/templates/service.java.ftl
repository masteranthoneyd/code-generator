package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {
    <#list table.fields as field>
        <#if field.keyFlag>
            <#assign keyPropertyName="${field.propertyName}"/>
            <#assign keyPropertyCapitalName="${field.capitalName}"/>
            <#assign keyPropertyType="${field.propertyType}"/>
        </#if>
    </#list>
    /* !!!TUPLE_MARK_START!!! */
    ${entity} getBy${keyPropertyCapitalName}(${keyPropertyType} ${keyPropertyName});

    <#list table.indexInfos as indexInfo>
    <#if indexInfo.unique>${entity}<#else>List<${entity}></#if> getBy${indexInfo.indexMethodSuffix}(<#list indexInfo.fieldList as f>${f.propertyType} ${f.propertyName}<#if f_has_next> ,</#if></#list>);

    </#list>
    /* !!!TUPLE_MARK_END!!! */
}
</#if>
