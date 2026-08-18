# M450 – Softwaretests

## Aufgabe 1: Formen von Tests

### 1. Komponententest

Ein Komponententest prüft eine einzelne, möglichst kleine Komponente, zum Beispiel
eine Methode zur Preisberechnung. Abhängigkeiten zu Datenbanken oder externen
Diensten werden dabei nach Möglichkeit vermieden oder ersetzt. Die Komponente
wird mit festgelegten Eingaben aufgerufen und das tatsächliche Ergebnis mit dem
erwarteten Ergebnis verglichen.

Praxisbeispiel: Für eine Preisberechnung werden normale Werte, Rabattgrenzen und
ungültige Eingaben getestet.

### 2. Integrationstest

Ein Integrationstest prüft das Zusammenspiel mehrerer Komponenten oder Systeme.
Die beteiligten Teile werden gemeinsam gestartet. Anschliessend werden Daten über
ihre echten Schnittstellen ausgetauscht und Resultate sowie Seiteneffekte geprüft.

Praxisbeispiel: Ein Webshop übergibt eine Bestellung an die Datenbank und an den
Zahlungsdienst. Der Test kontrolliert, ob die Bestellung gespeichert und die
Zahlung korrekt verarbeitet wird.

### 3. Systemtest

Ein Systemtest prüft die vollständige Anwendung gegen ihre Anforderungen. Das
System wird in einer möglichst produktionsnahen Testumgebung installiert und aus
Sicht der Benutzerinnen und Benutzer bedient.

Praxisbeispiel: Im Webshop wird ein kompletter Kauf vom Login über den Warenkorb
bis zur Bestellbestätigung durchgeführt.

### 4. Abnahmetest

Beim Abnahmetest prüft die Kundschaft oder Fachabteilung, ob die Software die
vereinbarten Geschäftsanforderungen erfüllt. Dazu werden vorbereitete fachliche
Szenarien mit definierten Akzeptanzkriterien ausgeführt. Das Ergebnis ist die
Freigabe oder eine Liste der noch zu behebenden Abweichungen.

Praxisbeispiel: Die Verkaufsabteilung kontrolliert anhand typischer Fahrzeugkäufe,
ob Preise und Rabatte gemäss Reglement berechnet werden.

## Aufgabe 2: Fehler, Mangel und Schaden

- **SW-Fehler:** In der Preisberechnung steht ein Minus statt eines Plus. Dadurch
  wird der Zubehörpreis vom Gesamtpreis abgezogen.
- **SW-Mangel:** Eine Anwendung erfüllt zwar ihre programmierten Funktionen,
  besitzt aber keine Tastaturbedienung, obwohl Barrierefreiheit als Anforderung
  vereinbart wurde.
- **Beispiel für hohen Schaden:** Ein Fehler in der Steuerungssoftware eines
  medizinischen Bestrahlungsgeräts kann eine zu hohe Strahlendosis auslösen. Dies
  kann schwere Verletzungen oder Todesfälle sowie hohe finanzielle und rechtliche
  Schäden verursachen.

Ein Fehler ist eine falsche Umsetzung in der Software. Ein Mangel liegt vor, wenn
die Software eine vereinbarte oder berechtigterweise erwartete Eigenschaft nicht
erfüllt.

## Aufgabe 3: Preisberechnung und Testtreiber

Die Umsetzung befindet sich unter `src/ch/m450/price`. Der Testtreiber ist bewusst
ein normales Java-Programm und verwendet kein Unit-Test-Framework.

Ausführen (JDK erforderlich):

```powershell
javac --release 8 -d out src/ch/m450/price/PriceCalculator.java src/ch/m450/price/PriceTestDriver.java
java -cp out ch.m450.price.PriceTestDriver
```

Der Treiber testet insbesondere:

- keinen Zubehörrabatt bei weniger als drei Extras,
- die Grenze bei drei Extras (10 %),
- die Grenze bei fünf Extras (15 %),
- die getrennte Anwendung von Händler- und Zubehörrabatt,
- einen Fall ohne Preise und Rabatte.

### Bonus: Fehler im vorgegebenen Programmstück

1. `if (extras >= 3)` steht vor `else if (extras >= 5)`. Bei fünf Extras ist
   bereits die erste Bedingung wahr; 15 % können daher nie gewählt werden. Zuerst
   muss auf `extras >= 5`, danach auf `extras >= 3` geprüft werden.
2. Laut Aufgabenstellung kommt der Zubehörpreis dazu. Vor `extraprice` muss daher
   `+` statt `-` stehen.
3. Der Händlerrabatt gilt nur für den Grundpreis. Die Anweisung, die
   `addonDiscount` durch `discount` ersetzt, vermischt beide Rabatte und muss
   entfernt werden.

Die korrigierte Formel lautet:

```text
Grundpreis × (1 − Händlerrabatt / 100)
+ Sondermodellaufschlag
+ Zubehörpreis × (1 − Zubehörrabatt / 100)
```
