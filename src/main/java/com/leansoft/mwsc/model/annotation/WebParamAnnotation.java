package com.leansoft.mwsc.model.annotation;

import com.leansoft.mxjc.model.Annotatable;

/**
 * Represents a common representation of a web parameter annotation. 
 * Customizes the mapping of an individual parameter to a Web Service message part and XML element.
 *
 * @author bulldog
 */
public class WebParamAnnotation implements Annotatable
{
    private String name = "";
    private String targetNamespace = "";
    private String partName = "";
    private boolean header = false;
    private String mode = "javax.jws.WebParam.Mode.IN";

    public String getPartName()
    {
        return partName;
    }

    public void setPartName(String partName)
    {
        this.partName = partName;
    }

    /**
     * Returns the name of the parameter as it appears in the WSDL. For RPC bindings, this is name of the wsdl:part
     * representing  the parameter. For document bindings, this is the local name of the XML element representing the
     * parameter.  Defaults to the name of the parameter as it appears in the argument list.
     *
     * @return the name of the parameter as it appears in the WSDL.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the parameter as it appears in the WSDL. For RPC bindings, this is name of the wsdl:part
     * representing  the parameter. For document bindings, this is the local name of the XML element representing the
     * parameter.  Defaults to the name of the parameter as it appears in the argument list.
     *
     * @param name the new name of the parameter as it appears in the WSDL.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the XML namespace for the parameter. Only used with document bindings, where the parameter maps to an XML
     * element. Defaults to the targetNamespace for the Web Service.
     *
     * @return the XML namespace for the parameter.
     */
    public String getTargetNamespace()
    {
        return targetNamespace;
    }

    /**
     * Sets the XML namespace for the parameter. Only used with document bindings, where the parameter maps to an XML
     * element. Defaults to the targetNamespace for the Web Service.
     *
     * @param targetNamespace the XML namespace for the parameter.
     */
    public void setTargetNamespace(String targetNamespace)
    {
        this.targetNamespace = targetNamespace;
    }

    /**
     * Returns the direction in which the parameter is flowing.
     *
     * @return the parameter mode.
     */
    public String getMode()
    {
        return mode;
    }

    /**
     * Sets the direction in which the parameter is flowing.
     *
     * @param mode the new parameter mode.
     * @throws IllegalArgumentException if <code>mode</code> is not a valid mode.
     */
    public void setMode(String mode)
    {
        this.mode = mode;
    }

    /**
     * If <code>true</code>, the parameter is pulled from a message header rather then the message body.
     *
     * @return <code>true</code> if a header; <code>false</code> otherwise.
     */
    public boolean isHeader()
    {
        return header;
    }

    /**
     * Determines whether this parameter is a header.
     *
     * @param header <code>true</code> if a header; <code>false</code> otherwise.
     */
    public void setHeader(boolean header)
    {
        this.header = header;
    }

	public boolean isParameterProvided() {
		if (name != null && name.length() > 0) return true;
		if (targetNamespace != null && targetNamespace.length() > 0) return true;
		if (partName != null && partName.length() > 0) return true;
		if (header) return true;
		if (mode != null && !mode.equals("javax.jws.WebParam.Mode.IN")) return true;		
		return false;
	}
	
    /**
     * Returns a <code>String</code> representation of this <code>WebParamAnnotation</code>.
     *
     * @return a string representation.
     */
    public String toString()
    {
		String value = "";
		if (name != null && name.length() > 0) {
			value += "name = \"" + name + "\"";
		}
		if (targetNamespace != null && targetNamespace.length() > 0) {
			if (value.length() != 0) {
				value += ", ";
			}
			value += "targetNamespace = \"" + targetNamespace + "\"";
		}
		if (partName != null && partName.length() > 0) {
			if (value.length() != 0) {
				value += ", ";
			}
			value += "partName = \"" + partName + "\"";
		}
		if (header) {
			if (value.length() != 0) {
				value += ", ";
			}
			value += "header = true";
		}
		if (mode != null && !mode.equals("javax.jws.WebParam.Mode.IN")) {
			if (value.length() != 0) {
				value += ", ";
			}
			value += "mode = \"" + mode + "\"";
		}
		value = "WebParam(" + value + ")";
		return value;
    }
}
