#######
Filters
#######


The Guice Multibinder extension does not garauntee the ordering of the injected values - they use a
``Set<>`` to return the values. Because the ordering of filters is important to their function, a
special ``FilterBinder`` is used to ensure that they are added properly.

.. warning:: If a filter is added directly to the ``Environment`` (i.e. using a ``Configurator``),
             there is no garuauntee that it will be added in the right order. Please make sure
             to use a ``FilterBinder`` for all filters.


FilterBinder
============

A ``FilterBinder`` allows you to set the patterns, priority, and name of the filter.

==============  ==================================  ======================================================
Name            Default                             Description
==============  ==================================  ======================================================
``priority``    ``500``                             Used to determine the sort order of the filter.
``name``        The classes ``getSimpleName()``     The name the filter is registered with.
``pattern``     ``/*``                              The pattern that the filter will be registered with.
==============  ==================================  ======================================================


An Example Binding
==================

The example below does the following:

- adds a filter for the class ``TestFilter``
- sets the priority to 900
- ensures the filter affects the following patterns: ``/test/**`` and ``/other/**``
- sets the name to ``mySpecialName``

.. code-block:: java

    import io.graceland.plugin.AbstractPlugin;

    public class ExamplePlugin extends AbstractPlugin {

        @Override
        protected void configure() {
            FilterBinder
                    .forClass(binder(), TestFilter.class)
                    .withPriority(900)
                    .addPattern("/test/**")
                    .addPattern("/other/**")
                    .withName("mySpecialName")
                    .bind();
        }
    }
