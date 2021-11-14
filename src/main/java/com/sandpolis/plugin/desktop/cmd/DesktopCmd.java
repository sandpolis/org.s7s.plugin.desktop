//============================================================================//
//                                                                            //
//                         Copyright © 2015 Sandpolis                         //
//                                                                            //
//  This source file is subject to the terms of the Mozilla Public License    //
//  version 2. You may not use this file except in compliance with the MPL    //
//  as published by the Mozilla Foundation.                                   //
//                                                                            //
//============================================================================//
package com.sandpolis.plugin.desktop.cmd;

import java.util.concurrent.CompletionStage;

import com.sandpolis.core.net.cmdlet.Cmdlet;
import com.sandpolis.plugin.desktop.Messages.RQ_CaptureScreenshot;
import com.sandpolis.plugin.desktop.Messages.RS_CaptureScreenshot;

/**
 * Contains desktop commands.
 *
 * @author cilki
 * @since 5.0.2
 */
public final class DesktopCmd extends Cmdlet<DesktopCmd> {

	/**
	 * Take a desktop screenshot.
	 *
	 * @return A response future
	 */
	public CompletionStage<RS_CaptureScreenshot> screenshot() {
		return request(RS_CaptureScreenshot.class, RQ_CaptureScreenshot.newBuilder());
	}

	/**
	 * Prepare for an asynchronous command.
	 *
	 * @return A configurable object from which all asynchronous (nonstatic)
	 *         commands in {@link DesktopCmd} can be invoked
	 */
	public static DesktopCmd async() {
		return new DesktopCmd();
	}

	private DesktopCmd() {
	}
}
