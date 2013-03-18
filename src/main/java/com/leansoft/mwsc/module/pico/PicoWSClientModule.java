package com.leansoft.mwsc.module.pico;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.leansoft.mwsc.model.MethodInfo;
import com.leansoft.mwsc.model.ParameterInfo;
import com.leansoft.mwsc.model.SEIInfo;
import com.leansoft.mwsc.model.WSCodeGenModel;
import com.leansoft.mxjc.model.CGConfig;
import com.leansoft.mxjc.model.FileInfo;
import com.leansoft.mxjc.model.TypeInfo;
import com.leansoft.mwsc.module.AbstractWSClientModule;
import com.leansoft.mwsc.module.WscModuleException;
import com.leansoft.mxjc.module.ModuleName;
import com.leansoft.mxjc.module.pico.Java2PicoTypeMapper;
import com.leansoft.mxjc.util.ClassNameUtil;

import freemarker.template.SimpleHash;

public class PicoWSClientModule extends AbstractWSClientModule {
	
	// references to templates
	private URL eiIntfSOAPTemplate;
	private URL eiImplSOAPTemplate;
	private URL eiIntfXMLTemplate;
	private URL eiImplXMLTemplate;

	@Override
	public ModuleName getName() {
		return ModuleName.PICO;
	}

	@Override
	public void init() throws WscModuleException {
		info("PicoWSClientModule loading templates ...");
		loadTemplates();
	}
	
	private void loadTemplates() throws WscModuleException {
		//load template
		eiIntfSOAPTemplate = this.getTemplateURL("client-endpoint-soap-interface.fmt");
		eiImplSOAPTemplate = this.getTemplateURL("client-endpoint-soap-impl.fmt");
		eiIntfXMLTemplate = this.getTemplateURL("client-endpoint-xml-interface.fmt");
		eiImplXMLTemplate = this.getTemplateURL("client-endpoint-xml-impl.fmt");
	}

	@Override
	public Set<FileInfo> generate(WSCodeGenModel cgModel, CGConfig config)
			throws WscModuleException {
		// freemarker datamodel
		SimpleHash fmModel = this.getFreemarkerModel();
		
		// container for target codes
		Set<FileInfo> targetFileSet = new HashSet<FileInfo>();
		
		info("Generating the Pico web serivce client classes...");
		
		if (config.picoPrefix == null) {
			warn("No prefix is provided, it's recommended to add prefix for Pico binding to avoid possible conflict");
		}
		String prefix = config.picoPrefix == null ? "" : config.picoPrefix;
		prefixType(cgModel, prefix);
		
		fmModel.put("group", config.picoServiceGroup);
		
		// generate endpoint interface
		for (SEIInfo interfaceInfo : cgModel.getServiceEndpointInterfaces()) {
			fmModel.put("imports", this.getInterfaceImports(interfaceInfo));
			fmModel.put("endpointInterface", interfaceInfo);
			// special logic for ebay service, just a convenient for ebay service proxy generation
			if (config.eBaySOAService) {
				fmModel.put("eBaySOAService", config.eBaySOAService);
			}
			if (config.eBayShoppingAPI) {
				fmModel.put("eBayShoppingAPI", config.eBayShoppingAPI);
			}
			String relativePath = ClassNameUtil.packageNameToPath(interfaceInfo.getPackageName());
			relativePath += File.separator + "client";
			FileInfo eiSoapIntf = this.generateFile(eiIntfSOAPTemplate, fmModel, interfaceInfo.getName() + "_SOAPClient", "h", relativePath);
			targetFileSet.add(eiSoapIntf);
			FileInfo eiSoapImpl = this.generateFile(eiImplSOAPTemplate, fmModel, interfaceInfo.getName() + "_SOAPClient", "m", relativePath);
			targetFileSet.add(eiSoapImpl);
			FileInfo eiXmlIntf = this.generateFile(eiIntfXMLTemplate, fmModel, interfaceInfo.getName() + "_XMLClient", "h", relativePath);
			targetFileSet.add(eiXmlIntf);
			FileInfo eiXmlImpl = this.generateFile(eiImplXMLTemplate, fmModel, interfaceInfo.getName() + "_XMLClient", "m", relativePath);
			targetFileSet.add(eiXmlImpl);
		}
		
		return targetFileSet;
	}
	
	// add prefix to avoid possible conflict
	private void prefixType(WSCodeGenModel model, String prefix) {
		for (SEIInfo intf : model.getServiceEndpointInterfaces()) {
			// update method i/o types
			for (MethodInfo method : intf.getMethods()) {
				// input parameter types
				for (ParameterInfo param : method.getParameters()) {
					TypeInfo paramType = param.getType();
					prefixType(paramType, prefix);
				}
				// output type
				TypeInfo returnType = method.getReturnType();
				prefixType(returnType, prefix);
			}
		}
	}
	
	// add prefix in the type full name
	private void prefixType(TypeInfo type, String prefix) {
		if (type == null) return; // be cautious
		// for pico primitives, do not prefix
		if (Java2PicoTypeMapper.lookupPicoType(type.getFullName()) != null) {
			return;
		}
		String name = type.getName();
		type.setName(prefix + name);
		type.setFullName(prefix + name); // remove package for pico
	}
	
	/**
	 * 
	 * Helper to find out all classes that will be imported by an interface
	 * 
	 * @param intf
	 *            , InterfaceInfo instance
	 * @return a set of class names that will be imported
	 */
	private Set<String> getInterfaceImports(SEIInfo intf) {
		Set<String> imports = new HashSet<String>();
		
		for (MethodInfo method : intf.getMethods()) {
			// import method parameter types
			for (ParameterInfo param : method.getParameters()) {
				TypeInfo paramType = param.getType();
				imports.add(paramType.getFullName());
			}
			// import return type
			TypeInfo returnType = method.getReturnType();
			imports.add(returnType.getFullName());
		}
		
		return imports;
	}

	@Override
	protected URL getTemplateURL(String template) throws WscModuleException {
		URL url = PicoWSClientModule.class.getResource("template/" + template);
		if (url == null) {
			throw new WscModuleException("Fail to load required template file : "
					+ template);
		}
		debug("PicoWSClientModule get template : " + url.toString());
		return url;
	}
	
}
