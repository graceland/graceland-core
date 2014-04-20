########
Overview
########

.. rubric:: Graceland is a modular web application framework. It combines some powerful frameworks
            to achieve it's goals. These frameworks include: Dropwizard and Guice.


Motivation
==========

Graceland was written because Dropwizard is amazing, but it's not very easy to build a modular
application with it. There is only a single ``Configuration`` available to a service, so swapping
between different components gets pretty tedious.

Guice is a pretty amazing framework for Dependency Injection, and it makes building modular
applications a heck of a lot easier and managable. There's just one, large, easy to understand
and manage Dependency Graph that is used to build your application.

Combining these two frameworks - Dropwizard and Guice - you can build a pretty functional
framework for quickly building modular, easy to maintain and upgrade web applications.

The goal of Graceland is to stay out of your way and let you develop. Hopefully Graceland you'll
find Graceland useful.


Modules
=======

The ``graceland-core`` project consists of the following sub-modules:

- ``graceland-example`` - An example project that uses as much of graceland as possible.
- ``graceland-metrics-graphite`` - a Metrics Reporter for graphite that uses your host in the prefix.
- ``graceland-platform`` - The Graceland Platform. Use this to build your application with.
