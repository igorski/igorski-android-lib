igorski.nl Android library
==========================

...contains what is says on the tin. A series of utilities to provide a pleasant
experience when creating Android applications ;) The entire library is written in Java and
requires no NDK or JNI magic to get going, as such it is pretty easy in use.

The documentation is, admittedly, sparse but most classes provide annotations and
usage descriptions to help you along the way.

A few keywords describing the out-of-the-box possibilities are:

 * SQLite utilities that imitate a lightweight ORM for CRUD operations
 * Pixel blitting engine that renders pixels onto a canvas, with abstraction
   classes (Sprites) to add logic (and (multi) touch response) to user interactions
 * Sensor and multi touch managers with convenience methods to map individual
   pointers to either Value Objects or Sprite Objects.
 * Convenience utilities for easily spawning alert messages or confirmation windows
   across threads.
 * Utils for math, file manipulation, String manipulation, JSON / XML, threading, etc.
 * Lightweight adaptation of PureMVC (omitting Mediators for a command and model based
   application pattern). See http://www.puremvc.org