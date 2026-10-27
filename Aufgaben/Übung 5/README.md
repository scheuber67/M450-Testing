# Übung 5 – Unit Tests, Reports und Pipeline

Die originale TBZ-Vorlage liegt in `recipe-planner-fronend-and-backend`.
Die Erweiterungen betreffen das Backend; die React-Oberfläche dient zum Ausprobieren.

## Voraussetzungen und Start

Java 17 (JDK), Maven 3.9 und Node.js mit npm müssen installiert sein.
`java -version` und `mvn -version` müssen das passende JDK verwenden (JAVA_HOME).

Terminal 1, aus diesem Ordner:

```powershell
cd recipe-planner-fronend-and-backend/recipe-planner-backend
mvn spring-boot:run
```

Backend: http://localhost:8080/api/recipes
Die H2-Datenbank liegt im Arbeitsspeicher und wird beim Start mit Beispielrezepten gefüllt.

Terminal 2, aus diesem Ordner:

```powershell
cd recipe-planner-fronend-and-backend/recipe-planner-fronend
npm install
npm start
```

Frontend: http://localhost:3000
Beide Terminals mit Ctrl+C beenden.

## Aufgabe 1: Tests

Im Backend ausführen:

```powershell
mvn clean verify
```

- `RecipeControllerTest`: MockMvc mit isoliertem Controller und gemocktem Service. Drei Tests prüfen die Rezeptliste, ein einzelnes Rezept und das Erstellen eines Rezepts inklusive JSON-Antwort und Weitergabe an den Service.
- `IngredientEntityMapperTest`: Zwei Tests prüfen die Umwandlung zwischen Zutat und Entity in beide Richtungen mit SoftAssertions.
- `RecipeEntityMapperTest`: Zwei Tests prüfen die Umwandlung zwischen Rezept und Entity in beide Richtungen inklusive Zutaten mit SoftAssertions.

Die Mapper-Tests verwenden die realen von MapStruct generierten Implementierungen.
`SoftAssertions.assertSoftly` sammelt fehlgeschlagene Assertions und meldet sie am Ende gemeinsam. Dadurch sieht man mehrere fehlerhafte Felder in einem Testlauf. Ein separates `assertAll()` ist bei dieser Variante nicht nötig.


## Aufgabe 2: Reports

`mvn verify` erstellt automatisch:

- `target/surefire-reports/`: JUnit-XML und Textresultate.
- `target/site/surefire-report.html`: lesbarer HTML-Testbericht.
- `target/site/jacoco/index.html`: HTML-Bericht zur Testabdeckung, inklusive Navigation auf Klassen und Quellcode.

Surefire führt die Tests aus; das Surefire-Report-Plugin erzeugt HTML. JaCoCo misst die Abdeckung.
Die Tests konzentrieren sich auf Controller und Mapper. Die globale Abdeckung schliesst auch andere Klassen ein und ist deshalb nicht als vollständige Abdeckung des Backends zu verstehen.
Falls Tests fehlschlagen, können die vorhandenen Resultate trotzdem aufbereitet werden:

```powershell
mvn surefire-report:report-only jacoco:report
```

## Aufgabe 3: GitHub Actions

Der Workflow liegt im Repository-Hauptverzeichnis unter `.github/workflows/uebung-5.yml`.
GitHub erkennt Workflows nur dort, deshalb befindet sich diese Datei ausserhalb des Übungsordners.

Jeder Push sowie Pull Request startet automatisch:

1. Checkout und Einrichtung von Java 17 mit Maven-Cache.
2. Kompilieren des Backends.
3. Ausführen aller Tests und Erstellen der Reports mit `mvn verify`.
4. Bei Fehlern: Aufbereiten der vorhandenen Testresultate.
5. Hochladen der Reports als Artefakt, auch bei fehlgeschlagenen Tests.

Fehlgeschlagene Tests lassen die Pipeline fehlschlagen. Es gibt kein `continue-on-error`.
Artefakte haben pro Durchlauf und Wiederholung einen eigenen Namen und werden 30 Tage aufbewahrt.
Ein lokaler Commit allein startet auf GitHub nichts; erst der Push überträgt ihn zum Runner.

Nach Commit und Push auf GitHub: Repository → Actions → „Uebung 5 - Backend Build und Tests“ → Durchlauf → Artifacts.
Das Report-ZIP herunterladen, entpacken und `surefire-report.html` bzw. `jacoco/index.html` öffnen.
Mit `workflow_dispatch` kann die Pipeline zusätzlich manuell gestartet werden.

## Der Lehrperson zeigen

- Controller- und Mapper-Tests sowie den Vorteil von SoftAssertions erklären.
- `mvn clean verify` ausführen und die beiden HTML-Reports öffnen.
- Auf GitHub einen durch einen Push gestarteten Lauf und dessen Report-Artefakt zeigen.
- Frontend und Backend starten und ein Rezept ansehen bzw. erstellen.

## Quellen

- Projektvorlage: https://gitlab.com/ch-tbz-it/Stud/m450/m450/-/blob/main/Unterlagen/projects/recipe-planner-fronend-and-backend.zip
- Unterrichtstheorie: https://gitlab.com/ch-tbz-it/Stud/m450/m450/-/blob/main/Unterlagen/ci-cd-pipeline/README.md

## Lokale Überprüfung

Am 27.10.2026 wurden die Tests auf 7 grundlegende Fälle reduziert und erfolgreich ausgeführt: 7 Tests, 0 Fehler, 0 übersprungene Tests.
Der echte Backend-Start sowie GET, POST und anschliessendes GET des neuen Rezepts wurden geprüft.

Auf diesem Rechner ist Java 8 im Standard-PATH. Für den geprüften Lauf wurde deshalb ein
lokales JDK 17 unter `.tools/java` bereitgestellt (durch `.gitignore` ausgeschlossen).
Bei Bedarf im Backend-Terminal vor den Maven-Befehlen setzen:

```powershell
$env:JAVA_HOME = (Resolve-Path '../../.tools/java/jdk-17*').Path
$env:PATH = "$env:JAVA_HOME/bin;$env:PATH"
```

Der GitHub-Lauf kann erst nach dem Push auf GitHub nachgewiesen werden.
Das Frontend wurde mit `npm start` gestartet und liefert HTTP 200 auf Port 3000.
Es kompiliert mit bestehenden ESLint-Warnungen zu unbenutzten Variablen/Imports.
Die Oberfläche wurde funktional nicht verändert; `package-lock.json` fixiert die installierten Abhängigkeiten.


