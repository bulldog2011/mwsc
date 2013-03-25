package com.leansoft.mwsc.model;

import java.io.Serializable;

import com.leansoft.mwsc.model.annotation.WebParamAnnotation;
import com.leansoft.mxjc.model.TypeInfo;

/**
 * Parameter model for codegen
 * 
 * @author bulldog
 *
 */
public class ParameterInfo implements Serializable {
	
	private static final long serialVersionUID = -1635019789993999839L;
	

	// the name of this parameter
	private String name;
	
	// the type of this parameter
	private TypeInfo type;
	
	private WebParamAnnotation webParamAnnotation;

	/**
	 * the name of this parameter
	 * @return parameter name
	 */
	public String getName() {
		return name;
	}

	/**
	 * set name of this parameter
	 * @param parameter name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * type of this parameter
	 * @return type of this parameter
	 */
	public TypeInfo getType() {
		return type;
	}

	/**
	 * set type of this parameter
	 * @param type
	 */
	public void setType(TypeInfo type) {
		this.type = type;
	}

	/**
	 * Get the annotation of this parameter
	 * 
	 * @return a WebParamAnnotation
	 */
	public WebParamAnnotation getWebParamAnnotation() {
		return webParamAnnotation;
	}

	/**
	 * Set the annotation of this parameter
	 * 
	 * @param webParamAnnotation
	 */
	public void setWebParamAnnotation(WebParamAnnotation webParamAnnotation) {
		this.webParamAnnotation = webParamAnnotation;
	}
	
}
