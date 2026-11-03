# Gutscheinrechner

Kleines Schulprojekt mit Java 17 und Maven. Der Gutscheinrechner summiert Artikelpreise,
prüft Gutscheincodes und berechnet den rabattierten Endbetrag.

## Start

```text
mvn test
mvn verify
```

## GitHub Actions

Die Pipeline liegt unter [`../.github/workflows/projekt.yml`](../.github/workflows/projekt.yml)
und startet bei Pushes, Pull Requests sowie manuell über den Actions-Tab.

1. **Build:** Maven baut die JAR-Datei und speichert sie als Artefakt.
2. **Test:** `mvn verify` führt Unit- und Integrationstests aus. JUnit-XML und der
   vollständige HTML-Testreport werden 30 Tage als Artefakt aufbewahrt, auch bei
   fehlgeschlagenen Tests. Fehlgeschlagene Tests lassen den Job fehlschlagen.
3. **Deploy:** Nach erfolgreichen Tests auf `main` wird der HTML-Testreport über
   GitHub Pages veröffentlicht. Pull Requests veröffentlichen keine Website.

Einmalig im Repository unter **Settings → Pages → Build and deployment → Source**
die Option **GitHub Actions** auswählen. Danach die Workflow-Datei pushen.
Unter **Actions → Projekt - Build, Test und Deploy** sind die Läufe und Artefakte sichtbar.
Der Deploy-Job zeigt den Link zum veröffentlichten Report an.
Ein lokaler Testlauf oder eine lokale Workflow-Datei belegt noch keinen erfolgreichen GitHub-Lauf.

## Teststrategie

Unit-Tests prüfen reine Domänenregeln, Mockito-Tests isolieren den Service vom
`CouponRepository`, und ein Integrationstest verbindet den echten JSON-Repository-Adapter
mit dem Berechnungsservice.

## KI-Deklaration

Bei diesem Schulprojekt wurde KI als unterstützendes Werkzeug für Entwurf, Formulierungen
und Fehlersuche verwendet. Anforderungen, Code, Tests und Testergebnisse wurden geprüft,
verstanden und verantwortet. Nicht verifizierte Punkte werden im Arbeitsprotokoll
ausdrücklich gekennzeichnet.

Für die Korrektur eines Test-Erwartungswerts und die Umstellung auf GitHub Actions
wurde Codex als Agent zum automatischen Bearbeiten von Code, Konfiguration und
Dokumentation verwendet. Die lokale Testausführung wurde mit Codex geprüft;
die Erklärung und Prüfung des Workflows gehören zur Projektarbeit.
