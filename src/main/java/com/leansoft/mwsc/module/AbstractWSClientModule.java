package com.leansoft.mwsc.module;

import java.io.ByteArrayOutputStream;
import java.net.URL;

import org.xml.sax.helpers.LocatorImpl;

import com.leansoft.mxjc.model.FileInfo;
import com.leansoft.mxjc.module.FreemarkerProcessor;
import com.leansoft.mxjc.module.XjcModuleException;
import com.sun.tools.ws.wscompile.ErrorReceiver;

import freemarker.template.SimpleHash;

/**
 * 
 * Common ws client module
 * 
 * @author bulldog
 *
 */
public abstract class AbstractWSClientModule implements WSClientModule {
	private ErrorReceiver errorReceiver;

	/**
	 * Get freemarker datamodel
	 * 
	 * @return a new SimpleHash instance
	 */
	protected SimpleHash getFreemarkerModel() {
		return new SimpleHash();
	}
	
	/**
	 * Set ErrorReceiver instance to be used for error reporting
	 */
	public void setErrorReceiver(ErrorReceiver errorReceiver) {
		this.errorReceiver = errorReceiver;
	}
	
	/**
	 * Get a template URL for the template of the given template name.
	 * 
	 * @param template
	 *            The specified template.
	 * @return The URL to the specified template.
	 * @throws WscModuleException
	 */
	abstract protected URL getTemplateURL(String template) throws WscModuleException;
	

	/**
	 * Generate files according to specific template, datamodel and file
	 * information,
	 * 
	 * No cache for this interface
	 * 
	 * @param template
	 *            , template URL
	 * @param fmModel
	 *            , freemarker datamodel
	 * @param fileName
	 *            , file name
	 * @param suffix
	 *            , file suffix
	 * @param relativePath
	 *            , file relative path(such as a\b\c)         
	 * @return FileInfo instance
	 * @throws WscModuleException
	 */
	protected FileInfo generateFile(URL template, Object fmModel,
			String fileName, String suffix, String relativePath)
			throws WscModuleException {
		byte[] context = processTemplate(template, fmModel);
		FileInfo fileInfo = new FileInfo();
		fileInfo.setName(fileName);
		fileInfo.setPath(relativePath);
		fileInfo.setSuffix(suffix);
		fileInfo.setContent(context);
		return fileInfo;
	}

	/**
	 * Process given template and datamodel
	 * 
	 * @param templateURL
	 *            , template URL
	 * @param model
	 *            , freemarker datamodel
	 * @return byte array
	 * @throws WscModuleException
	 */
	protected byte[] processTemplate(URL templateURL, Object model)
			throws WscModuleException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			FreemarkerProcessor.getInstance().processTemplate(templateURL, model, baos);
			return baos.toByteArray();
		} catch (XjcModuleException e) {
			throw new WscModuleException("Fail to process template "
					+ templateURL, e);
		}
	}
	
	/**
	 * information level report
	 * 
	 * @param msg
	 */
	protected void info(String msg) {
		if (this.errorReceiver != null) {
			this.errorReceiver.debug(msg);
		}
	}
	
	/**
	 * error level report
	 * 
	 * @param msg
	 */
	protected void error(String msg) {
		if (this.errorReceiver != null) {
			LocatorImpl locator = new LocatorImpl();
			locator.setLineNumber(-1);
			locator.setSystemId("module : " + this.getName().toString());
			this.errorReceiver.error(locator, msg);
		}
	}
	
	protected void warn(String msg) {
		if (this.errorReceiver != null) {
			LocatorImpl locator = new LocatorImpl();
			locator.setLineNumber(-1);
			locator.setSystemId("module : " + this.getName().toString());
			this.errorReceiver.warning(locator, msg);
		}
	}
	
	/**
	 * error level report
	 * 
	 * @param msg
	 */
	protected void debug(String msg) {
		if (this.errorReceiver != null) {
			this.errorReceiver.debug(msg);
		}
	}
}
