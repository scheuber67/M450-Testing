# JUnit Zusammenfassung

JUnit ist ein Test-Framework für Java. Damit können Methoden automatisch getestet werden.

## `@Test`

Mit `@Test` wird eine Methode als Test markiert.

```java
@Test
void addieren() {
    assertEquals(8, calculator.add(5, 3));
}
```

## `@BeforeEach`

Die Methode mit `@BeforeEach` wird vor jedem Test ausgeführt. Damit kann zum Beispiel der Calculator neu erstellt werden.

```java
@BeforeEach
void setUp() {
    calculator = new Calculator();
}
```

## `@AfterEach`

Die Methode mit `@AfterEach` wird nach jedem Test ausgeführt. Sie kann zum Beispiel Testdaten wieder löschen.

## `@BeforeAll` und `@AfterAll`

`@BeforeAll` wird einmal vor allen Tests ausgeführt. `@AfterAll` wird einmal nach allen Tests ausgeführt.

Das ist nützlich, wenn eine Verbindung nur einmal geöffnet und am Schluss wieder geschlossen werden soll.

## Assertions

Assertions vergleichen das erwartete mit dem erhaltenen Resultat.

- `assertEquals`: Prüft, ob zwei Werte gleich sind.
- `assertTrue`: Prüft, ob ein Wert `true` ist.
- `assertFalse`: Prüft, ob ein Wert `false` ist.
- `assertNull`: Prüft, ob ein Wert `null` ist.
- `assertThrows`: Prüft, ob ein Fehler ausgelöst wird.

## `@ParameterizedTest`

Mit einem parametrisierten Test kann der gleiche Test mit verschiedenen Werten ausgeführt werden.

```java
@ParameterizedTest
@ValueSource(ints = {1, 2, 3})
void positiveZahlen(int zahl) {
    assertTrue(zahl > 0);
}
```

## `@Disabled`

Mit `@Disabled` kann ein Test vorübergehend ausgeschaltet werden.

## Tests ausführen

In der Entwicklungsumgebung kann man den Test über den Startknopf neben der Testklasse starten.

Mit Maven werden alle Tests so ausgeführt:

```powershell
mvn test
```

## Referenz

[Offizielle JUnit-5-Dokumentation](https://docs.junit.org/5.14.1/user-guide/index.html)
