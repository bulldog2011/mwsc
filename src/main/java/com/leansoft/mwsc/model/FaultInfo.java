package com.leansoft.mwsc.model;

import javax.xml.namespace.QName;

import com.leansoft.mxjc.model.TypeInfo;

public class FaultInfo {
	
	// package name of this class
	private String packageName;
	// simple name of this class
	private String name;
	// full name of this class
	private String fullName;
	
	// the type of the faultInfo this fault contains
	private TypeInfo faultBeanTypeInfo;
	
	// doc comment of this class
	private String docComment;
	
	// the schema name of this fault
	private QName schemaName;

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public TypeInfo getFaultBeanTypeInfo() {
		return faultBeanTypeInfo;
	}

	public void setFaultBeanTypeInfo(TypeInfo faultBeanTypeInfo) {
		this.faultBeanTypeInfo = faultBeanTypeInfo;
	}

	public QName getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(QName schemaName) {
		this.schemaName = schemaName;
	}

	public String getDocComment() {
		return docComment;
	}

	public void setDocComment(String docComment) {
		this.docComment = docComment;
	}
}
