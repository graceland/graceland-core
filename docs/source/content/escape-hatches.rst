##############
Escape Hatches
##############

.. rubric:: Graceland is built on top of the dropwizard platform and tries to wrap up the
            functionality so your plugins don't need to deal directly with it. Because we can't
            begin to pretend that we'll cover all of your use cases, we've provided some escape
            hatches so you could access the low-level dropwizard functionality when needed.


How We Use Dropwizard
=====================

A dropwizard application has two primary phases: ``initialize`` and ``run``. Graceland provides
access to the dropwizard components in each of those phases.

.. code-block:: java

    public class Platform extends io.dropwizard.Application<PlatformConfiguration> {
        @Override
        public void initialize(Bootstrap<PlatformConfiguration> bootstrap) {
            // run the Initializers and other code
        }

        @Override
        public void run(PlatformConfiguration configuration, Environment environment) throws Exception {
            // run the Configurators and other code
        }
    }

.. warning:: Using an escape hatch will cause your code to run outside of the *happy path*. There
             are few guarantees made by the ``Platform`` for running these escape hatches. If there
             is a graceland-provided way of doing something (i.e. adding a resource), you're better
             off using it rather than an escape hatch.

Configurator
============

Configurators are triggered during the ``run`` phase of the dropwizard application. It provides
access to two important low level components:

- ``configuration`` - the application's configuration.
- ``environment`` - an ``io.dropwizard.setup.Environment``, which provides access to the underlying
  frameworks, such as jetty, jersey, and the metrics registry.

Most of the work done in a dropwizard application is done during the ``run`` phase, so the majority
of the custom code will come in the form of a ``Configurator``.

.. code-block:: java

    public class SampleConfigurator implements Configurator {
        @Override
        public void configure(PlatformConfiguration configuration, Environment environment) {
            // example of adding a filter directly to the environment
            environment.servlets().addFilter(...)
        }
    }


Initializer
===========

If you need access to the a ``io.dropwizard.setup.Bootstrap``, you can use an ``Initializer``.
This phase is usually used to add commands and bundles to a dropwizard application.

.. code-block:: java

    public class SampleInitializer implements Initializer {
        @Override
        public void initialize(Bootstrap<PlatformConfiguration> bootstrap) {
            // add a configured command
            bootstrap.addCommand(...);
        }
    }
