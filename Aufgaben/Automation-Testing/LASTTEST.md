# Übung 3: Lasttest mit Autocannon

Autocannon ist ein HTTP-Lasttestwerkzeug für Node.js. Es passt hier, weil Node schon für Angular vorhanden ist. Installation und Ausführung sind über npm möglich.

## Funktionen
- Anzahl gleichzeitiger Verbindungen, Dauer und HTTP-Pipelining einstellen.
- HTTP-Methoden, Header und Request-Body konfigurieren.
- Requests pro Sekunde, Datendurchsatz und Antwortzeiten mit Perzentilen messen.
- Fehler, Timeouts und Antworten ausserhalb des 2xx-Bereichs zählen.
- Als CLI oder JavaScript-Bibliothek verwenden und Ergebnisse als JSON speichern.

Quelle: [Autocannon-Dokumentation](https://github.com/mcollina/autocannon).

## Aufbau und Ausführung
Backend starten, dann im Aufgabenordner `npm.cmd run test:load` ausführen.
`tests/load.cjs` sendet GET-Anfragen an `http://localhost:8081/students`: je 10 Sekunden mit 1, 20 und 50 Verbindungen. Pipelining ist 1, Timeout 5 Sekunden. Es werden keine Studenten durch den Lasttest angelegt.

## Messung vom 22.09.2026
Windows-Rechner, Java 17, Spring Boot 3.1.2, lokale H2-Datenbank und Lastgenerator auf demselben Rechner. Währenddessen liefen Angular-Kompilierung und Browser-Installation. Kein eigener Warm-up, nur ein Durchlauf je Laststufe.

| Verbindungen | Requests/s (Durchschnitt) | p99 (ms) | Fehler | Timeouts | Nicht-2xx |
| --- | --- | --- | --- | --- | --- |
| 1 | 327,61 | 72 | 0 | 0 | 0 |
| 20 | 1273,70 | 552 | 109 | 0 | 0 |
| 50 | 3660,10 | 116 | 350 | 0 | 0 |

p99 bedeutet: 99 Prozent der gemessenen Antwortzeiten liegen höchstens bei diesem Wert. Rohdaten: `results/load.json`.

## Beobachtung und Grenzen
Mit mehr Verbindungen steigt der Durchsatz, es treten aber auch Fehler auf. Der Test endet deshalb bewusst mit Exitcode 1. Die empfangenen HTTP-Antworten waren 2xx; die Ursache der separat gezählten Fehler wurde nicht genauer untersucht. Ein höherer Durchsatz bedeutet hier also nicht, dass alle Anfragen erfolgreich waren.

Die geringere p99 bei 50 gegenüber 20 Verbindungen beweist keine Verbesserung. JVM-Aufwärmung und konkurrierende Arbeiten beeinflussen diese kurze Messung. Für einen fairen Vergleich müsste man nach dem Warm-up jede Stufe mehrmals ohne andere Arbeiten messen. Das Ergebnis ist keine Aussage über Produktionskapazität.

Autocannon ist für einfache HTTP-Lasttests schnell eingerichtet. Komplexe Benutzerabläufe und die GUI werden damit nicht geprüft; dafür verwenden wir Playwright. Für diese Übung reicht der kleine GET-Lasttest.
