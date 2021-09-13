//============================================================================//
//                                                                            //
//                         Copyright © 2015 Sandpolis                         //
//                                                                            //
//  This source file is subject to the terms of the Mozilla Public License    //
//  version 2. You may not use this file except in compliance with the MPL    //
//  as published by the Mozilla Foundation.                                   //
//                                                                            //
//============================================================================//

plugins {
	id("java-library")
	id("sandpolis-java")
	id("sandpolis-module")
	id("sandpolis-soi")
}

dependencies {
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.0")

	compileOnly(project.getParent()?.getParent()!!)

	compileOnly("org.openjfx:javafx-base:16:linux")
	compileOnly("org.openjfx:javafx-graphics:16:linux")
	compileOnly("org.openjfx:javafx-controls:16:linux")
}

eclipse {
	project {
		name = "com.sandpolis.plugin.desktop:client:lifegem"
		comment = "com.sandpolis.plugin.desktop:client:lifegem"
	}
}
