package com.mitrakoff.lasnotes;

import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.util.Arrays;

// for macOS: run with -XstartOnFirstThread JVM option
public class App {
    public static void main(String [] args) {
        String html = renderHtml();

        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Snippet 136");
        shell.setLayout(new FillLayout());
        Browser browser;
        try {
            browser = new Browser(shell, SWT.NONE);
        } catch (SWTError e) {
            System.out.println("Could not instantiate Browser: " + e.getMessage());
            display.dispose();
            return;
        }
        browser.setText(html);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }

    public static String renderHtml() {
        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        var s = """
                # Java: Simple Http client
                
                ```java
                import java.net.URI;
                import java.net.URLEncoder;
                import java.net.http.*;
                import java.nio.charset.StandardCharsets;
                
                public String makeHttpRequest(String url) throws Exception {
                    final var client = HttpClient.newHttpClient();
                    final var request = HttpRequest.newBuilder(URI.create(url)).build();
                    final var reply = client.send(request, HttpResponse.BodyHandlers.ofString());
                    client.close();
                
                    return reply.body();
                }
                ```
                Usage:
                ```java
                final var key = "hello world";
                final var keyEncoded = URLEncoder.encode(key, StandardCharsets.UTF_8).replaceAll("\\\\+", "%20");
                makeHttpRequest("http://mitrakoff.com:9090/lingo/key/en-GB/" + keyEncoded);
                ```
                
                # C# global hotkeys
                ```cs
                protected override bool ProcessCmdKey(ref Message message, Keys keys) {
                    switch (keys) {
                        case Keys.B | Keys.Control | Keys.Alt | Keys.Shift:
                            // ... Process Shift+Ctrl+Alt+B ...
                            return true; // signal that we've processed this key
                    }
                
                    return base.ProcessCmdKey(ref message, keys);
                }
                ```
                [Link](https://stackoverflow.com/a/14982579/2212849)
                
                # Login to Docker
                ```yml
                - name: registry
                  type: string
                  default: 'Docker-Artifactory-service-connection'
                ...
                steps:
                  - task: Docker@2
                    displayName: 'Docker login'
                    inputs:
                      containerRegistry: '${{ parameters.registry }}'
                      command: 'login'
                    condition: or(and(succeeded(), eq(variables['Build.SourceBranch'], 'refs/heads/main')), eq(variables['PrismaScan'],'true'), false)
                ```
                
                """;
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
