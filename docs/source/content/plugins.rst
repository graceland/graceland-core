#######
Plugins
#######

.. rubric:: Plugins are the basic building block of a Graceland Application. They let the
            developer build discrete units of functionality that they can load into their app at
            runtime, or easily share between different projects ate compile time.


Why Plugins?
============

There are a lot of advantages to having a modular system.

- **Easier to Understand** - It makes it easier to break up the domain problem and think about what
  is important to solve the problem in front of you.

- **Testable Code** - Writing modular code makes it easier to write testable code. A modular code base
  is less coupled, making testing a lot easier.

- **Swap Out Functionality** - It gives you the ability to swap out components, making it easy to
  change the functionality of your application in a sane and easy to understand way.

- **Develop in Parallel** - Plugins can be developed in parallel, and used by the same application.

Plugins vs. Modules
===================

If you're familiar with Guice, then you're familiar with their concept of a ``Module``. You've
probably used the ``AbstractModule`` to build out your application, since it provides some nice
plumbing for you.

In Graceland, a ``Plugin`` is just like a ``Module``, providing the basic building blocks for your
application. An ``AbstractPlugin`` provides the a lot of helpful functionality for binding your
application's components.


A Sample Plugin
===============

Below is a sample plugin. It binds a resource to be served by Jersey, and a task to the Dropwizard
environment.

.. code-block:: java

    import io.graceland.plugin.AbstractPlugin;

    public class ExamplePlugin extends AbstractPlugin {

        @Override
        protected void configure() {
            // add the resource
            bindJerseyComponent(ExampleResource.class);

            // add the task
            bindTask(ResetTask.class);
        }
    }


Plugin Loaders
==============

You won't always be able to define what plugins to load in your application. If you want to load
plugins dynamically at run time, you'll need a mechanism for doing this. Plugin Loaders make this
easy to do.


Native Plugin Loader
--------------------

The ``NativePluginLoader`` will load all Plugins that it can find using the native, Java
``ServiceLoader``. This means it will look on the class path for the ``META-INF/services``
entries and loading those.

It's a simple ``PluginLoader`` that can be used to scan the class path at runtime and load
plugins dynamically.
