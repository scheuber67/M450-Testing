# Grundlagen Softwaretests

## Aufgabe 1

### End-to-End-Test

Bei einem End-to-End-Test wird ein ganzer Ablauf von Anfang bis Ende getestet.

Beispiel: In einem Webshop melden wir uns an, legen ein Produkt in den Warenkorb und schliessen die Bestellung ab.

### Integrationstest

Bei einem Integrationstest wird geprüft, ob mehrere Teile zusammen funktionieren.

Beispiel: Ein Webshop speichert eine Bestellung in der Datenbank. Wir prüfen, ob die Bestellung richtig gespeichert wurde.

### Smoke-Test

Bei einem Smoke-Test wird kurz geprüft, ob die wichtigsten Funktionen des Programms laufen.

Beispiel: Nach einem Update prüfen wir, ob der Webshop startet und ob Login und Warenkorb funktionieren.

## Aufgabe 2

**Beispiel SW-Fehler:**
In einer Preisberechnung wird ein Preis abgezogen, obwohl er addiert werden sollte.

**Beispiel SW-Mangel:**
Ein Programm hat keine Suchfunktion, obwohl der Kunde eine Suchfunktion verlangt hat.

**Beispiel für einen hohen Schaden:**
Ein Fehler in einer Spitalsoftware gibt einem Patienten die falsche Menge eines Medikaments. Dadurch kann der Patient schwer verletzt werden.

## Aufgabe 3

Der Java-Code ist im Ordner `Aufgaben/Aufgabe3`:

- `PriceCalculator.java`: Preisberechnung
- `PriceTestDriver.java`: Testtreiber

Der Testtreiber prüft diese Fälle:

- weniger als 3 Extras: kein Rabatt auf Extras
- 3 Extras: 10 % Rabatt auf Extras
- 5 Extras: 15 % Rabatt auf Extras
- Händler- und Zubehörrabatt werden getrennt berechnet
- alle Werte sind 0

Starten:

```powershell
javac --release 8 -d out Aufgaben/Aufgabe3/PriceCalculator.java Aufgaben/Aufgabe3/PriceTestDriver.java
java -cp out PriceTestDriver
```

## Bonus

Im gegebenen Code gibt es drei Fehler:

1. `extras >= 5` muss vor `extras >= 3` geprüft werden. Sonst werden bei 5 Extras nur 10 % abgezogen.
2. Der Zubehörpreis muss mit `+` addiert werden. Im gegebenen Code steht ein `-`.
3. Der Händlerrabatt darf nicht als Zubehörrabatt verwendet werden. Beide Rabatte gelten für verschiedene Preise.
