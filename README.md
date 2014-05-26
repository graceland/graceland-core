graceland-core
==============


#### Build Status

- `master` [![Build Status](https://travis-ci.org/graceland/graceland-core.png?branch=master)](https://travis-ci.org/graceland/graceland-core)
- `develop` [![Build Status](https://travis-ci.org/graceland/graceland-core.png?branch=develop)](https://travis-ci.org/graceland/graceland-core)


About
-----

_**NOTE:** Graceland is currently pre 1.0.0, and therefore the APIs can change between releases. We'll do our best to
maximize the benefit of each change, but until we hit 1.0.0, there are no garuntees from version to verison._

Graceland lets you easily build a dropwizard application using Guice to inject everything, everywhere.

Graceland introduces the concept of Plugins (which are pretty much Guice `Module`s) so you can compose an application
easily and modularly. To read more about plugins, see the [plugin documentation](http://docs.graceland.io/en/develop/content/plugins.html)
section.


Depending On It
---------------

Graceland is currently publishing to both the Sonatype Snapshot Central Repository and Maven Central.

The versions currently available are:

- Stable: `0.1.0` - available on Maven Central
- Development: `0.2.0-SNAPSHOT` - available on Sonatype Snapshot

Maven Instructions:

    <!-- This is only necessary if you plan on using a SNAPSHOT version -->
    <repositories>
        <repository>
            <id>Sonatype Snapshots</id>
            <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>io.graceland</groupId>
            <artifactId>graceland-platform</artifactId>
            <version>${graceland.version}</version>
        </dependency>
    <dependencies>

Gradle Instructions:


    repositories {
        mavenCentral()

        // This is only necessary if you plan on using a SNAPSHOT version
        maven {
            url "https://oss.sonatype.org/content/repositories/snapshots/"
        }
    }

    dependencies {
        compile "io.graceland:graceland-platform:$gracelandVersion"
    }

Documentation
-------------

The documentation can be found on the [graceland-core read the docs](http://graceland-core.readthedocs.org).


More Info
---------

Read more information about dropwizard at [dropwizard.io](http://www.dropwizard.io).
