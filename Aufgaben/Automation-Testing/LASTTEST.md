# Übung 3: Lasttest mit Autocannon

## Was wurde gemacht?
Das Backend wurde mit vielen automatischen Anfragen getestet. Dafür wurde Autocannon verwendet, eine Alternative zu Postman oder JMeter.

Das Skript `tests/load.cjs` ruft die Studentenliste mit `GET http://localhost:8081/students` immer wieder ab. Es laufen nacheinander drei Tests mit 1, 20 und 50 gleichzeitigen Verbindungen. Jeder Test dauert 10 Sekunden. Jede Verbindung wartet auf die Antwort und schickt danach die nächste Anfrage. Es werden keine Studenten angelegt.

## Was kann das Tool?
- Viele Anfragen gleichzeitig senden.
- Anzahl Verbindungen und Testdauer einstellen.
- Anfragen pro Sekunde und Antwortzeiten messen.
- Fehler und Timeouts zählen.

Unser Skript zeigt die wichtigsten Werte und konkrete Fehlermeldungen im Terminal. Alle Messwerte werden in `results/load.json` gespeichert.

## Test starten
Das Backend muss laufen. Im Ordner `Aufgaben/Automation-Testing` ausführen:

```powershell
npm.cmd run test:load
```

Nach ungefähr 30 Sekunden sind alle drei Durchläufe fertig. Das Frontend wird nicht benötigt.

## Ergebnisse des letzten Laufs

| Verbindungen | Anfragen pro Sekunde | p99 in ms | Fehler |
| --- | ---: | ---: | ---: |
| 1 | 883,2 | 105 | 0 |
| 20 | 5711,2 | 98 | 560 |
| 50 | 5708,4 | 142 | 550 |

p99 bedeutet: 99 Prozent der gemessenen Antwortzeiten waren höchstens so lang.

## Was bedeuten die Fehler?
Bei 20 und 50 Verbindungen wurde `ECONNRESET: read ECONNRESET` gemeldet. Das bedeutet, dass Verbindungen unerwartet abgebrochen wurden. Dabei kam keine vollständige HTTP-Antwort zurück. Die genaue Ursache wurde nicht abschliessend untersucht.

`0 Nicht-2xx` bedeutet, dass keine empfangene HTTP-Antwort einen Status ausserhalb von 200 bis 299 hatte. Das widerspricht den Verbindungsfehlern nicht, da bei diesen keine vollständige Antwort vorlag. Bei Fehlern beendet unser Skript den Lauf mit Exitcode 1; die Ergebnisse werden trotzdem gespeichert.

## Fazit
Von 1 auf 20 Verbindungen steigt die Zahl der Anfragen pro Sekunde stark. Mit 50 Verbindungen werden kaum mehr Anfragen verarbeitet und die p99-Antwortzeit steigt. Unter höherer Last treten Verbindungsabbrüche auf. Das beweist allein noch keine Überlastung des Backends.

Für die Übung zeigt der Test, wie sich mehr gleichzeitige Anfragen auswirken. Die kurzen lokalen Messungen reichen nicht aus, um die maximale Belastbarkeit der Anwendung sicher zu bestimmen.

Quelle: [Autocannon-Dokumentation](https://github.com/mcollina/autocannon).
