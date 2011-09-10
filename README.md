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
                             (from-nimrod "http://localhost:7000" (from-log "1" (read-alert "test1" "alert"))) 
                               (view (time-series-plot "timestamp" "alert")))

## License

Copyright (C) 2011 [Sergio Bossa](http://twitter.com/sbtourist)

Distributed under the [Apache Software License](http://www.apache.org/licenses/LICENSE-2.0.html).
