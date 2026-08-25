# Übung 3 – Bank-Software

Die Software verwaltet Konten. Man kann Geld einzahlen, abheben und auf ein anderes Konto überweisen.

## Black-Box-Testfälle

| ID | Test | Erwartetes Resultat |
|---|---|---|
| 1 | 100 CHF einzahlen | Der Kontostand steigt um 100 CHF. |
| 2 | 50 CHF abheben | Der Kontostand sinkt um 50 CHF. |
| 3 | Mehr Geld abheben als vorhanden | Die Auszahlung wird abgelehnt. |
| 4 | Geld auf ein anderes Konto überweisen | Der Betrag wird beim ersten Konto abgezogen und beim zweiten Konto hinzugefügt. |
| 5 | Eine falsche Kontonummer eingeben | Eine Fehlermeldung wird angezeigt. |

## White-Box-Testfälle

Diese Methoden könnten direkt getestet werden:

| Methode | Test |
|---|---|
| `Account.deposit()` | Wird der Betrag richtig addiert? |
| `Account.withdraw()` | Wird bei zu wenig Guthaben `false` zurückgegeben? |
| `Bank.createAccount()` | Wird das neue Konto gespeichert? |
| `Bank.getAccount()` | Wird das richtige Konto gefunden? |
| `Counter.transferAmount()` | Wird das Geld bei beiden Konten richtig geändert? |

## Verbesserungen

- Negative Beträge sollten nicht erlaubt sein.
- Für Geld sollte `BigDecimal` statt `double` verwendet werden.
- Der API-Key sollte nicht direkt im Code stehen.
- Benutzereingabe und Banklogik sollten getrennt werden.
