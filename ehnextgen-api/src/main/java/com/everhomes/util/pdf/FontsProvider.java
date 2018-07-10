package com.everhomes.util.pdf;

import com.itextpdf.text.Font;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;

public class FontsProvider extends XMLWorkerFontProvider{

    public FontsProvider(){
        super(null, null);
    }

    @Override
    public Font getFont(final String fontname, String encoding, float size, final int style) {
        String fntname = fontname;
        if (fntname == null) {
            fntname = "ttf/SIMYOU.TTF";
        }
        if (size == 0) {
            size = 4;
        }
        return super.getFont(fntname, encoding, size, style);
    }

}
