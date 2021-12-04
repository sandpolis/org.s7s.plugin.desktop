//============================================================================//
//                                                                            //
//                         Copyright © 2015 Sandpolis                         //
//                                                                            //
//  This source file is subject to the terms of the Mozilla Public License    //
//  version 2. You may not use this file except in compliance with the MPL    //
//  as published by the Mozilla Foundation.                                   //
//                                                                            //
//============================================================================//
package com.sandpolis.plugin.desktop.agent.kilo.exe;

import static com.sandpolis.core.instance.stream.StreamStore.StreamStore;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLiteOrBuilder;
import com.google.protobuf.UnsafeByteOperations;
import com.sandpolis.core.instance.exelet.Exelet;
import com.sandpolis.core.instance.exelet.ExeletContext;
import com.sandpolis.core.instance.stream.OutboundStreamAdapter;
import com.sandpolis.plugin.desktop.agent.kilo.JavaDesktopSource;
import com.sandpolis.plugin.desktop.Messages.EV_DesktopStreamOutput;
import com.sandpolis.plugin.desktop.Messages.RQ_DesktopStream;
import com.sandpolis.plugin.desktop.Messages.RS_DesktopStream;
import com.sandpolis.plugin.desktop.Messages.RQ_CaptureScreenshot;
import com.sandpolis.plugin.desktop.Messages.RS_CaptureScreenshot;

public final class DesktopExe extends Exelet {

	@Handler(auth = true)
	public static RS_CaptureScreenshot rq_screenshot(RQ_CaptureScreenshot rq) throws Exception {

		try (var out = new ByteArrayOutputStream()) {
			BufferedImage screenshot = new Robot()
					.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			ImageIO.write(screenshot, "jpg", out);

			return RS_CaptureScreenshot.newBuilder().setData(UnsafeByteOperations.unsafeWrap(out.toByteArray()))
					.build();
		}
	}

	@Handler(auth = true)
	public static RS_DesktopStream rq_desktop_stream(ExeletContext context, RQ_DesktopStream rq) {

		var source = new JavaDesktopSource();
		var outbound = new OutboundStreamAdapter<EV_DesktopStreamOutput>(rq.getStreamId(), context.connector,
				context.request.getFrom());
		StreamStore.add(source, outbound);

		context.defer(() -> {
			source.start();
		});

		return RS_DesktopStream.DESKTOP_STREAM_OK;
	}

	private DesktopExe() {
	}
}
