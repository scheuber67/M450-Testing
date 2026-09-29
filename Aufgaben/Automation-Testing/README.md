# Automatisiertes Testing – einfache Übungslösung

Quellcode: [TBZ-ZIP](https://gitlab.com/ch-tbz-it/Stud/m450/m450/-/blob/main/Unterlagen/automation-testing/spring-boot-angular-basic-lw.zip).

## Lokal starten
Voraussetzungen: JDK 17, Maven, Node.js und npm. Maven muss mit Java 17 laufen (`mvn --version`). Auf diesem Rechner ist das bereits eingestellt, obwohl `java -version` Java 8 meldet. Angular 16 stammt aus der Vorlage; der lokale Lauf wird hier mit Node 24 geprüft.

Terminal 1, aus diesem Ordner:
```powershell
cd spring-boot-angular-basic-lw2
mvn spring-boot:run
```
Terminal 2, ebenfalls aus diesem Ordner:
```powershell
cd spring-boot-angular-basic-lw2/src/main/js/my-app
npm.cmd ci
npm.cmd start
```
Frontend: http://localhost:4200/students
Backend: http://localhost:8081/students
Die H2-Datenbank liegt im Arbeitsspeicher. Ein Backend-Neustart setzt die Daten zurück.

Terminal 3, aus diesem Ordner:
```powershell
npm.cmd ci
npx.cmd playwright install chromium
npm.cmd test
```
Beide Anwendungen müssen vor den Tests bereit sein. Die Tests erzeugen eindeutige E-Mail-Adressen. Mangels DELETE-Endpunkt bleiben Teststudenten bis zum nächsten Backend-Neustart bestehen.

## Übung 1: REST testen
Tool: Playwright. Damit lassen sich HTTP-Anfragen senden und Antworten automatisch prüfen ([Dokumentation](https://playwright.dev/docs/api-testing)).

`npm.cmd run test:api` führt drei Tests aus:
- GET /students: Status 200, JSON und vorhandener Beispielstudent.
- POST /students: Student speichern und anschliessend per GET wiederfinden.
- Ungültiges JSON senden: Status 400 erwarten.

Datei: `tests/api.spec.js`. Es wird das echte Backend inklusive H2 angesprochen.

## Übung 2: End-to-End im Browser
Tool: ebenfalls Playwright, mit Chromium.

`npm.cmd run test:e2e` öffnet automatisch einen Browser im Hintergrund. Ein Student wird über das Formular erstellt und nach dem Neuladen in der Liste gesucht.

Für die Vorführung mit sichtbarem Browser: `npm.cmd run test:demo`.
HTML-Bericht öffnen: `npx.cmd playwright show-report`.
Datei: `tests/e2e.spec.js`. Es gibt keine gemockten Backend-Antworten.

## Übung 3: Lasttest
`npm.cmd run test:load` startet drei Durchläufe mit 1, 20 und 50 gleichzeitigen Verbindungen, je 10 Sekunden. Details, Messwerte und Einordnung stehen in LASTTEST.md. Rohdaten werden in `results/load.json` gespeichert.

## Vorführung im Team
Eine Person zeigt die REST-Tests, die andere den Browser- und Lasttest. Danach die Lasttestwerte erklären.

## Geprüfter Stand (22.09.2026)
- `mvn -q package`: erfolgreich, vorhandener Spring-Kontexttest bestanden (1 Test).
- Angular-Entwicklungsbuild: erfolgreich, Anwendung auf Port 4200 erreichbar.
- `npm.cmd test`: Die drei REST-Tests und der E2E-Test zum Erfassen eines Studenten bestanden beim damaligen Lauf. Aktueller Umfang: 4 Tests.
- Erster E2E-Aufruf: Timeout während des Angular-Starts; erneuter Lauf nach fertigem Build erfolgreich.
- Lasttest: ausgeführt, bei höherer Last Fehler; siehe LASTTEST.md. Exitcode 1 meldet diese Fehler korrekt.

Die mitgelieferten Angular-Karma-Tests wurden nicht ausgeführt; die Frontend-Prüfung dieser Lösung erfolgt über den E2E-Test zum Erfassen eines Studenten.

