
# Project Factual

Don't worry, I'll come up with a new name eventually.

## Setup

Packages that need to be installed:

* JDK 1.7+
* boot 2.6
* nodejs
* electron-prebuilt (via npm)

## Development mode

Dev mode:

```
boot dev-build
```

This starts a nrepl server on 9001, connect to it from whatever you might be using. Type `(start-repl)` in the repl to start the repl.

In a separate terminal window:

```
electron target/
```

The `target/` is important. Don't omit the slash.

If you edit the cljs files, it should automatically reload and show up in the electron window

## Release build

This is not production ready yet. Anything can happen when you run `boot prod-build`, including setting yourself on fire. Don't be on fire.