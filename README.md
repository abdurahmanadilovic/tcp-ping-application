# TCP ping

To run the app with gradle type

```sh
gradle assemble
java -jar build/libs/tcpPing-1.0.jar
```

To run the app without gradle (packaged binary beforehand)
```sh
java -jar build/libs/tcpPing-1.0.jar
```

To run tests

```sh
gradle test
```

# App structure

MainApplication -> PitcherApplication / CatcherServer

PitcherApplication -> PitcherSender / PitcherReceiver -> Pitcher

CatcherServer -> CatcherApplication -> CatcherSender / CatcherReceiver -> Catcher
