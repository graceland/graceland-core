##############
Configurations
##############

.. rubric:: A Graceland system can have more than one configuration. This section of the guide will
            walk you through how to bind these configurations so you can use them in your system.


Binding a Configuration
=======================

The easiest way to explain configurations may be to show an example:

.. code-block:: java

    public class ExamplePlugin extends AbstractPlugin {

        @Override
        protected void configure() {

            // bind the configuration file to the class
            bindConfiguration(ExampleConfiguration.class).toFile("example.yml");
        }
    }

Behind the scenes there is a ``ConfigurationBinder`` that is wiring up the configuration so it is available
in your Guice dependency graph.

There are two main ways of binding configurations:

- ``toInstance`` - an instance of the configuration is provided in the bind statement. This can be
  useful if you know the values to use, or if you're preparing an instance by some other means
  (i.e. loading from a database).

- ``toFile`` - an instance is created by reading the ``YAML`` file provided. It will look for the
  file relative to the current working directory. If there are any parsing issues, Graceland will
  not load and will attempt to provide a helpful error message.
