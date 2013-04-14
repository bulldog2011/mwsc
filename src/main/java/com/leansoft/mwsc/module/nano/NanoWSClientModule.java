package com.leansoft.mwsc.module.nano;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.leansoft.mwsc.model.MethodInfo;
import com.leansoft.mwsc.model.ParameterInfo;
import com.leansoft.mwsc.model.SEIInfo;
import com.leansoft.mwsc.model.WSCodeGenModel;
import com.leansoft.mwsc.module.AbstractWSClientModule;
import com.leansoft.mwsc.module.WscModuleException;
import com.leansoft.mxjc.model.CGConfig;
import com.leansoft.mxjc.model.FileInfo;
import com.leansoft.mxjc.model.TypeInfo;
import com.leansoft.mxjc.module.ModuleName;
import com.leansoft.mxjc.util.ClassNameUtil;

import freemarker.template.SimpleHash;

public class NanoWSClientModule extends AbstractWSClientModule {
	
	private URL soapClientTemplate;
	private URL xmlClientTemplate;

	@Override
	public ModuleName getName() {
		return ModuleName.NANO;
	}

	@Override
	public void init() throws WscModuleException {
		info("NanoWSClientModule loading templates ...");
		loadTemplates();
	}
	
	private void loadTemplates() throws WscModuleException {
		//load template
		soapClientTemplate = this.getTemplateURL("client-endpoint-soap.fmt");
		xmlClientTemplate = this.getTemplateURL("client-endpoint-xml.fmt");
	}

	@Override
	public Set<FileInfo> generate(WSCodeGenModel cgModel, CGConfig config)
			throws WscModuleException {
		// freemarker datamodel
		SimpleHash fmModel = this.getFreemarkerModel();
		
		// container for target codes
		Set<FileInfo> targetFileSet = new HashSet<FileInfo>();
		
		info("Generating the Nano web serivce client classes...");
		
		fmModel.put("group", config.picoServiceGroup);
		fmModel.put("config", config);
		
		// generate endpoint interface
		for (SEIInfo interfaceInfo : cgModel.getServiceEndpointInterfaces()) {
			fmModel.put("imports", this.getInterfaceImports(interfaceInfo));
			fmModel.put("endpointInterface", interfaceInfo);

			String relativePath = ClassNameUtil.packageNameToPath(interfaceInfo.getPackageName());
			relativePath += File.separator + "client";
			FileInfo eiSoapClient = this.generateFile(soapClientTemplate, fmModel, interfaceInfo.getName() + "_SOAPClient", "java", relativePath);
			targetFileSet.add(eiSoapClient);
			FileInfo eiXmlClient = this.generateFile(xmlClientTemplate, fmModel, interfaceInfo.getName() + "_XMLClient", "java", relativePath);
			targetFileSet.add(eiXmlClient);
		}
		
		return targetFileSet;
	}
	
	/**
	 * 
	 * Helper to find out all classes that will be imported by an service endpoint interface
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
		URL url = NanoWSClientModule.class.getResource("template/" + template);
		if (url == null) {
			throw new WscModuleException("Fail to load required template file : "
					+ template);
		}
		debug("NanoWSClientModule get template : " + url.toString());
		return url;
	}

}
