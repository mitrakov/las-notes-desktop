package com.mitrakoff.lasnotes;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

// for macOS: run with -XstartOnFirstThread JVM option
// https://highlightjs.org/demo
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
        final var browser = new Browser(shell, SWT.NONE);
        final var page = """
          <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/styles/stackoverflow-light.min.css">
          <script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/highlight.min.js"></script>
          <div id="dc"/>
        """;
        browser.setText(page);
        browser.setLayoutData (button2Data);

        // text
        Text text = new Text(shell, SWT.MULTI | SWT.BORDER);
        text.addModifyListener(e -> browser.execute(MdRenderer.makeJsFromMd(text.getText())));
        text.setLayoutData(button1Data);

        shell.pack ();
        shell.open ();
        while (!shell.isDisposed ()) {
            if (!display.readAndDispatch ()) display.sleep ();
        }
        display.dispose ();
    }
}
