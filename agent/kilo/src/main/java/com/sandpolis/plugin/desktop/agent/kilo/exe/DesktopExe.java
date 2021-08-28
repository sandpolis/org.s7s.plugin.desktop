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

import static com.sandpolis.core.foundation.util.ProtoUtil.begin;
import static com.sandpolis.core.foundation.util.ProtoUtil.failure;
import static com.sandpolis.core.foundation.util.ProtoUtil.success;
import static com.sandpolis.core.net.stream.StreamStore.StreamStore;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import com.google.protobuf.MessageLiteOrBuilder;
import com.google.protobuf.ByteString;
import com.sandpolis.core.net.exelet.Exelet;
import com.sandpolis.core.net.exelet.ExeletContext;
import com.sandpolis.core.net.stream.OutboundStreamAdapter;
import com.sandpolis.plugin.desktop.agent.kilo.JavaDesktopSource;
import com.sandpolis.plugin.desktop.msg.MsgDesktop.RQ_Screenshot;
import com.sandpolis.plugin.desktop.msg.MsgDesktop.RS_Screenshot;
import com.sandpolis.plugin.desktop.msg.MsgRd.EV_DesktopStream;
import com.sandpolis.plugin.desktop.msg.MsgRd.RQ_DesktopStream;

public final class DesktopExe extends Exelet {

	@Handler(auth = true)
	public static MessageLiteOrBuilder rq_screenshot(RQ_Screenshot rq) {
		var outcome = begin();

		try (var out = new ByteArrayOutputStream()) {
			BufferedImage screenshot = new Robot()
					.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			ImageIO.write(screenshot, "jpg", out);

			return RS_Screenshot.newBuilder().setData(ByteString.copyFrom(out.toByteArray()));
		} catch (Exception e) {
			return failure(outcome);
		}
	}

	@Handler(auth = true)
	public static MessageLiteOrBuilder rq_desktop_stream(ExeletContext context, RQ_DesktopStream rq) {
		var outcome = begin();
		// TODO use correct stream ID
		// Stream stream = new Stream();

		context.defer(() -> {
			var source = new JavaDesktopSource();
			var outbound = new OutboundStreamAdapter<EV_DesktopStream>(rq.getId(), context.connector,
					context.request.getFrom());
			StreamStore.add(source, outbound);
			source.start();
		});

		return success(outcome);
	}

	@Handler(auth = true)
	public static void ev_desktop_stream(ExeletContext context, EV_DesktopStream ev) {
		StreamStore.streamData(context.request.getId(), ev);
	}

	private DesktopExe() {
	}
}
