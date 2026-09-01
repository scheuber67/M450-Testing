# Aufgabe 3 – Banken-Simulation

- `Bank` verwaltet alle Konten.
- `Account` ist die Basisklasse für alle Kontotypen.
- Jedes Konto hat eine Nummer, einen Saldo und mehrere Buchungen.
- `SavingsAccount` darf nicht ins Minus gehen.
- `SalaryAccount` darf bis zur Kreditlimite ins Minus gehen.
- `PromoYouthSavingsAccount` gibt bei Einzahlungen 1 % Bonus.
- `Booking` speichert Datum und Betrag einer Buchung.
- Die Comparator-Klassen sortieren die Konten nach Saldo.
- `BankUtils` formatiert Datum und Geldbeträge.

## Tests

```powershell
cd Aufgaben/Aufgabe3/02_bank-vorgabe/02_bank-vorgabe
mvn test
```

Der Coverage-Bericht wird unter `target/site/jacoco/index.html` erstellt.

Resultat: 6 Tests erfolgreich, ca. 96 % Line Coverage.
