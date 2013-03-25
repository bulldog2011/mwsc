package com.leansoft.mwsc.model.annotation;

import com.leansoft.mxjc.model.Annotatable;

/**
 * Represents an common representation of a web service annotation. 
 * Specifies that the given method is exposed as a Web
 * Service operation, making it part of the Web Service's public contract. 
 * A WebMethod annotation is required for each method that is published by the Web Service.
 *
 * @author bulldog
 */
public class WebServiceAnnotation implements Annotatable
{
    private String endpointInterface = "";
    private String name = "";
    private String serviceName = "";
    private String targetNamespace = "";
    private String portName = "";
    private String wsdlLocation;

    /**
     * The location of the WSDL for the service.
     * @return
     */
    public String getWsdlLocation()
    {
        return wsdlLocation;
    }

    public void setWsdlLocation(String wsdlLocation)
    {
        this.wsdlLocation = wsdlLocation;
    }

    public String getPortName()
    {
        return portName;
    }

    public void setPortName(String portName)
    {
        this.portName = portName;
    }

    /**
     * Returns the name of the Web Service. Used as the name of the wsdl:portType when mapped to WSDL 1.1.  Defaults to
     * the simple name of the Java class or interface.
     *
     * @return the name of the Web Service.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the Web Service. Used as the name of the wsdl:portType when mapped to WSDL 1.1.  Defaults to the
     * simple name of the Java class or interface.
     *
     * @param name The new name of the Web Service.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the complete name of the service endpoint interface defining the service's abstract Web Service
     * contract.
     *
     * @return the name of the service endpoint interface.
     */
    public String getEndpointInterface()
    {
        return endpointInterface;
    }

    /**
     * Sets the complete name of the service endpoint interface defining the service's abstract Web Service contract.
     *
     * @param endpointInterface the new name of the service endpoint interface.
     */
    public void setEndpointInterface(String endpointInterface)
    {
        this.endpointInterface = endpointInterface;
    }

    /**
     * Returns the service name of the Web Service. Used as the name of the wsdl:service when mapped to WSDL 1.1.  Not
     * allowed on interfaces. Defaults to the simple name of the Java class + "Service".
     *
     * @return the service name of the Web Service.
     */
    public String getServiceName()
    {
        return serviceName;
    }

    /**
     * Sets the service name of the Web Service. Used as the name of the wsdl:service when mapped to WSDL 1.1.  Not
     * allowed on interfaces. Defaults to the simple name of the Java class + "Service".
     *
     * @param serviceName the new service name of the Web Service.
     */
    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

    /**
     * Returns the XML namespace used for the WSDL and XML elements generated from this Web Service.
     *
     * @return the XML namespace used.
     */
    public String getTargetNamespace()
    {
        return targetNamespace;
    }

    /**
     * Sets the XML namespace used for the WSDL and XML elements generated from this Web Service.
     *
     * @param targetNamespace the new XML namespace used.
     */
    public void setTargetNamespace(String targetNamespace)
    {
        this.targetNamespace = targetNamespace;
    }

    /**
     * Returns a String representation of this <code>WebServiceAnnotation</code> attribute.
     *
     * @return a string representation.
     */
    public String toString()
    {
		String value = "";
		if (endpointInterface != null && endpointInterface.length() > 0) {
			value += "endpointInterface = \"" + endpointInterface + "\"";
		}
		if (name != null && name.length() > 0) {
			if (value.length() != 0) {
				value += ", ";
			}
			value += "name = \"" + name + "\"";;
		}
		if (serviceName != null && serviceName.length() > 0) {
			if (value.length() != 0) {
				value += ", ";
			}
			value += "serviceName = \"" + serviceName + "\"";;
		}
		if (targetNamespace != null && targetNamespace.length() > 0) {
			if (value.length() != 0) {
				value += ", ";
			}
			value += "targetNamespace = \"" + targetNamespace + "\"";;
		}
		if (portName != null && portName.length() > 0) {
			if (value.length() != 0) {
				value += ", ";
			}
			value += "portName = \"" + portName + "\"";;
		}
		if (wsdlLocation != null && wsdlLocation.length() > 0) {
			if (value.length() != 0) {
				value += ", ";
			}
			value += "wsdlLocation = \"" + wsdlLocation + "\"";;
		}
		value = "WebService(" + value + ")";
		return value;
	}

	public boolean isParameterProvided() {
		if (endpointInterface != null && endpointInterface.length() > 0)  return true;
		if (name != null && name.length() > 0) return true;
		if (serviceName != null && serviceName.length() > 0) return true;
		if (targetNamespace != null && targetNamespace.length() > 0) return true;
		if (portName != null && portName.length() > 0) return true;
		if (wsdlLocation != null && wsdlLocation.length() > 0) return true;
		return false;
	}
    
}
