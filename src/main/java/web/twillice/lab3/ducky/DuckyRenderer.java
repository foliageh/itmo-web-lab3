package web.twillice.lab3.ducky;

import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;
import jakarta.faces.render.Renderer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FacesRenderer(componentFamily = Ducky.COMPONENT_FAMILY, rendererType = DuckyRenderer.RENDERER_TYPE)
public class DuckyRenderer extends Renderer<Ducky> {
    public static final String RENDERER_TYPE = "twillice.Ducky";

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeBegin(FacesContext context, Ducky component) throws IOException {
        context.getResponseWriter().startElement("div", component);
        context.getResponseWriter().writeAttribute("id", component.getId(), null);
    }

    @Override
    public void encodeChildren(FacesContext context, Ducky component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        StringWriter output = new StringWriter();
        context.setResponseWriter(writer.cloneWithWriter(output));
        try {
            super.encodeChildren(context, component);
        } finally {
            context.setResponseWriter(writer);
        }
        String content = output.toString();
        if (component.isDucksShown()) {
            Matcher matcher = Pattern.compile("\\bduck\\b", Pattern.CASE_INSENSITIVE).matcher(content);
            if (matcher.results().count() >= component.getMinValue()) {
                content = matcher.replaceAll("<img height=20 src=\"https://www.icegif.com/wp-content/uploads/2023/04/icegif-1432.gif\">");
            }
        }
        writer.write(content);
    }

    @Override
    public void encodeEnd(FacesContext context, Ducky component) throws IOException {
        context.getResponseWriter().endElement("div");
    }
}
