###############
Getting Started
###############

.. rubric:: A quick guide to getting started using Graceland, using the example of a counting
            machine, and resources and tasks that use a ``CountingMachine`` singleton.

Step 0 - Depending On It
========================

Maven Instructions:

.. code-block:: xml

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
            <version>0.1.0</version>
        </dependency>
    <dependencies>

Gradle Instructions:

.. code-block:: groovy

    repositories {
        maven {
            url "https://oss.sonatype.org/content/repositories/snapshots/"
        }
    }

    dependencies {
        compile “io.graceland:graceland-platform:0.1.0"
    }

Step 1 - Silly Counting Machine
===============================

For this example, we'll create a silly counting machine that will keep count and can be represented
in JSON, like the following:

.. code-block:: json

    {
        "count": 4,
        "timestamp: 1396591214348
    }

You can do this by creating a simple POJO representation of the counter:

.. code-block:: java

    public class Counter {
        private final long count;
        private final DateTime timestamp;

        public Counter(long count, DateTime timestamp) {
            this.count = count;
            this.timestamp = timestamp;
        }

        public long getCount() { return count; }

        public DateTime getTimestamp() { return timestamp; }
    }

And the interface that for the counting machine:

.. code-block:: java

    package io.graceland.example.counting;

    public interface CountingMachine {

        void increment();

        void resetCount();

        Counter getCurrentCount();
    }

This should be enough of a toy example to shed some light on Graceland's plugins.


Step 3 - Wire It Up
===================

Now let's create a simple implementation of the ``CountingMachine``:

.. code-block:: java

    public class SimpleCountingMachine implements CountingMachine {

        private final AtomicLong count = new AtomicLong();

        @Override
        public void increment() { count.incrementAndGet(); }

        @Override
        public void resetCount() { count.set(0); }

        @Override
        public Counter getCurrentCount() {
            return new Counter(count.get(), DateTime.now());
        }
    }

And lets wire it up inside of an ``Plugin``:

.. code-block:: java

    public class SimpleCountingPlugin extends AbstractPlugin {

        @Override
        protected void configure() {
            // hook up the counting machine
            bind(CountingMachine.class).to(SimpleCountingMachine.class).in(Singleton.class);
        }
    }

Whenever we include this ``SimpleCountingPlugin``, we'll be telling Guice to use the
``SimpleCountingMachine`` implementation wherever it needs a ``CountingMachine``.

You can look into the Guice documentation if you need more information.


Step 4 - Resources, Tasks & Plugins
===================================

Now that we have a counting machine, we need to expose it through a RESTful endpoint. We can do
that with the following class. Notice how the ``CountingMachine`` is injected into the constructor.

.. code-block:: java

    @Path("/api/example")
    public class ExampleResource {

        private final CountingMachine countingMachine;

        @Inject
        ExampleResource(CountingMachine countingMachine) {
            this.countingMachine = countingMachine;
        }

        @Timed
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public Counter getCurrentCount() {
            countingMachine.increment();
            return countingMachine.getCurrentCount();
        }
    }

We also want to add a Dropwizard ``Task`` to help us clear the counting machine whenever we want.
We can inject the same ``CountingMachine`` here as well:

.. code-block:: java

    public class ResetTask extends Task {

        private final CountingMachine countingMachine;

        @Inject
        ResetTask(CountingMachine countingMachine) {
            super("reset");
            this.countingMachine = countingMachine;
        }

        @Override
        public void execute(ImmutableMultimap<String, String> stringStringImmutableMultimap, PrintWriter printWriter) throws Exception {
            countingMachine.resetCount();

            printWriter.println("Count Reset!");
            printWriter.flush();
        }
    }

Now let's add the ``Task`` and ``Resource`` to our web service:

.. code-block:: java

    public class ExamplePlugin extends AbstractPlugin {

        @Override
        protected void configure() {
            // add the resource
            bindJerseyComponent(ExampleResource.class);

            // add the task
            bindTask(ResetTask.class);
        }
    }

This should be enough for you to start wiring up a simple application!


Step 5 - Building the Application
=================================

Now we'll need both of those plugins to wire up our application. We can extend the
``SimpleApplication`` for this example, and load the plugins explictly:

.. code-block:: java

    public class ExampleApplication extends SimpleApplication {

        @Override
        protected void configure() {
            loadPlugin(new ExamplePlugin());
            loadPlugin(new SimpleCountingPlugin());
        }
    }

To run the application, we just add a ``public static void main(String[] args)`` method and run it
from our IDE:

.. code-block:: java

    public static void main(String[] args) throws Exception {
        Platform
                .forApplication(new ExampleApplication())
                .start(args);
    }

You'll receive a message, listing the commands available. Create a configuration file
``platform.yml`` to tell graceland where to start up the server:

.. code-block:: yaml

    server:
        applicationConnectors:
            - type: http
              port: 8080

And then re-run the application with the following command line arguments:
``server platform.yml``

.. note:: Make sure the current working directory contains the ``platform.yml`` file.

You should see text similar to the following:

.. code-block:: shell

    INFO  [2014-04-04 06:38:55,065] io.dropwizard.server.ServerFactory: Starting Platform
    INFO  [2014-04-04 06:38:55,127] org.eclipse.jetty.setuid.SetUIDListener: Opened application@24a06fb1{HTTP/1.1}{0.0.0.0:8080}
    INFO  [2014-04-04 06:38:55,128] org.eclipse.jetty.setuid.SetUIDListener: Opened admin@2104e040{HTTP/1.1}{0.0.0.0:8081}
    INFO  [2014-04-04 06:38:55,130] org.eclipse.jetty.server.Server: jetty-9.0.7.v20131107
    INFO  [2014-04-04 06:38:55,225] com.sun.jersey.server.impl.application.WebApplicationImpl: Initiating Jersey application, version 'Jersey: 1.18.1 02/19/2014 03:28 AM'
    INFO  [2014-04-04 06:38:55,291] io.dropwizard.jersey.DropwizardResourceConfig: The following paths were found for the configured resources:

        GET     /api/example (io.graceland.example.ExampleResource)

    INFO  [2014-04-04 06:38:55,505] org.eclipse.jetty.server.handler.ContextHandler: Started i.d.j.MutableServletContextHandler@3ba6d328{/,null,AVAILABLE}
    INFO  [2014-04-04 06:38:55,506] io.dropwizard.setup.AdminEnvironment: tasks =

        POST    /tasks/gc (io.dropwizard.servlets.tasks.GarbageCollectionTask)
        POST    /tasks/reset (io.graceland.example.ResetTask)

    WARN  [2014-04-04 06:38:55,507] io.dropwizard.setup.AdminEnvironment:
    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    !    THIS APPLICATION HAS NO HEALTHCHECKS. THIS MEANS YOU WILL NEVER KNOW      !
    !     IF IT DIES IN PRODUCTION, WHICH MEANS YOU WILL NEVER KNOW IF YOU'RE      !
    !    LETTING YOUR USERS DOWN. YOU SHOULD ADD A HEALTHCHECK FOR EACH OF YOUR    !
    !         APPLICATION'S DEPENDENCIES WHICH FULLY (BUT LIGHTLY) TESTS IT.       !
    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    INFO  [2014-04-04 06:38:55,511] org.eclipse.jetty.server.handler.ContextHandler: Started i.d.j.MutableServletContextHandler@5ae9fa73{/,null,AVAILABLE}
    INFO  [2014-04-04 06:38:55,525] org.eclipse.jetty.server.ServerConnector: Started application@24a06fb1{HTTP/1.1}{0.0.0.0:8080}
    INFO  [2014-04-04 06:38:55,526] org.eclipse.jetty.server.ServerConnector: Started admin@2104e040{HTTP/1.1}{0.0.0.0:8081}

You can finally test it out: http://127.0.0.1:8080/api/example


Step 6 - Add a New Machine
==========================

Now lets extend our application by adding a new ``CountingMachine`` implementation. This time,
we'll use one that uses a configuration file to set itself up.

First, lets make a configuration file, ``starting-up.yml``:

.. code-block:: yaml

    startingOn: 500

Now let's make a configuration class to represent the values as a POJO.

.. code-block:: java

    public class StartingOnConfiguration implements io.graceland.configuration.Configuration {

        private final long startingOn;

        @JsonCreator
        public StartingOnConfiguration(@JsonProperty("startingOn") long startingOn) {
            this.startingOn = startingOn;
        }

        public long getStartingOn() { return startingOn; }
    }

Using the configuration, let's build another ``CountingMachine``. It's very similar to our earlier
version, but this one has a constructor where the ``StartingOnConfiguration`` is injected into.

.. code-block:: java

    public class StartingOnCountingMachine implements CountingMachine {

        private final AtomicLong count;

        @Inject
        StartingOnCountingMachine(StartingOnConfiguration configuration) {
            // use the configuration to get the starting on count
            count = new AtomicLong(configuration.getStartingOn());
        }

        @Override
        public void increment() { count.incrementAndGet(); }

        @Override
        public void resetCount() { count.set(0); }

        @Override
        public Counter getCurrentCount() {
            return new Counter(count.get(), DateTime.now());
        }
    }

and finally the plugin to bind the ``CountingMachine`` and to tell Graceland what file to use for
the configuration.

.. code-block:: java

    public class StartingOnCountingPlugin extends AbstractPlugin {

        @Override
        protected void configure() {
            // hook up the counting machine
            bind(CountingMachine.class).to(StartingOnCountingMachine.class).in(Singleton.class);

            // bind the configuration file to the class
            bindConfiguration(StartingOnConfiguration.class).toFile("starting-on.yml");
        }
    }

.. note:: The configuration files look for the files relative to the current working directory
          (``cwd``). If you're running into trouble finding a configuration file, check to see what the
          ``cwd`` is.

Now let's see how we can bring this new ``CountingMachine`` into our application.


Step 7 - Switch It Up
=====================

We can simply swap out the plugin being loaded!

.. code-block:: java

    @Override
    protected void configure() {
        loadPlugin(new ExamplePlugin());

        // replace the simple with the StartingOn
        // loadPlugin(new SimpleCountingPlugin());
        loadPlugin(new StartingOnCountingPlugin());
    }

And now when you check out the URL, you'll see the counting machine starts at the configured value.
