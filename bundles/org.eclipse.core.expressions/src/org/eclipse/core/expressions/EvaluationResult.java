/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.core.expressions;

import org.eclipse.core.internal.expressions.Assert;

/**
 * An evaluation result represents the result of an expression
 * evaluation. There are exact three instances of evaluation 
 * result. They are: <code>FALSE</code>, <code>TRUE</code> and
 * <code>NOT_LOADED</code>. <code>NOT_LOADED</code> represents
 * the fact that an expression couldn't be evaluated since a
 * plug-in providing certain test expressions isn't loaded yet.
 * <p>
 * In addition the class implements the three operation <code>and
 * </code>, <code>or</code> and <code>not</code>. The operation are
 * defined as follows:
 * </p>
 * <table border="1" cellpadding="5">
 *   <tbody>
 *     <tr>
 *       <td><em>AND</em></td>
 *       <td>FALSE</td>
 *       <td>TRUE</td>
 *       <td>NOT_LOADED</td>
 *     </tr>
 *     <tr>
 *       <td>FALSE</td>
 *       <td>FALSE</td>
 *       <td>FALSE</td>
 *       <td>FALSE</td>
 *     </tr>
 *     <tr>
 *       <td>TRUE</td>
 *       <td>FALSE</td>
 *       <td>TRUE</td>
 *       <td>NOT_LOADED</td>
 *     </tr>
 *     <tr>
 *       <td>NOT_LOADED</td>
 *       <td>FALSE</td>
 *       <td>NOT_LOADED</td>
 *       <td>NOT_LOADED</td>
 *     </tr>
 *   </tbody>
 * </table>
 * <table border="1" cellpadding="5">
 *   <tbody>
 *     <tr>
 *       <td><em>OR</em></td>
 *       <td>FALSE</td>
 *       <td>TRUE</td>
 *       <td>NOT_LOADED</td>
 *     </tr>
 *     <tr>
 *       <td>FALSE</td>
 *       <td>FALSE</td>
 *       <td>TRUE</td>
 *       <td>NOT_LOADED</td>
 *     </tr>
 *     <tr>
 *       <td>TRUE</td>
 *       <td>TRUE</td>
 *       <td>TRUE</td>
 *       <td>TRUE</td>
 *     </tr>
 *     <tr>
 *       <td>NOT_LOADED</td>
 *       <td>NOT_LOADED</td>
 *       <td>TRUE</td>
 *       <td>NOT_LOADED</td>
 *     </tr>
 *   </tbody>
 * </table>
 * <table border="1" cellpadding="5">
 *   <tbody>
 *     <tr>
 *       <td><em>NOT<em></td>
 *       <td>FALSE</td>
 *       <td>TRUE</td>
 *       <td>NOT_LOADED</td>
 *     </tr>
 *     <tr>
 *       <td></td>
 *       <td>TRUE</td>
 *       <td>FALSE</td>
 *       <td>NOT_LOADED</td>
 *     </tr>
 *   </tbody>
 * </table>
 * 
 * @since 3.0
 */
public class EvaluationResult {
	
	private int fValue;
	
	private static final int FALSE_VALUE= 0;
	private static final int TRUE_VALUE= 1;
	private static final int NOT_LOADED_VALUE= 2;
	
	/** The evalutation result representing the value FALSE */
	public static final EvaluationResult FALSE= new EvaluationResult(FALSE_VALUE);
	/** The evaluation result respresenting the value TRUE */
	public static final EvaluationResult TRUE= new EvaluationResult(TRUE_VALUE);
	/** The evalutation result representing the value NOT_LOADED */
	public static final EvaluationResult NOT_LOADED= new EvaluationResult(NOT_LOADED_VALUE);

	private static final EvaluationResult[][] AND= new EvaluationResult[][] {
						// FALSE	//TRUE		//NOT_LOADED
		/* FALSE   */ { FALSE,		FALSE,		FALSE		},
		/* TRUE    */ { FALSE,		TRUE,		NOT_LOADED	},
		/* PNL     */ { FALSE,		NOT_LOADED, NOT_LOADED	},
	};

	private static final EvaluationResult[][] OR= new EvaluationResult[][] {
						// FALSE	//TRUE	//NOT_LOADED
		/* FALSE   */ { FALSE,		TRUE,	NOT_LOADED	},
		/* TRUE    */ { TRUE,		TRUE,	TRUE		},
		/* PNL     */ { NOT_LOADED,	TRUE, 	NOT_LOADED	},
	};

	private static final EvaluationResult[] NOT= new EvaluationResult[] {
		//FALSE		//TRUE	//NOT_LOADED
		TRUE,		FALSE,	NOT_LOADED
	};

	/*
	 * No instances outside of <code>EvaluationResult</code>
	 */
	private EvaluationResult(int value) {
		fValue= value;
	}
	
	/**
	 * Returns an <code>EvalutionResult</code> whose value is <code>this &amp;&amp; other)</code>.
	 * 
	 * @param other the right hand side of the and operation.
	 * 
	 * @return <code>this &amp;&amp; other</code> as defined by the evlaution result
	 */
	public EvaluationResult and(EvaluationResult op) {
		return AND[fValue][op.fValue];
	}
	
	/**
	 * Returns an <code>EvalutionResult</code> whose value is <code>this || other)</code>.
	 * 
	 * @param other the right hand side of the or operation.
	 * 
	 * @return <code>this || other</code> as defined by the evlaution result
	 */
	public EvaluationResult or(EvaluationResult op) {
		return OR[fValue][op.fValue];
	}
	
	/**
	 * Returns the inverted value of this evaluation result
	 * 
	 * @return the inverted value of this evaluation result
	 */
	public EvaluationResult not() {
		return NOT[fValue];
	}
	
	/**
	 * Returns an evaluation result instance representing the
	 * given boolean value. If the given boolean value is
	 * <code>true</code> then <code.ExpressionResult.TRUE<code>
	 * is returned. If the value is <code>false</code> the <code>
	 * ExpressionResult.FALSE</code> is returned.
	 * 
	 * @param b a boolean value
	 * 
	 * @return the expression result representing the boolean
	 *  value
	 */
	public static EvaluationResult valueOf(boolean b) {
		return b ? TRUE : FALSE;
	}
	
	/**
	 * Returns a evaluation result instance representing the
	 * given <code>Boolean</code> value. If the given <code>Boolean
	 * </code> value is <code>true</code> then <code.ExpressionResult.TRUE<code>
	 * is returned. If the value is <code>false</code> the <code>
	 * ExpressionResult.FALSE</code> is returned.
	 * 
	 * @param b a <code>Boolean</code> value
	 * 
	 * @return the expression result representing the <code>Boolean</code>
	 *  value
	 */
	public static EvaluationResult valueOf(Boolean b) {
		return b.booleanValue() ? TRUE : FALSE;
	}
	
	/*
	 * For debugging purpose only
	 */
	public String toString() {
		switch (fValue) {
			case 0:
				return "false"; //$NON-NLS-1$
			case 1:
				return "true"; //$NON-NLS-1$
			case 2:
				return "not_loaded"; //$NON-NLS-1$
		}
		Assert.isTrue(false);
		return null;
	}
}
