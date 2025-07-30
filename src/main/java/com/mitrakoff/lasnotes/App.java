package com.mitrakoff.lasnotes;

import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import java.util.Arrays;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

// for macOS: run with -XstartOnFirstThread JVM option
public class App {
    public static void main(String [] args) {
        Display display = new Display ();
        final Shell shell = new Shell (display);
        shell.setText("Las Notes");
        final Sash sash = new Sash (shell, SWT.VERTICAL);

        final FormLayout form = new FormLayout ();
        shell.setLayout (form);

        final int limit = 20, percent = 50;
        final FormData sashData = new FormData ();
        sashData.left = new FormAttachment (percent, 0);
        sashData.top = new FormAttachment (0, 0);
        sashData.bottom = new FormAttachment (300, 0);
        sash.setLayoutData (sashData);
        sash.addListener (SWT.Selection, e -> {
            Rectangle sashRect = sash.getBounds ();
            Rectangle shellRect = shell.getClientArea ();
            int right = shellRect.width - sashRect.width - limit;
            e.x = Math.max (Math.min (e.x, right), limit);
            if (e.x != sashRect.x)  {
                sashData.left = new FormAttachment (0, e.x);
                shell.layout ();
            }
        });

        FormData button1Data = new FormData ();
        button1Data.left = new FormAttachment (0, 0);
        button1Data.right = new FormAttachment (sash, 0);
        button1Data.top = new FormAttachment (0, 0);
        button1Data.bottom = new FormAttachment (100, 0);


        FormData button2Data = new FormData ();
        button2Data.left = new FormAttachment (sash, 0);
        button2Data.right = new FormAttachment (100, 0);
        button2Data.top = new FormAttachment (0, 0);
        button2Data.bottom = new FormAttachment(100, 0);

        // browser
        Browser browser;
        try {
            browser = new Browser(shell, SWT.NONE);
        } catch (SWTError e) {
            System.out.println("Could not instantiate Browser: " + e.getMessage());
            display.dispose();
            return;
        }
        browser.setLayoutData (button2Data);

        // text-multiline
        Text text = new Text(shell, SWT.MULTI | SWT.BORDER);
        text.addModifyListener(e -> browser.setText(renderHtml(text.getText())));
        text.setLayoutData(button1Data);

        shell.pack ();
        shell.open ();
        while (!shell.isDisposed ()) {
            if (!display.readAndDispatch ()) display.sleep ();
        }
        display.dispose ();
    }

    public static String renderHtml(String s) {
        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        final var html = renderer.render(parser.parse(s));

        // https://highlightjs.org/demo
        final var highlightJs = """
          <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/styles/stackoverflow-light.min.css">
          <script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/highlight.min.js"></script>
          <script>
            document.addEventListener('DOMContentLoaded', (event) => {
              document.querySelectorAll('pre code').forEach((block) => {
                hljs.highlightElement(block);
              });
            });
          </script>
        """;
        return highlightJs + html;
    }
}
