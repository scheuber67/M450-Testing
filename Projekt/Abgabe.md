# Abgabe: Gutscheinrechner

## Ziel

Der Gutscheinrechner summiert Artikelpreise und berechnet den Endbetrag nach einem
prozentualen Gutschein.

## Technik

- Java 17 und Maven
- `BigDecimal` für Geldbeträge
- injizierte `Clock` für testbare Datumsprüfungen
- JUnit 5 und Mockito
- Gutscheine aus [coupons.json](src/test/resources/coupons.json)

## Aufbau

Der `CouponCalculationService` enthält die Berechnungsregeln. Das Interface
`CouponRepository` trennt die Berechnung vom Datenzugriff. `JsonCouponRepository`
lädt die Gutscheine aus einer JSON-Datei.

## Regeln

- Unbekannte Gutscheincodes werden abgelehnt.
- Der Mindestbestellwert muss erreicht werden.
- Der Gutschein ist am Ablaufdatum noch gültig; ab dem Folgetag nicht mehr.
- Der Rabatt wird prozentual berechnet.
- Der Endbetrag wird kaufmännisch mit `HALF_UP` auf zwei Nachkommastellen gerundet.

## Tests und CI

Es gibt Unit-/Mockito-Tests für den Service und einen Integrationstest mit dem echten
JSON-Repository. Die GitLab-Pipeline führt Build, Tests und die Veröffentlichung des
HTML-Testreports über GitLab Pages aus.

## KI-Deklaration

KI wurde unterstützend für Entwurf, Formulierungen und Fehlersuche verwendet. Code,
Tests und Anforderungen wurden geprüft und verantwortet.

