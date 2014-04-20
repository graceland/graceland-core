########
Platform
########


The ``Platform`` replaces the ``io.dropwizard.Application`` in your graceland system. It's the base
for your service, and is what is responsible for wiring everything up.

To start up a ``Platform``, just create one and call the ``start(String[] args)`` method, where the
args are the command line arguments. A good place to put this is in a ``public static void main``
clause. Here's an example:

.. code-block:: java

    public static void main(String[] args) throws Exception {
        Platform
                .forApplication(new ExampleApplication())
                .start(args);
    }


Platform Configuration
======================

The graceland ``Platform`` has a single, platform-wide configuration. This extends the dropwizard
``io.dropwizard.Configuration`` class, and is where the user will set the HTTP ports, logging, etc.

In your graceland system, you may have more than one configuration file, but only one Platform
configuration. For more information on configurations, see the Configuration section.
