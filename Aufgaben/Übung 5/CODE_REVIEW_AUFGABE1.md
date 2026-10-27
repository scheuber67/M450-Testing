# Code-Review – Aufgabe 1: Rezept hinzufügen

Die Umsetzung liegt im bestehenden Projekt von Übung 5 auf dem Branch `codex/add-recipe`.
Das Selbstreview im Pull Request ist noch vom Schüler durchzuführen.

## Geänderte Dateien

- `AddRecipe.js`: Formulardaten verwalten, Zutaten hinzufügen/entfernen, Rezept per POST senden, Fehler anzeigen und nach Erfolg zur Liste wechseln.
- `AddIngredient.js`: Eingaben einer Zutat an das Rezeptformular zurückmelden; eindeutige Labels pro Zeile.
- `AddRecipe.test.js`: Drei Tests für Speichern, Validierung sowie Fehler mit erneutem Versuch.

Die Dateien liegen unter `recipe-planner-fronend-and-backend/recipe-planner-fronend/src/components`.

## Entscheidungen

- Die bestehenden Backend-Feldnamen `name`, `unit` und `amount` werden verwendet.
- Die lokale `listId` dient nur als React-Schlüssel und wird nicht ans Backend gesendet.
- Mengen sind positive ganze Zahlen, da das Backend für `amount` einen Java-Integer verwendet.
- Name ist Pflicht; Beschreibung und Bildadresse sind optional. Zutaten können entfernt werden, auch alle.
- Während des Speicherns ist das Formular gesperrt; Fehler löschen die Eingaben nicht.
- Backend und bestehende Pipeline wurden nicht verändert.

## Geprüft am 27.10.2026

Im Frontend-Ordner:

```powershell
npm test -- --watchAll=false --runInBand
npm run build
```

- Alle 3 Frontend-Tests erfolgreich.
- Produktionsbuild erfolgreich; bestehende Warnungen zu unbenutzten Imports in App/Browse.
- Bibliotheken melden im Test bestehende React-/Router-Kompatibilitätswarnungen.
- Im Browser ein Rezept „Review-Test Brot“ mit 500 GRAMM Mehl erstellt; Weiterleitung zur Liste und gespeicherte Backend-Daten geprüft.

## Dein Selbstreview im Pull Request

1. Unter „Files changed“ zuerst `AddRecipe.js`, danach `AddIngredient.js` ansehen.
2. Sind Namen verständlich? Gibt es unnötigen oder doppelten Code?
3. Werden die richtigen Felder gesendet? Bleibt `listId` im Frontend?
4. Leeren Namen, unvollständige Zutat und Menge 0 ausprobieren.
5. Mehrere Zutaten erfassen, eine entfernen und speichern.
6. Backend beenden und speichern: Fehlermeldung und Erhalt der Eingaben prüfen.
7. Eigene Beobachtungen als PR-Kommentare notieren und nötige Änderungen vornehmen.

## Eigene Erkenntnisse – nach deinem Review ausfüllen

- Was war gut verständlich?
- Welche konkrete Verbesserung wurde gefunden?
- Was wurde verbessert und erneut geprüft?
- Was wird beim nächsten Feature anders gemacht?

Theorie: https://gitlab.com/ch-tbz-it/Stud/m450/m450/-/tree/main/Unterlagen/code-reviews
