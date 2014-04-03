################
Metrics Graphite
################

.. rubric:: The ``graceland-metrics-graphite`` module provides a metrics reporting factory that
            automatically uses the hostname in the prefix.


Host-Aware Graphite Reporter
============================

Reports metrics periodically to Graphite. The prefix provided may include a variable ``%s`` that will be replaced with
the current machine's host name. This is so each instance of the service will have a different prefix, letting you
differentiate each machine's activity, while still letting you roll them up using Graphite.


Caveats
-------

- If no hostname can be determined, the variable is removed and any double dots ``..`` will be replaced with single
  dots.
- All non-alphanumeric characters (including dots) in the hostname will be replaced with an underscore ``_`` to ensure
  graphite does not break down the hostname variable.


Configuration
-------------

Extends the attributes that are available to the graphite reporter. See the
`graphite reporter documentation <http://dropwizard.github.io/dropwizard/manual/configuration.html#graphite-reporter>`_
for more details.

.. code-block:: yaml

    metrics:
        reporters:
            - type: graphite-hostaware
              host: localhost
              port: 8080
              prefix: <prefix>


====================== ===============  ================================================================================
Name                   Default          Description
====================== ===============  ================================================================================
host                   localhost        The hostname of the Graphite server to report to.
port                   8080             The port of the Graphite server to report to.
prefix                 (none)           The prefix for Metric key names to report to Graphite.
====================== ===============  ================================================================================
