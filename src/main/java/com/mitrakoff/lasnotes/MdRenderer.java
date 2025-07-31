package com.mitrakoff.lasnotes;

import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import java.util.Arrays;

public class MdRenderer {
    private static final MutableDataSet options = new MutableDataSet() {{
        set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));
    }};
    private static final Parser parser = Parser.builder(options).build();
    private static final HtmlRenderer renderer = HtmlRenderer.builder(options).build();

    public static String makeJsFromMd(String md) {
        var html = renderer.render(parser.parse(md)).replace("`", "\\`").replace("\\", "\\\\");
        return String.format("document.getElementById('dc').innerHTML=`%s`; document.querySelectorAll('pre code').forEach((e)=>{hljs.highlightElement(e);});", html);
    }
}
