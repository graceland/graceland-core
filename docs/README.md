Graceland Documentation
=======================

We're using a floating `gh-pages` branch for the content of the documentation. See below for simple ways of building,
deploying, and contributing to the documentation.


Building
--------

The easiest way of building the documentation is to run to use the sphinx `Makefile` to produce the HTML:

    make html

This will produce the documentation in the `docs/build/html` directory.


Deploying
---------

The easiest way to deploy the documentation to Github Pages is to check out the `gh-pages` branch in it's own, sibling
folder. An example folder structure:

    .
    ├── graceland-core                      # branch: develop, master, feature/*
    │   ├── docs                            # source of documentation
    │   ├── graceland-metrics-graphite
    │   └── graceland-platform
    │
    └── graceland-core-docs                 # branch: gh-pages
        ├── _sources
        ├── _static
        └── content

Once you have this folder layout, you should be able to run the following commands:

    # get into the docs directory
    cd docs

    # build the HTML
    make html

    # copy the contents into the docs repo
    ./copyHtml.sh

If you don't have the folder structure above, the `copyHtml.sh` will not work.


Contributing
------------

We're using `sphinx` and the `read-the-docs` theme.

For more information, check out the README: [https://github.com/snide/sphinx_rtd_theme](https://github.com/snide/sphinx_rtd_theme)
