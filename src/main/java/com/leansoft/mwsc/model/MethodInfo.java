package com.leansoft.mwsc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.leansoft.mwsc.model.annotation.SOAPBindingAnnotation;
import com.leansoft.mwsc.model.annotation.WebMethodAnnotation;
import com.leansoft.mwsc.model.annotation.WebResultAnnotation;
import com.leansoft.mxjc.model.TypeInfo;

/**
 * 
 * Method model for codegen, included in {@link SEIInfo}
 * 
 * 
 * @author bulldog
 *
 */
public class MethodInfo implements Serializable {
	
	private static final long serialVersionUID = -7646518984431922995L;
	
	// method simple name
	private String name;
	// method return type
	private TypeInfo returnType;
	// doc comment of this method
	private String docComment;
	// a list of faults that will be thrown by this method
	private final List<FaultInfo> _throws = new ArrayList<FaultInfo>();
	
	// @WebMethod
	private WebMethodAnnotation webMethodAnnotation;
	
	// @WebResult
	private WebResultAnnotation webResultAnnotation;
	
	// @SOAPBinding
	private SOAPBindingAnnotation soapBindingAnnotation;
//	//operation name in wsdl
//	private String schemaName;
	// method params
	private final List<ParameterInfo> parameters = new ArrayList<ParameterInfo>();
	
	/**
	 * method parameters
	 * 
	 * @return a list of {@link ParamterInfo} instances
	 */
	public List<ParameterInfo> getParameters() {
		return parameters;
	}
	
	/**
	 * doc comment of this method
	 * @return
	 */
	public String getDocComment() {
		return docComment;
	}
	/**
	 * set doc comment of this method
	 * @param docComment
	 */
	public void setDocComment(String docComment) {
		this.docComment = docComment;
	}
	/**
	 * simple name of this method
	 * @return simple name
	 */
	public String getName() {
		return name;
	}
	/**
	 * set simple name of this method
	 * @param simple name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * return type of this method
	 * @return return type
	 */
	public TypeInfo getReturnType() {
		return returnType;
	}
	/**
	 * set return type of this method
	 * @param returnType
	 */
	public void setReturnType(TypeInfo returnType) {
		this.returnType = returnType;
	}
	
	/**
	 * get WebMethod annotation
	 * 
	 * @return a WebMethod annotation
	 */
	public WebMethodAnnotation getWebMethodAnnotation() {
		return webMethodAnnotation;
	}
	
	/**
	 * set WebMethod annoation
	 * 
	 * @param webMethodAnnotation
	 */
	public void setWebMethodAnnotation(WebMethodAnnotation webMethodAnnotation) {
		this.webMethodAnnotation = webMethodAnnotation;
	}
	
	/**
	 * get WebResult annotation
	 * 
	 * @return a WebResult annotation
	 */
	public WebResultAnnotation getWebResultAnnotation() {
		return webResultAnnotation;
	}
	
	/**
	 * Set WebResult annotation
	 * 
	 * @param webResultAnnotation
	 */
	public void setWebResultAnnotation(WebResultAnnotation webResultAnnotation) {
		this.webResultAnnotation = webResultAnnotation;
	}
	
	/**
	 * Set SOAPBinding annotation
	 * 
	 * @return a SOAPBinding annotation
	 */
	public SOAPBindingAnnotation getSoapBindingAnnotation() {
		return soapBindingAnnotation;
	}
	
	/**
	 * Get SOAPBinding annotation
	 * 
	 * @param soapBindingAnnotation
	 */
	public void setSoapBindingAnnotation(SOAPBindingAnnotation soapBindingAnnotation) {
		this.soapBindingAnnotation = soapBindingAnnotation;
	}

	/**
	 * Get a list of faults that will be thrown by this method
	 * 
	 * @return
	 */
	public List<FaultInfo> getThrows() {
		return _throws;
	}
	
	
//	/**
//	 * operation name in wsdl
//	 * @return operation name in wsdl
//	 */
//	public String getSchemaName() {
//		return schemaName;
//	}
//	/**
//	 * set operation name in wsdl
//	 */
//	public void setSchemaName(String schemaName) {
//		this.schemaName = schemaName;
//	}
	
}
