package com.leansoft.mwsc.model.annotation;

import com.leansoft.mxjc.model.Annotatable;

/**
 * Represents an common representation of a web result annotation. 
 * Customizes the mapping of the return value to a WSDL part and XML element.
 *
 * @author bulldog
 */
public class WebResultAnnotation implements Annotatable
{
    private String name = "return";
    private String partName = "return";
    private String targetNamespace = "";
    private boolean header;

    public String getPartName()
    {
        return partName;
    }

    public void setPartName(String partName)
    {
        this.partName = partName;
    }

    public boolean isHeader()
    {
        return header;
    }

    public void setHeader(boolean header)
    {
        this.header = header;
    }

    /**
     * Returns the name of the return value as it appears in the WSDL and messages on the wire. For RPC bindings, this
     * is the name of the wsdl:part representing the return value. For document bindings, this is the local name of the
     * XML element representing the return value.
     *
     * @return the name of the return value.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of return value as it appears in the WSDL and messages on the wire. For RPC bindings, this is the
     * name of  the wsdl:part representing the return value. For document bindings, this is the local name of the XML
     * element  representing the return value.
     *
     * @param name the new name of the return value.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the XML namespace for the return value. Only used with document bindings, where the return value maps to
     * an  XML element. Defaults to the targetNamespace of the Web Service.
     *
     * @return the XML namespace for the return value.
     */
    public String getTargetNamespace()
    {
        return targetNamespace;
    }

    /**
     * Sets the XML namespace for the return value. Only used with document bindings, where the return value maps to an
     * XML element. Defaults to the targetNamespace of the Web Service.
     *
     * @param targetNamespace the new XML namespace for the return value.
     */
    public void setTargetNamespace(String targetNamespace)
    {
        this.targetNamespace = targetNamespace;
    }

	public boolean isParameterProvided() {
		if (name != null && !name.equals("return")) return true;
		if (partName != null && !partName.equals("return")) return true;
		if (targetNamespace != null && !targetNamespace.equals("return")) return true;
		if (header) return true;
		return false;
	}
	
    /**
     * Returns a String representation of this <code>WebResultAnnotation</code> attribute.
     *
     * @return a string representation.
     */
    public String toString()
    {
		String value = "";
		if (name != null && !name.equals("return")) {
			value += "name = \"" + name + "\"";
		}
		if (partName != null && !partName.equals("return")) {
			if (value.length() != 0) {
				value += ", ";
			}
			value += "partName = \"" + partName + "\"";
		}
		if (targetNamespace != null && targetNamespace.length() > 0) {
			if (value.length() != 0) {
				value += ", ";
			}
			value += "targetNamespace = \"" + targetNamespace + "\"";
		}
		if (header) {
			if (value.length() != 0) {
				value += ", ";
			}
			value += "header = true";
		}
		value = "WebResult(" + value + ")";
		return value;
    }
}
