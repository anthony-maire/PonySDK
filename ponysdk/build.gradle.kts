plugins {
    //id 'jacoco'
    java
    `maven-publish`
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

group = "com.ponysdk"
version = "3.0.0"

configurations.create("gwtdev")

dependencies {
    implementation("org.gwtproject:gwt-user:2.10.0")
    implementation("com.google.elemental2:elemental2-core:1.2.1")
    implementation("com.google.elemental2:elemental2-dom:1.2.1")
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("org.eclipse.jetty:jetty-server:12.0.3")
    implementation("org.eclipse.jetty.websocket:jetty-websocket-jetty-server:12.0.3")
    //implementation("org.eclipse.jetty.websocket:jetty-websocket-core-server:12.0.3")
    implementation("org.eclipse.jetty.websocket:jetty-websocket-jetty-api:12.0.3")
    //implementation("org.eclipse.jetty.ee10.websocket:jetty-ee10-websocket-jakarta-server:12.0.3")
    implementation("org.eclipse.jetty.ee10.websocket:jetty-ee10-websocket-jetty-server:12.0.3")
    implementation("org.eclipse.jetty.ee10:jetty-ee10-servlet:12.0.3")
    implementation("jakarta.json:jakarta.json-api:2.1.2")

    add("gwtdev", "org.gwtproject:gwt-user:2.10.0")
    add("gwtdev", "org.gwtproject:gwt-dev:2.10.0")
    add("gwtdev", "com.google.elemental2:elemental2-core:1.2.1")
    add("gwtdev", "com.google.elemental2:elemental2-dom:1.2.1")
}

val gwtOutputDirName = "${layout.buildDirectory.get().asFile}/gwt"

tasks.register<JavaExec>("gwtc") {
    description = "GWT compile to JavaScript (production mode)"
    mainClass.set("com.google.gwt.dev.Compiler")
    workingDir = File(gwtOutputDirName)
    maxHeapSize = "512M"

    inputs.dir("src/main/java/com/ponysdk/core/terminal")
    outputs.dir("$gwtOutputDirName/gwt/ponyterminal")

    classpath(
            sourceSets.main.get().java.srcDirs,
            configurations["gwtdev"]
    )

    args = listOf(
            "-war",
            "gwt",
            "-localWorkers",
            "${Runtime.getRuntime().availableProcessors()}",
            "com.ponysdk.core.PonyTerminal",
            "-generateJsInteropExports"
    )
}


tasks.named<Jar>("jar") {
    dependsOn("gwtc")

    into("ponyterminal") {
        from("$gwtOutputDirName/gwt/ponyterminal")
    }
    exclude("*.devmode.js")
    exclude("*compilation-mappings.txt")
    exclude("com/ponysdk/core/terminal/**")
}

//version = "2.8.92" +  project.hasProperty("BUILD_RELEASE") ? "" : "-SNAPSHOT"

/**
ext {
resourcesCoreTest = "src/test/resources"
gwtOutputDirName = buildDir.name + "/gwt"
jsinteropVersion = '1.0.2'
glassfishVersion = '1.1.4'
javaxValidationVersion = '2.0.1.Final'
seleniumVersion = '3.14.0'
junitVersion = '4.12'
tyrusVersion = '1.15'
mockitoVersion = '2.24.0'
gwtVersion = '2.10.0'
elemental2Version = '1.2.1'
jettyVersion = '9.4.43.v20210629'
springVersion = '5.1.3.RELEASE'
javaxServletVersion = '4.0.1'
slf4jVersion = '2.0.0-alpha1'
log4jVersion = '2.10.0'
jsonVersion = '2.1.2'
}





publishing {
publications {
maven(MavenPublication) {
artifactId = 'ponysdk'
from components.java
}
}

repositories {
maven {
name = "GitHubPackages"
url = uri("https://maven.pkg.github.com/smartTrade-OpenSource/PonySDK")
credentials {
username = System.getenv("GITHUB_ACTOR")
password = System.getenv("GITHUB_TOKEN")
}
}
}
}

/**
jacocoTestReport {
reports {
xml.enabled true
xml.destination file("${buildDir}/reports/jacoco/report.xml")
html.enabled false
csv.enabled false
}
}**/

configurations {
gwt
gwtdev
implementation.extendsFrom gwt
}

dependencies {
gwt(
'org.gwtproject:gwt-user:' + gwtVersion,
//'org.gwtproject:gwt-elemental:' + gwtVersion,
'com.google.elemental2:elemental2-dom:' + elemental2Version
)

gwtdev(
'org.gwtproject:gwt-dev:' + gwtVersion
)

implementation(
'javax.validation:validation-api:' + javaxValidationVersion,
'com.google.jsinterop:jsinterop:' + jsinteropVersion,
'com.google.jsinterop:jsinterop-annotations:' + jsinteropVersion,
'org.eclipse.jetty:jetty-server:' + jettyVersion,
'org.eclipse.jetty:jetty-servlet:' + jettyVersion,
'org.eclipse.jetty:jetty-webapp:' + jettyVersion,
'org.eclipse.jetty:jetty-servlets:' + jettyVersion,
'org.eclipse.jetty:jetty-client:' + jettyVersion,
'org.eclipse.jetty:jetty-io:' + jettyVersion,
'org.eclipse.jetty:jetty-util:' + jettyVersion,
'org.eclipse.jetty:jetty-http:' + jettyVersion,
'org.eclipse.jetty:jetty-security:' + jettyVersion,
'org.eclipse.jetty:jetty-continuation:' + jettyVersion,
'org.eclipse.jetty.websocket:websocket-api:' + jettyVersion,
'org.eclipse.jetty.websocket:websocket-common:' + jettyVersion,
'org.eclipse.jetty.websocket:websocket-server:' + jettyVersion,
'org.eclipse.jetty.websocket:websocket-servlet:' + jettyVersion,
'org.eclipse.jetty.websocket:javax-websocket-server-impl:' + jettyVersion,
'org.springframework:spring-core:' + springVersion,
'org.springframework:spring-web:' + springVersion,
'org.springframework:spring-beans:' + springVersion,
'org.springframework:spring-context:' + springVersion,
'org.slf4j:slf4j-api:' + slf4jVersion,
'javax.servlet:javax.servlet-api:' + javaxServletVersion,
'jakarta.json:jakarta.json-api:' + jsonVersion,
'org.glassfish:javax.json:' + glassfishVersion,
'org.seleniumhq.selenium:selenium-api:' + seleniumVersion,
'org.glassfish.tyrus:tyrus-client:' + tyrusVersion,
'org.glassfish.tyrus.ext:tyrus-extension-deflate:' + tyrusVersion,
'junit:junit:' + junitVersion,
'org.mockito:mockito-core:' + mockitoVersion
)

testImplementation(
'org.seleniumhq.selenium:selenium-java:' + seleniumVersion,
'javax.websocket:javax.websocket-client-api:1.1',
'org.jsoup:jsoup:1.14.3',
'org.junit.jupiter:junit-jupiter-api:5.8.1',
'org.junit.jupiter:junit-jupiter-engine:5.8.1',
'org.junit.platform:junit-platform-engine:1.8.1',
'org.junit.platform:junit-platform-launcher:1.8.1'
)

testRuntimeOnly(
'org.slf4j:jcl-over-slf4j:' + slf4jVersion,
'org.slf4j:log4j-over-slf4j:' + slf4jVersion,
'org.slf4j:jul-to-slf4j:' + slf4jVersion,
'org.apache.logging.log4j:log4j-slf4j-impl:' + log4jVersion,
'org.glassfish.tyrus:tyrus-container-grizzly-client:' + tyrusVersion
)
}

jar {
into('ponyterminal') {
from gwtOutputDirName + '/gwt/ponyterminal'
}
exclude('*.devmode.js')
exclude('*compilation-mappings.txt')

manifest {
def cmd = "git rev-parse HEAD"
def proc = cmd.execute()
ext.revision = proc.text.trim()

manifest {
attributes(
'Built-By': System.properties['user.name'],
'Build-Timestamp': new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date()),
'Build-Revision': revision,
'Created-By': "Gradle ${gradle.gradleVersion}",
'Build-Jdk': "${System.properties['java.version']} (${System.properties['java.vendor']} ${System.properties['java.vm.version']})",
'Build-OS': "${System.properties['os.name']} ${System.properties['os.arch']} ${System.properties['os.version']}",
'License-Title': "Apache License 2.0"
)
}
}
}

task gwtc(type: JavaExec) {
inputs.dir('src/main/java/com/ponysdk/core/terminal')
outputs.dir(gwtOutputDirName + '/gwt/ponyterminal')
description = "GWT compile to JavaScript (production mode)"
main = 'com.google.gwt.dev.Compiler'
workingDir = new File(gwtOutputDirName)
classpath { [sourceSets.main.java.srcDirs, configurations.gwtdev, configurations.gwt] }
maxHeapSize = '512M'
args = [
'-war',
'gwt',
'-localWorkers',
Runtime.getRuntime().availableProcessors(),
'com.ponysdk.core.PonyTerminal',
'-generateJsInteropExports'
// Debug Mode
// '-style', 'DETAILED',
// '-optimize', '0'
]
}

test {
classpath = files(resourcesCoreTest, gwtOutputDirName) + classpath
afterSuite { desc, result ->
if (!desc.parent) {
println "Results: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} successes, ${result.failedTestCount} failures, ${result.skippedTestCount} skipped)"
}
}
}

check.dependsOn jacocoTestReport
gwtc.dependsOn classes
jar.dependsOn gwtc
publish.dependsOn check
 **/