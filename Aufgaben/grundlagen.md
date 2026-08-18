# Grundlagen Softwaretests

## Aufgabe 1

### Komponententest

Bei einem Komponententest wird ein kleiner Teil des Programms getestet, zum Beispiel eine Methode.

Beispiel: Wir geben der Preisberechnung verschiedene Zahlen und prüfen, ob der richtige Preis zurückkommt.

### Integrationstest

Bei einem Integrationstest wird geprüft, ob mehrere Teile zusammen funktionieren.

Beispiel: Ein Webshop speichert eine Bestellung in der Datenbank. Wir prüfen, ob die Bestellung richtig gespeichert wurde.

### Systemtest

Bei einem Systemtest wird das ganze Programm getestet.

Beispiel: Wir testen in einem Webshop den ganzen Ablauf vom Login bis zur Bestellung.

## Aufgabe 2

**Beispiel SW-Fehler:**
In einer Preisberechnung wird ein Preis abgezogen, obwohl er addiert werden sollte.

**Beispiel SW-Mangel:**
Ein Programm hat keine Suchfunktion, obwohl der Kunde eine Suchfunktion verlangt hat.

**Beispiel für einen hohen Schaden:**
Ein Fehler in einer Spitalsoftware gibt einem Patienten die falsche Menge eines Medikaments. Dadurch kann der Patient schwer verletzt werden.

## Aufgabe 3

Der Java-Code ist im Ordner `src/ch/m450/price`.

Der Testtreiber prüft diese Fälle:

- weniger als 3 Extras: kein Rabatt auf Extras
- 3 Extras: 10 % Rabatt auf Extras
- 5 Extras: 15 % Rabatt auf Extras
- Händler- und Zubehörrabatt werden getrennt berechnet
- alle Werte sind 0

Starten:

```powershell
javac --release 8 -d out src/ch/m450/price/PriceCalculator.java src/ch/m450/price/PriceTestDriver.java
java -cp out ch.m450.price.PriceTestDriver
```

## Bonus

Im gegebenen Code gibt es drei Fehler:

1. `extras >= 5` muss vor `extras >= 3` geprüft werden. Sonst werden bei 5 Extras nur 10 % abgezogen.
2. Der Zubehörpreis muss mit `+` addiert werden. Im gegebenen Code steht ein `-`.
3. Der Händlerrabatt darf nicht als Zubehörrabatt verwendet werden. Beide Rabatte gelten für verschiedene Preise.
