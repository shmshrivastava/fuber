# fuber

This project is generated using [Luminus](https://luminusweb.com/) version "3.85"

## Live app

This app is deployed on heroku and is live at https://fuber-shm.herokuapp.com/

## About

This is a clojure based web application for managing cabs and rides.

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein run 

This will run the server at port 3000.

## Database

Edn files are used as temporary data storage.

## API Endpoints

The detailed api documentation can be found at the [swagger endpoint](http://localhost:3000/swagger-ui) once the server is started.

## Home Page

Once the server is running, you can visit the home page at <localhost:3000>
This page contains the list of all available cabs

## Testing

[Midje "1.9.9"](https://github.com/marick/Midje) is used for tests.

Add this to your ~/.lein/profiles.clj file:

```
{:user {:plugins [[lein-midje "3.2.1"]]}}
```

Please see how to setup midje and other information [here](https://github.com/marick/Midje/wiki/A-tutorial-introduction).

Once midje is setup, do
```
lein midje
```
in your development or test environment to run all tests.