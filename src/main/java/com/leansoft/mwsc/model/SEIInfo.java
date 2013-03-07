package com.leansoft.mwsc.model;

import java.util.ArrayList;
import java.util.List;

import com.leansoft.mwsc.model.annotation.SOAPBindingAnnotation;
import com.leansoft.mwsc.model.annotation.WebServiceAnnotation;

/**
 * Service Endpoint Interface model for codegen
 * 
 * @author bulldog
 *
 */
public class SEIInfo {
	// package name of this interface
	private String pkgName;
	// simple name of this interface
	private String name;
	// doc comment of this interface
	private String docComment;
	
	// info for WebService annotation
	private WebServiceAnnotation webServiceAnnotation;
	
	// info for SOAPBinding annotation
	private SOAPBindingAnnotation soapBindingAnnotation;
	
	// a list of methods this interface contains
	private final List<MethodInfo> methods = new ArrayList<MethodInfo>();
	
	/**
	 * the methods this interface contains
	 * 
	 * @return a list of {@link MethodInfo} instance
	 */
	public List<MethodInfo> getMethods() {
		return methods;
	}
	
	/**
	 * package name of this interface
	 * @return package name of this interface
	 */
	public String getPackageName() {
		return pkgName;
	}
	/**
	 * set package name of this interface
	 * @param pkgName
	 */
	public void setPackageName(String pkgName) {
		this.pkgName = pkgName;
	}
	/**
	 * simple name of this interface
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 * set simple name of this interface
	 * @param simple name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * doc comment of this interface
	 * @return
	 */
	public String getDocComment() {
		return docComment;
	}
	/**
	 * set doc comment of this interface
	 * @param docComment
	 */
	public void setDocComment(String docComment) {
		this.docComment = docComment;
	}
	
	/**
	 * set jsr181 WebService annotation information on the SEI
	 * 
	 * @return
	 */
	public WebServiceAnnotation getWebServiceAnnotation() {
		return webServiceAnnotation;
	}
	
	/**
	 * jsr 181 WebSerivce annotation information on the SEI
	 * 
	 * @param webServiceAnnotation
	 */
	public void setWebServiceAnnotation(WebServiceAnnotation webServiceAnnotation) {
		this.webServiceAnnotation = webServiceAnnotation;
	}
	
	/**
	 * set jsr181 SOAPBinding annotation information on the SEI
	 * 
	 * @return
	 */
	public SOAPBindingAnnotation getSoapBindingAnnotation() {
		return soapBindingAnnotation;
	}
	
	/**
	 * jsr181 SOAPBinding annotation information on the SEI
	 * 
	 * @param soapBindingAnnotation
	 */
	public void setSoapBindingAnnotation(SOAPBindingAnnotation soapBindingAnnotation) {
		this.soapBindingAnnotation = soapBindingAnnotation;
	}
	
	
}
