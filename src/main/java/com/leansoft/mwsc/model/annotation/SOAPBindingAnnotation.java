package com.leansoft.mwsc.model.annotation;

import com.leansoft.mxjc.model.Annotatable;

/**
 * Represents an common representation of a soap binding annotation. 
 * Specifies the mapping of the Web Service onto the SOAP message protocol.
 *
 * @author bulldog
 */
public class SOAPBindingAnnotation implements Annotatable
{
	private String style;
	private String use;
	private String parameterStyle;
	
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getUse() {
		return use;
	}
	public void setUse(String use) {
		this.use = use;
	}
	public String getParameterStyle() {
		return parameterStyle;
	}
	public void setParameterStyle(String parameterStyle) {
		this.parameterStyle = parameterStyle;
	}
	
	public boolean isParameterProvided() {
		if (style != null && style.length() > 0) return true;
		if (use != null && use.length() > 0) return true;
		if (parameterStyle != null && parameterStyle.length() > 0) return true;
		return false;
	}
	
	/**
	 * A string representation of the annotation
	 */
	public String toString() {
		String value = "";
		if (style != null && style.length() > 0) {
			value += "style = " + style;
		}
		if (use != null && use.length() > 0) {
			if (value.length() != 0) {
				value += ", ";
			}
			value += "use = " + use;
		}
		if (parameterStyle != null && parameterStyle.length() > 0) {
			if (value.length() != 0) {
				value += ", ";
			}
			value += "parameterStyle = " + parameterStyle;
		}
		value = "SOAPBinding(" + value + ")";
		return value;
	}
}
