package org.eclipse.debug.internal.ui;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */

import org.eclipse.debug.core.*;
import org.eclipse.debug.core.model.*;
import org.eclipse.jface.action.IAction;

public class TerminateAndRemoveActionDelegate extends ControlActionDelegate {
	
	private static final String PREFIX= "terminate_and_remove_action.";

	/**
	 * @see ControlActionDelegate
	 */
	public void initializeForOwner(ControlAction controlAction) {		
		super.initializeForOwner(controlAction);
		controlAction.setEnabled(!controlAction.getStructuredSelection().isEmpty());
	}
	
	/**
	 * @see ControlActionDelegate
	 */
	protected void doAction(Object element) throws DebugException {
		ITerminate terminate= (ITerminate)element;
		if (!terminate.isTerminated()) {
			terminate.terminate();
		}		
		
		ILaunch launch= null;
		if (element instanceof ILaunch) {
			launch= (ILaunch) element;
		} else if (element instanceof IDebugElement) {
			launch= ((IDebugElement) element).getLaunch();
		} else if (element instanceof IProcess) {
			launch= ((IProcess) element).getLaunch();
		}
		ILaunchManager lManager= DebugPlugin.getDefault().getLaunchManager();
		lManager.deregisterLaunch(launch);
	}

	/**
	 * @see ControlActionDelegate
	 */
	public boolean isEnabledFor(Object element) {
		return element instanceof ITerminate;
	}	
	/**
	 * @see ControlActionDelegate
	 */
	protected String getPrefix() {
		return PREFIX;
	}
	
	protected String getHelpContextId() {
		return IDebugHelpContextIds.TERMINATE_AND_REMOVE_ACTION;
	}

	/**
	 * @see ControlActionDelegate
	 */
	protected void setActionImages(IAction action) {	
	}	
}