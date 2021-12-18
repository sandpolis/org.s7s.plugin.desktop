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
	kotlin("jvm") version "1.6.0"
	id("com.sandpolis.build.module")
}

import org.gradle.internal.os.OperatingSystem

repositories {
	maven {
		url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
	}
}

dependencies {
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.+")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.+")

	compileOnly(project.getParent()?.getParent()!!)

	findProject(":instance:com.sandpolis.client.lifegem")?.let {
		compileOnly(it)
	} ?: run {
		if (OperatingSystem.current().isMacOsX()) {
			compileOnly("com.sandpolis:client.lifegem:+:macos")
		} else if (OperatingSystem.current().isLinux()) {
			compileOnly("com.sandpolis:client.lifegem:+:linux")
		} else {
			compileOnly("com.sandpolis:client.lifegem:+:windows")
		}
	}
}

eclipse {
	project {
		name = "com.sandpolis.plugin.desktop:client:lifegem"
		comment = "com.sandpolis.plugin.desktop:client:lifegem"
	}
}
