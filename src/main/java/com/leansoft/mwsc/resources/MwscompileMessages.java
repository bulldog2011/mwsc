package com.leansoft.mwsc.resources;

import com.sun.xml.ws.util.localization.Localizable;
import com.sun.xml.ws.util.localization.LocalizableMessageFactory;
import com.sun.xml.ws.util.localization.Localizer;

public class MwscompileMessages {
	
    private final static LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("com.leansoft.mwsc.resources.mwscompile");
    private final static Localizer localizer = new Localizer();
    
    public static Localizable localizableMWSC_HELP(Object arg0) {
        return messageFactory.getMessage("mwsc.help", arg0);
    }
    
    public static String MWSC_HELP(Object arg0) {
        return localizer.localize(localizableMWSC_HELP(arg0));
    }
    
    public static Localizable localizableMWSC_USAGE_EXAMPLES() {
        return messageFactory.getMessage("mwsc.usage.examples");
    }

    public static String MWSC_USAGE_EXAMPLES() {
        return localizer.localize(localizableMWSC_USAGE_EXAMPLES());
    }

}
