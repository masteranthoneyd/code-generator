package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {
    <#list table.fields as field>
        <#if field.keyFlag>
            <#assign keyPropertyName="${field.propertyName}"/>
            <#assign keyPropertyCapitalName="${field.capitalName}"/>
            <#assign keyPropertyType="${field.propertyType}"/>
        </#if>
    </#list>
    /* !!!TUPLE_MARK_START!!! */

    @Override
    public ${entity} getBy${keyPropertyCapitalName}(${keyPropertyType} ${keyPropertyName}) {
        return redisoper.getByKey(() -> baseMapper.selectById(${keyPropertyName}), ${keyPropertyName});
    }

    <#list table.indexInfos as indexInfo>
        <#if indexInfo.unique>
    @Override
    public ${entity} getBy${indexInfo.indexMethodSuffix}(<#list indexInfo.fieldList as f>${f.propertyType} ${f.propertyName}<#if f_has_next> ,</#if></#list>) {
        return redisoper.getByUniqueIndex(() -> getOne(new QueryWrapper<${entity}>()<#list indexInfo.fieldList as f>.eq(${entity}.${f.name?upper_case}, ${f.propertyName})</#list>), ${entity}.${indexInfo.indexName?upper_case}, <#list indexInfo.fieldList as f>${f.propertyName}<#if f_has_next> ,</#if></#list>);
    }
        <#else>
    @Override
    public List<${entity}> getBy${indexInfo.indexMethodSuffix}(<#list indexInfo.fieldList as f>${f.propertyType} ${f.propertyName}<#if f_has_next> ,</#if></#list>) {
        return redisoper.getByNormalIndex(() -> list(new QueryWrapper<${entity}>()<#list indexInfo.fieldList as f>.eq(${entity}.${f.name?upper_case}, ${f.propertyName})</#list>), ${entity}.${indexInfo.indexName?upper_case}, <#list indexInfo.fieldList as f>${f.propertyName}<#if f_has_next> ,</#if></#list>);
    }
        </#if>

    </#list>
    /* !!!TUPLE_MARK_END!!! */
}
</#if>
