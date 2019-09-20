/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.yangbingdong.generator.engine;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.yangbingdong.generator.config.ConstVal;
import com.yangbingdong.generator.config.builder.ConfigBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import static com.yangbingdong.generator.Replacer.process;
import static com.yangbingdong.generator.config.ConstVal.UTF_8;
import static org.apache.commons.io.FileUtils.writeLines;
import static org.apache.commons.io.IOUtils.readLines;

/**
 * Freemarker 模板引擎实现文件输出
 *
 * @author nieqiurong
 * @since 2018-01-11
 */
public class FreemarkerTemplateEngine extends AbstractTemplateEngine {

    private Configuration configuration;

    @Override
    public FreemarkerTemplateEngine init(ConfigBuilder configBuilder) {
        super.init(configBuilder);
        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding(ConstVal.UTF8);
        configuration.setClassForTemplateLoading(FreemarkerTemplateEngine.class, StringPool.SLASH);
        return this;
    }


    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile, boolean isCreate) throws Exception {
        Template template = configuration.getTemplate(templatePath);
        if (isCreate) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
                template.process(objectMap, new OutputStreamWriter(fileOutputStream, ConstVal.UTF8));
            }
        } else {
            File file = new File(outputFile);
            List<String> oldLines = FileUtils.readLines(file);
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(2048);
                 OutputStreamWriter out = new OutputStreamWriter(byteArrayOutputStream, ConstVal.UTF8)) {
                template.process(objectMap, out);
                ByteArrayInputStream swapStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                List<String> newLines = readLines(swapStream);
                List<String> replacedOldLines = process(oldLines, newLines);
                writeLines(file, UTF_8, replacedOldLines, false);
            }
        }

        logger.debug("模板:" + templatePath + ";  文件:" + outputFile);
    }


    @Override
    public String templateFilePath(String filePath) {
        return filePath + ".ftl";
    }
}
