# Gutscheinrechner

Kleines Schulprojekt mit Java 17 und Maven. Der Gutscheinrechner summiert Artikelpreise,
prüft Gutscheincodes und berechnet den rabattierten Endbetrag.

## Start

```text
mvn test
mvn verify
```

Die GitLab-Pipeline besteht aus `build`, `test` und `deploy`. Der Testjob veröffentlicht
JUnit-XML sowie den HTML-Testreport als Artefakte. Auf `main` wird der HTML-Report über
GitLab Pages veröffentlicht, wenn der Testjob erfolgreich war.

## Teststrategie

Unit-Tests prüfen reine Domänenregeln, Mockito-Tests isolieren den Service vom
`CouponRepository`, und ein Integrationstest verbindet den echten JSON-Repository-Adapter
mit dem Berechnungsservice.

## KI-Deklaration

Bei diesem Schulprojekt wurde KI als unterstützendes Werkzeug für Entwurf, Formulierungen
und Fehlersuche verwendet. Anforderungen, Code, Tests und Testergebnisse wurden geprüft,
verstanden und verantwortet. Nicht verifizierte Punkte werden im Arbeitsprotokoll
ausdrücklich gekennzeichnet.
