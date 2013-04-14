package com.leansoft.mwsc.module;

import com.leansoft.mwsc.module.nano.NanoWSClientModule;
import com.leansoft.mwsc.module.pico.PicoWSClientModule;
import com.leansoft.mxjc.module.ModuleName;

public class WSModuleFactory {
	public static WSClientModule getModule(ModuleName name) {
		if (name == ModuleName.NANO) {
			return new NanoWSClientModule();
		} else {
			return new PicoWSClientModule();
		}
	}
}
