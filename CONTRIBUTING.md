## How can I contribute?

* create issues for bugs you find
* create pull requests to fix bigs, add features, clean up code, etc.
* improve the documentation
* chat with us on the freenode IRC server at #open-m3u8

## What can I work on?

We do not yet support the full m3u8 specification! So a great way to help is by adding support for more tags in the specification:

http://tools.ietf.org/html/draft-pantos-http-live-streaming-14

The issues page is another good place to look for ways to contribute.

## Compatibility

This library needs to support Android which means we are limited to Java 7 sans try-with-resources.

## Code Style

* 4 spaces per indent - no tab characters
* mPrefix private members
* opening braces on the same line

## Merging

We use a rebase / cherry-pick strategy to merging code. This is to maintain a legible git history on the master branch. This has a few implications:

* it is best if you rebase your branches onto master to fix merge conflicts instead of merging
* your commits may be squashed, reordered, reworded, or edited when merged
* your pull request will be marked closed instead of merged but will be linked to the closing commit
* your branch will not remain tracked by this repository

## Working with the Code

### com.iheartradio.m3u8 package

Everything not meant to be visible to the public API must be package protected. If a whole class is package protected, then you may mark the fields public since they will still not be visible.

### com.iheartradio.m3u8.data package

The data structures in this package reflect the structure of a playlist based on the specification. They are part of the public API and must be immutable. The `Playlist` is the result of parsing and the input of writing.
