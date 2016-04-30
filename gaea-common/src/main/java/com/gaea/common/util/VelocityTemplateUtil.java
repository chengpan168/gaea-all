package com.gaea.common.util;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.gaea.common.constant.Constant;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * Velocity模板引擎工具类<br>
 * 从classpath 下读取模板文件
 * 
 */
public class VelocityTemplateUtil {

    static {
        Properties properties = new Properties();

        properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        properties.setProperty(Velocity.ENCODING_DEFAULT, Constant.DEFAULT_CHARSET);
        properties.setProperty(Velocity.INPUT_ENCODING, Constant.DEFAULT_CHARSET);
        properties.setProperty(Velocity.OUTPUT_ENCODING, Constant.DEFAULT_CHARSET);
        Velocity.init(properties);
        Velocity.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, true); //使用缓存
    }

    /**
     * 获得指定模板填充后的内容，使用默认引擎
     * @param templateFileName 模板文件
     * @param context 上下文（变量值的容器）
     * @return 模板和内容匹配后的内容
     */
    public static String getContent(String templateFileName, VelocityContext context) {
        final StringWriter writer = new StringWriter(); //StringWriter不需要关闭
        toWriter(templateFileName, context, writer);
        return writer.toString();
    }

    /**
     * 生成内容写入流<br>
     * 会自动关闭Writer
     * @param templateFileName 模板文件名
     * @param context 上下文
     * @param writer 流
     */
    public static void toWriter(String templateFileName, VelocityContext context, Writer writer) {
        final Template template = Velocity.getTemplate(templateFileName);
        template.merge(context, writer);
    }

    /**
     * 融合模板和内容
     * @param templateContent 模板的内容字符串
     * @param context 上下文
     * @return 模板和内容匹配后的内容
     */
    public static String merge(String templateContent, VelocityContext context) {
        final StringWriter writer = new StringWriter();
        try {
            Velocity.evaluate(context, writer, UUID.randomUUID().toString(), templateContent);
        } catch (Exception e) {
            throw new UtilException(e);
        }
        return writer.toString();
    }

    /**
     * 将Request中的数据转换为模板引擎
     * @param request 请求对象
     * @return 模板引擎
     */
    public static VelocityContext parseRequest(HttpServletRequest request) {
        VelocityContext context = new VelocityContext();

        final Enumeration<String> attrs = request.getAttributeNames();
        String attrName = null;
        while (attrs.hasMoreElements()) {
            attrName = attrs.nextElement();
            context.put(attrName, request.getAttribute(attrName));
        }

        return context;
    }

}
