/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.core.internal.expressions;

public class Property {
	
	private Class fType;
	private String fNamespace;
	private String fName;
	
	private IPropertyTester fTester;

	/* package */ Property(Class type, String namespace, String name) {
		Assert.isNotNull(type);
		Assert.isNotNull(namespace);
		Assert.isNotNull(name);
		
		fType= type;
		fNamespace= namespace;
		fName= name;
	}
	
	/* package */ void setPropertyTester(IPropertyTester tester) {
		Assert.isNotNull(tester);
		fTester= tester;
	}
	
	public boolean isLoaded() {
		return fTester.isLoaded();
	}
	
	public boolean canLoad() {
		return fTester.canLoad();
	}
 	
	public boolean test(Object receiver, Object[] args, Object expectedValue) {
		return fTester.test(receiver, fName, args, expectedValue);
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof Property))
			return false;
		Property other= (Property)obj;
		return fType.equals(other.fType) && fNamespace.equals(other.fNamespace) && fName.equals(other.fName);
	}
	
	public int hashCode() {
		return (fType.hashCode() << 16) | fNamespace.hashCode() << 8 | fName.hashCode();
	}
}
