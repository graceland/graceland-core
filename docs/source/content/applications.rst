############
Applications
############

.. rubric:: The purpose of the Application is to manage the list of plugins that define your
            application. The two primary pieces of functionality are ``loadPlugin`` and
            ``getPlugins``.


Simple Applications
===================

A simple plugin provides the most basic implementation of an application. It provides a
``configure()`` method so you can define the plugins to load.


Modal Applications
==================

You may want an application to end up having different modes of operation - loading different
plugins while in the staging environment vs. the production environment. It would be ideal for
swapping out implementations that are either not present or not deployed in a given environment.

If you want different modes, you can extend the ``io.graceland.platform.application.ModalApplication``
class, and use your own enum to define the different modes.

.. note:: You'll need to provide a default mode for the application in the constructor. Choosing a
          production-ready mode would make deploying to a production environment that much less
          complicated.

Instead of using the ``configure()`` method, you will use the ``configureFor(Enum<?> Mode)``
so you can load determine what to do based on the mode.

.. code-block:: java

    public class SampleModalApplication extends ModalApplication<SampleModes> {

        protected SampleModalApplication(String[] args) {
            // it helps to pass in a reference to the class, and the default
            // mode of the application
            super(SampleModes.class, SampleModes.PROD, args);
        }

        @Override
        protected void configureFor(SampleModes mode) {

            // load plugins that will be used in all modes
            loadPlugin(new SamplePlugin());

            switch (mode) {
                case DEV:
                    // do dev stuff
                    loadPlugin(new SampleDevPlugin());

                case PROD:
                    // do prod stuff
                    loadPlugin(new SampleProdPlugin());
            }
        }
    }


Choosing a Mode
---------------

To tell your application what mode to run in, add a command line argument with a leading
double-dash and the mode enum name. For example: ``--DEV`` will match up to the enum value ``DEV``.

If no value is passed in, the default mode is used.

.. note:: The mode passed in the command line is case-sensitive.

Custom Applications
===================

If the provided applications do not provide the functionality needed, you can extend them or
write your own implementation of the ``io.graceland.platform.application.Application`` interface.
