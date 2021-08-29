//============================================================================//
//                                                                            //
//                         Copyright © 2015 Sandpolis                         //
//                                                                            //
//  This source file is subject to the terms of the Mozilla Public License    //
//  version 2. You may not use this file except in compliance with the MPL    //
//  as published by the Mozilla Foundation.                                   //
//                                                                            //
//============================================================================//
package com.sandpolis.plugin.desktop.client.lifegem.ui;

import com.sandpolis.core.net.stream.StreamSink;
import com.sandpolis.core.net.stream.StreamSource;
import com.sandpolis.plugin.desktop.msg.MsgDesktop.EV_DesktopInput;
import com.sandpolis.plugin.desktop.msg.MsgDesktop.EV_DesktopOutput;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class RemoteDesktopView extends ImageView {

	private SimpleDoubleProperty zoomLevel = new SimpleDoubleProperty(1.0);

	private WritableImage image;

	private StreamSource<EV_DesktopInput> source;

	private StreamSink<EV_DesktopOutput> sink;

	public RemoteDesktopView() {
		zoomLevel.addListener(l -> {
			if (getImage() != null) {
				setFitHeight(getImage().getHeight() * zoomLevel.get());
			}
		});

		sink = new StreamSink<>() {

			@Override
			public void onNext(EV_DesktopOutput ev) {

				if (ev.getPixelData().isEmpty()) {

					// Copy an entire block already visible
					var copy = new WritableImage(ev.getWidth(), ev.getHeight());
					copy.getPixelWriter().setPixels(0, 0, ev.getWidth(), ev.getHeight(), image.getPixelReader(),
							ev.getSourceX(), ev.getSourceY());
					image.getPixelWriter().setPixels(ev.getDestX(), ev.getDestY(), ev.getWidth(), ev.getHeight(),
							copy.getPixelReader(), 0, 0);
				} else {

					image.getPixelWriter().setPixels(ev.getDestX(), ev.getDestY(), ev.getWidth(), ev.getHeight(),
							pixelFormat.get(), ev.getPixelData().asReadOnlyByteBuffer(), rawRect.getScanlineStride());
				}
			}
		};

		addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
			source.submit(EV_DesktopInput.newBuilder().build());
		});

		addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			source.submit(EV_DesktopInput.newBuilder().build());
		});

		setImage(image);
	}

}
