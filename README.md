# Nimrod Incanter

[Nimrod](https://github.com/sbtourist/nimrod) metrics analysis with [Incanter](http://incanter.org/).

## Quickstart

Once you have a running Nimrod server ...

Check out the Nimrod-Incanter repository.

Install with:

    $> sh ./install.sh

Start with:

    $> sh ./start.sh
 
Load Nimrod metrics as Incanter datasets, i.e.:

    nimrod.incanter.main=> (with-data 
                             (from-nimrod "http://localhost:7000" (from-log "1" (read-alert "test1" "alert" :no-tag))) 
                               (view (time-series-plot "timestamp" "alert")))

As an alternative, you can put your Nimrod-Incanter code into an external file, let's say "test.clj":

    (with-data 
      (from-nimrod "http://localhost:7000" (from-log "1" (read-alert "test1" "alert" :no-tag))) 
        (view (time-series-plot "timestamp" "alert")))

And load it:

    $> sh ./load.sh test.clj

You may note the ":no-tag" keyword above: it is used to indicate no tags have to be read. 
In order to read tags, they must be formatted as "tag_name:tag_value", i.e. "type:1", and the tag name must be passed to the read function (just one tag is supported right now):

    (with-data 
      (from-nimrod "http://localhost:7000" (from-log "1" (read-alert "character" "alert" "type")))
          ($rollup :count :alert :type))

## License

Copyright (C) 2011 [Sergio Bossa](http://twitter.com/sbtourist)

Distributed under the [Apache Software License](http://www.apache.org/licenses/LICENSE-2.0.html).
