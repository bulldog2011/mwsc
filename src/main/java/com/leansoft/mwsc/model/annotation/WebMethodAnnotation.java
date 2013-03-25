package com.leansoft.mwsc.model.annotation;

import com.leansoft.mxjc.model.Annotatable;


/**
 * A common representation of a web method annotation.
 * Specifies that the given method is exposed as a Web Service operation, 
 * making it part of the Web Service's public contract. 
 * A WebMethod annotation is required for each method that is published by the Web Service.
 *
 * @author bulldog
 */
public class WebMethodAnnotation implements Annotatable
{
    private String action = "";
    private String operationName = "";
    private boolean exclude = false;
    
    public boolean isExclude()
    {
        return exclude;
    }

    public void setExclude(boolean exclude)
    {
        this.exclude = exclude;
    }

    /**
     * Returns the action for this operation. For SOAP bindings, this determines the value of the SOAPAction header.
     *
     * @return the action for this operation.
     */
    public String getAction()
    {
        return action;
    }

    /**
     * Sets the action for this operation. For SOAP bindings, this determines the value of the SOAPAction header.
     *
     * @param action the new action for this operation.
     */
    public void setAction(String action)
    {
        this.action = action;
    }

    /**
     * Returns the name of the wsdl:operation matching this method. By default the WSDL operation name will be the same
     * as the Java method name.
     *
     * @return the name of the wsdl:operation matching this method.
     */
    public String getOperationName()
    {
        return operationName;
    }

    /**
     * Sets the name of the wsdl:operation matching this method. By default the WSDL operation name will be the same as
     * the Java method name.
     *
     * @param operationName the new name of the wsdl:operation matching this method.
     */
    public void setOperationName(String operationName)
    {
        this.operationName = operationName;
    }

    /**
     * Returns a <code>String</code> representation of this <code>WebMethodAnnotation</code>.
     *
     * @return a string representation.
     */
    public String toString()
    {
		String value = "";
		if (operationName != null && operationName.length() > 0) {
			value += "operationName = \"" + operationName + "\"";
		}
		if (action != null && action.length() > 0) {
			if (value.length() != 0) {
				value += ", ";
			}
			value += "action = \"" + action + "\"";
		}
		if (exclude) {
			if (value.length() != 0) {
				value += ", ";
			}
			value += "exclude = true";
		}
		value = "WebMethod(" + value + ")";
		return value;
    }

	public boolean isParameterProvided() {
		if (operationName != null && operationName.length() > 0) return true;
		if (action != null && action.length() > 0) return true;
		if (exclude) return true;
		return false;
	}
}
