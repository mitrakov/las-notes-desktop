package com.mitrakoff.lasnotes;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

// for macOS: run with -XstartOnFirstThread JVM option
// https://highlightjs.org/demo
public class App {
    public static void main(String [] args) {
        final var display = new Display();
        final var shell = new Shell(display);
        final var sash = new SashForm (shell, SWT.HORIZONTAL);
        final var text = new Text(sash, SWT.MULTI | SWT.BORDER);
        final var browser = new Browser(sash, SWT.BORDER);

        shell.setText("Las Notes");
        shell.setLayout (new FillLayout());
        text.addModifyListener(e -> browser.execute(MdRenderer.makeJsFromMd(text.getText())));
        browser.setText("""
          <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/styles/stackoverflow-light.min.css">
          <script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/highlight.min.js"></script>
          <div id="dc"/>
        """);

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}
