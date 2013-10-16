TumorML-DB
==========

A database web app for storing and querying over TumorML documents. Uses BaseX native-XML database server as the back-end.

Built using the Scalatra framework and SBT.

## SBT Build & Run ##

```sh
$ cd TumorML-DB
$ ./sbt
> container:start
> browse
```

If `browse` doesn't launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.

