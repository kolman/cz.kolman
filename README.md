# cz.kolman

Repo for the website [www.kolman.cz](https://www.kolman.cz).

## Overview

This is a playground for testing development of serverless app in ClojureScript. Frontend is
deployed by Netlify, backend runs on AWS Lambda (also a ClojureScript project).

## Setup

To get an interactive development environment run:

    lein figwheel

and open your browser at [localhost:3000](http://localhost:3000/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

Running ring server without figwheel:

    lein ring server

To clean all compiled files:

    lein clean

To create a production build run:

    lein do clean, cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL. 

## License

Copyright © 2018 Daniel Kolman

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
