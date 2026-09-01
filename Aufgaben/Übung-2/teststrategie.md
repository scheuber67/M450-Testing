# Teststrategie

## Übung 1

Bei der Verkaufssoftware gibt es verschiedene Rabatte, je nachdem wie teuer das Auto ist.

### Abstrakte Testfälle

| ID | Kaufpreis | Erwarteter Rabatt |
|---|---|---|
| 1 | Preis < 15'000 CHF | 0 % |
| 2 | Preis >= 15'000 CHF und <= 20'000 CHF | 5 % |
| 3 | Preis > 20'000 CHF und < 25'000 CHF | 7 % |
| 4 | Preis >= 25'000 CHF | 8,5 % |

### Konkrete Testfälle

| ID | Eingabe | Erwarteter Rabatt |
|---|---|---|
| 1 | 10'000 CHF | 0 % |
| 2 | 14'999 CHF | 0 % |
| 3 | 15'000 CHF | 5 % |
| 4 | 18'000 CHF | 5 % |
| 5 | 20'000 CHF | 5 % |
| 6 | 20'001 CHF | 7 % |
| 7 | 24'999 CHF | 7 % |
| 8 | 25'000 CHF | 8,5 % |
| 9 | 30'000 CHF | 8,5 % |

Die Grenzwerte sind wichtig, weil dort der Rabatt wechselt. Deshalb habe ich 14'999 CHF und 15'000 CHF getestet.

## Übung 2

Für diese Aufgabe verwende ich die Webseite von SIXT für Mietwagen in Zürich.

Webseite: https://www.sixt.ch/mietwagen/schweiz/zurich/

Bei einem Black-Box-Test testet man nur die Funktionen der Webseite. Man schaut also, ob die Eingaben und Ausgaben richtig funktionieren, ohne zu wissen, wie die Webseite programmiert wurde.

### Testfälle

| ID | Beschreibung | Erwartetes Resultat | Effektives Resultat | Status | Mögliche Ursache |
|---|---|---|---|---|---|
| 1 | Abholort Zürich auswählen | Zürich wird als Abholort übernommen | Noch nicht getestet | Offen | Fehler bei der Ortsauswahl |
| 2 | Abhol- und Rückgabedatum eingeben | Die gewählten Daten werden übernommen | Noch nicht getestet | Offen | Fehler bei der Datumsauswahl |
| 3 | Mit gültigen Angaben nach Autos suchen | Verfügbare Mietwagen werden angezeigt | Noch nicht getestet | Offen | Fehler bei der Suche oder keine Verbindung zum System |
| 4 | Ein verfügbares Fahrzeug auswählen | Das gewählte Fahrzeug mit Preis und Informationen wird angezeigt | Noch nicht getestet | Offen | Fahrzeugdaten werden nicht richtig geladen |
| 5 | Eine Buchung durchführen | Die Buchung wird abgeschlossen und eine Bestätigung wird angezeigt | Noch nicht getestet | Offen | Fehler bei der Buchung oder bei der Verarbeitung der Kundendaten |

### Kurze Erklärung

Diese fünf Testfälle testen die wichtigsten Funktionen der SIXT-Webseite.

Ein Kunde muss einen Abholort und ein Datum auswählen können. Danach muss die Webseite verfügbare Fahrzeuge anzeigen. Anschliessend soll der Kunde ein Fahrzeug auswählen und am Ende eine Buchung durchführen können.

## Übung 3

Die Software verwaltet Konten. Man kann Geld einzahlen, abheben und auf ein anderes Konto überweisen.

### Black-Box-Testfälle

| ID | Test | Erwartetes Resultat |
|---|---|---|
| 1 | 100 CHF einzahlen | Der Kontostand steigt um 100 CHF. |
| 2 | 50 CHF abheben | Der Kontostand sinkt um 50 CHF. |
| 3 | Mehr Geld abheben als vorhanden | Die Auszahlung wird abgelehnt. |
| 4 | Geld auf ein anderes Konto überweisen | Der Betrag wird beim ersten Konto abgezogen und beim zweiten Konto hinzugefügt. |
| 5 | Eine falsche Kontonummer eingeben | Eine Fehlermeldung wird angezeigt. |

### White-Box-Testfälle

Diese Methoden könnten direkt getestet werden:

| Methode | Test |
|---|---|
| `Account.deposit()` | Wird der Betrag richtig addiert? |
| `Account.withdraw()` | Wird bei zu wenig Guthaben `false` zurückgegeben? |
| `Bank.createAccount()` | Wird das neue Konto gespeichert? |
| `Bank.getAccount()` | Wird das richtige Konto gefunden? |
| `Counter.transferAmount()` | Wird das Geld bei beiden Konten richtig geändert? |

### Verbesserungen

- Negative Beträge sollten nicht erlaubt sein.
- Für Geld sollte `BigDecimal` statt `double` verwendet werden.
- Der API-Key sollte nicht direkt im Code stehen.
- Benutzereingabe und Banklogik sollten getrennt werden.

