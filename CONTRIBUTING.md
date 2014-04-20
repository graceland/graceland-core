## Semantic Versioning

We're attempting to use [semantic versioning](http://semver.org/).


## Git Flow

We're using a form of Git Flow to manage the repository branches:

- `master` - the released, production quality code. This code should reflect what is available on Maven Central.
- `develop` - the latest, stable version of the code. This code should reflect what is available on Sonatype Snapshot.
- `feature/xyz` - new feature work. More often than not, branches off of `develop`, and merges back into `develop`.


## GitHub Issues

If you run into an issue with the project - including issues with the documentation, not just the code - please create
an issue on GitHub. We're attempting to use GitHub Issues as the primary form of communication between developers and
users.

It's also a great place to look for past issues, which hopefully have useful answers that can be used to help solve
your current problem.


## GitHub Pull Requests

We actively welcome code contributions in the form of pull requests.

When you submit your pull request, please make sure to do the following:

1. Ensure that the code is well documented and tested. Ideally, the code coverage is 80% or higher.
1. Ensure `mvn verify` passes on Travis-CI. This will include `checkstyle`, `findbugs` and unit testing.
1. If you break existing APIs, please call it out in the pull request.
1. Please describe the goal/intention of your code change in the description of the pull request.


## Coding Style

The coding style should be enforced by `checkstyle`. If a style is not programmatically enforced, please make sure to
follow the same style + convention found in the rest of the code base.
