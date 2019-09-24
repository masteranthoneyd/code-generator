package com.yangbingdong.generator.config.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ybd
 * @date 2019/9/20
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class IndexInfo {
    private String indexName;
    private boolean unique;
    private List<TableField> fieldList = new ArrayList<>();

    private int indexOrder = 0;

    private String indexMethodSuffix;

    public IndexInfo addFieldInfo(TableField fieldInfo) {
        fieldInfo.setIndexOrder(indexOrder++)
                 .setIndex(true)
                 .setUnique(unique)
                 .setIndexName(indexName);
        fieldList.add(fieldInfo);
        return this;
    }

    public IndexInfo(String indexName, boolean unique) {
        this.indexName = indexName;
        this.unique = unique;
    }

    public String getIndexMethodSuffix() {
        if (indexMethodSuffix != null) {
            return indexMethodSuffix;
        }
        indexMethodSuffix = fieldList.stream()
                                     .map(TableField::getCapitalName)
                                     .collect(Collectors.joining("And"));
        return indexMethodSuffix;
    }
}
