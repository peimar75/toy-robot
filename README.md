Toy Robot

En enkel konsolbaserad simulering av en leksaksrobot som rör sig på en 5×5 bordyta.  
Roboten följer textkommandon från en fil och rapporterar sin slutliga position.

Funktioner
- Kommandon: `PLACE,X,Y,F`, `MOVE`, `LEFT`, `RIGHT`, `REPORT`
- Förhindrar att roboten faller av bordet
- Accepterar flera `PLACE`-kommandon
- Ignorerar ogiltiga eller utomgräns rörelser
- Rent, testbart design med enhetstester (JUnit 5)


📁 PROJEKTSTRUKTUR

toy-robot/
│
├── build.gradle.kts
├── README.md
│
├── src/
│   ├── main/java/se/hiq/
│   │   ├── Main.java
│   │   ├── Robot.java
│   │   ├── TableTop.java
│   │   ├── CommandProcessor.java
│   │   └── Visualizer.java
│   │
│   └── test/java/se/hiq/
│       ├── RobotTest.java
│       ├── CommandProcessorTest.java
│       ├── TableTopTest.java
│       └── IntegrationTest.java
│
└── resources/
    ├── input1.txt
    ├── input2.txt
    └── input3.txt


1️⃣ Bygg projektet

./gradlew clean build

Detta kommando:
•	Kompilerar alla main + test-källor
•	Kör alla JUnit 5-tester
•	Producerar kompilerade klasser under build/classes/java/main

⸻

2️⃣ Kör programmet

```bash
./gradlew run --args="src/main/resources/input3.txt"
```

Förväntad utdata (med visualisering och kommandon):
```
>>> Executed: PLACE,1,2,EAST
>>> Next: MOVE

[ ][ ][ ][ ][ ]  4
[ ][ ][ ][ ][ ]  3
[ ][→][ ][ ][ ]  2
[ ][ ][ ][ ][ ]  1
[ ][ ][ ][ ][ ]  0
 0  1  2  3  4  (X-axis)

Current Position: 1,2,EAST
--------------------------------------

>>> Executed: MOVE
>>> Next: MOVE
...

3,3,NORTH
```

Visualiseringen visar:
- **>>> Executed:** Det kommando som just kördes
- **>>> Next:** Nästa kommando som kommer att köras
- **Rutnät:** Robotens position med riktningspilar (↑↓←→)
- **Current Position:** Nuvarande koordinater och riktning


3️⃣ Kör alla tester

./gradlew test

Exempel på utdata:

> Task :test

ToyRobotSimulator > se.hiq.RobotTest
ToyRobotSimulator > se.hiq.CommandProcessorTest
ToyRobotSimulator > se.hiq.IntegrationTest

BUILD SUCCESSFUL in 2s


⸻

Valfritt: Alternativ manuell körning (utan Gradle Application Plugin)

Om du föredrar att köra manuellt efter byggning:

javac -d build/classes/java/main src/main/java/se/hiq/*.java
java -cp build/classes/java/main:build/resources/main se.hiq.Main src/main/resources/input3.txt

(Motsvarar dina javac/java-kommandon, bara med Gradles utdatasökväg.)

⸻

Sammanfattning

Uppgift	Kommando	Beskrivning
Bygg allt	./gradlew clean build	Kompilerar & kör tester
Kör filläge	./gradlew run --args="src/main/resources/input3.txt"	Kör från fil
Kör endast tester	./gradlew test	Kör alla JUnit 5-tester
Manuell Java-körning (fil)	java -cp build/classes/java/main:build/resources/main se.hiq.Main src/main/resources/input3.txt	Kör kompilerad kod manuellt



KÖRNINGSINSTRUKTIONER

1. Bygg projektet

./gradlew clean build

2. Kör en exempelfil med kommandon

./gradlew run --args="src/main/resources/input1.txt"
./gradlew run --args="src/main/resources/input2.txt"
./gradlew run --args="src/main/resources/input3.txt"

Förväntad utdata (slutresultat):

3,3,NORTH

3️⃣ Kör alla tester

./gradlew test

Utdatasammanfattning:

-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running se.hiq.RobotTest
Running se.hiq.CommandProcessorTest
Running se.hiq.TableTopTest
Running se.hiq.IntegrationTest
Tests run: 30, Failures: 0, Errors: 0, Skipped: 0
-------------------------------------------------------
BUILD SUCCESS


## Köra programmet
Kompilera och kör med terminal:

javac -d out src/main/java/se.hiq/*.java
java -cp out se.hiq.Main src/main/resources/input3.txt

Öppningssammanfattning

"Projektet simulerar en leksaksrobot som rör sig säkert på ett 5×5 rutnät.
Roboten kör kommandon från en textfil — PLACE, MOVE, LEFT, RIGHT, REPORT —
medan den säkerställer att den aldrig faller av bordet.
Jag byggde det med tydlig separation av ansvar, hög testtäckning och en liten visualiseringsmodul för tydlighet."

Arkitekturoversikt

Lösningen är byggd med kärn-Java-klasser:
	•	Main hanterar filinmatning.
	•	CommandProcessor orkestrerar all logik.
	•	Robot kapslar in position och riktning.
	•	TableTop hanterar rumsliga gränser.
	•	Visualizer ritar rutnätet dynamiskt i konsolen.

Klasserna är avsiktligt avkopplade — till exempel vet Robot aldrig om inmatningsparsing eller visualisering 
vilket gör systemet lätt att testa och utöka.



ASCII-arkitektursdiagram

+-------------+
|   Main      | <-- Läser fil, skickar kommandon
+------^------+
      |
      v
+-------------+
| CommandProc | <-- Orkestrerar robot + bord
+--+-------+--+
   |       |
   v       v
+-----+  +-------+
|Robot|  |TableTp|
+--+--+  +-------+
   |
   v
+---------+
|Visualizer|
+---------+




Förklaring av kommandoflöde

Varje rad från filen parsas av CommandProcessor.processCommand().
Innan något giltigt PLACE-kommando ignoreras andra kommandon.
När den är placerad kan roboten röra sig och rotera.

Varje gång roboten ändrar tillstånd anropas Visualizer.render() —
den skriver ut bordet och visar robotens nuvarande koordinater och riktning.


📊 ASCII-sekvensexempel (MOVE-kommando)

Main --> CommandProcessor --> Robot --> TableTop
  |           |                 |
  | "MOVE"    |   beräkna nästa  |
  |            \-> isValid() -> |
  |                 true        |
  |<----------------------------|
  | visualisera tillstånd       |



Designval

Område	        Förklaring
Inkapsling	Robot hanterar rörelselogik; TableTop hanterar gränser.
Testbarhet	    Varje klass testbar i isolering; ingen statisk koppling.
Robusthet	    Ogiltiga kommandon och dåliga data ignoreras säkert.
Utökningsbarhet	Bordstorlek, kommandon, visualisering kan utökas.
Visualisering	Valfritt; kan stängas av eller utökas för GUI.


Diskussionspunkter för förbättringar

Jag designade det på ett sätt som gör att framtida funktioner blir lätta att lägga till:
	•	Variabla rutnätsstorlekar (redan stödda).
	•	Hinder eller begränsade celler (utöka TableTop).
	•	Flera robotar (lagra lista i CommandProcessor).
	•	GUI-visualisering (kan utöka Visualizer eller skapa ny implementation).
	•	Interaktiv konsolinmatning (kan implementeras i framtiden).


Felhantering

Systemet ignorerar ogiltiga inmatningar säkert.
Till exempel har PLACE,7,7,NORTH eller MOVE före ett giltigt PLACE ingen effekt.

Det kastar aldrig undantag till användaren — istället bevarar det giltigt tillstånd tyst.


Testning

Jag använde JUnit 5 för både enhets- och integrationstester.
	•	RobotTest (9 tester): Kontrollerar rörelse, rotation, edge cases och hörn.
	•	CommandProcessorTest (13 tester): Validerar korrekt hantering av kommandon, validering och edge cases.
	•	TableTopTest (5 tester): Testar gränspositioner, utomgränspositioner och olika bordstorlekar.
	•	IntegrationTest (1 test): Reproducerar de officiella uppgiftsexemplen exakt.

**Totalt: 30 tester** som täcker både happy path och edge cases.
Kärnlogiken är fullt täckt; visualisering är avsiktligt utelämnad från tester eftersom den är ren presentation.

Varför denna struktur?

Jag ville ha en ren separation så att varje klass har ett enda ansvar:
	•	Robot = rörelse
	•	TableTop = gränser
	•	CommandProcessor = kontrolllogik
	•	Visualizer = utdata
	•	Main = I/O

Detta speglar också verklig programvarulager — domän, kontroll, presentation.


🧾 ASCII-sekvensdiagram (Fullständigt körexempel)

Fil: input3.txt
----------------
PLACE,1,2,EAST
MOVE
MOVE
LEFT
MOVE
REPORT
----------------

Flöde:

Main
 │
 │ läs rader
 ▼
CommandProcessor
 │
 ├── PLACE -> new Robot(1,2,EAST)
 │             Visualizer.draw()
 │
 ├── MOVE -> Robot.move() -> TableTop.isValid(2,2)
 │             Visualizer.draw()
 │
 ├── MOVE -> Robot.move() -> TableTop.isValid(3,2)
 │             Visualizer.draw()
 │
 ├── LEFT -> Robot.turnLeft()
 │             Visualizer.draw()
 │
 ├── MOVE -> Robot.move() -> TableTop.isValid(3,3)
 │             Visualizer.draw()
 │
 └── REPORT -> print("3,3,NORTH")


Toy Robot Simulator är en liten men välstrukturerad Java-applikation som simulerar en robot som rör sig på ett kvadratiskt rutnät.

Jag fokuserade på ren separation av ansvar:
	•	Robot hanterar sin egen rörelse- och orienteringslogik.
	•	TableTop definierar de tillåtna gränserna.
	•	CommandProcessor tolkar textkommandon och koordinerar mellan dem.
	•	Visualizer skriver ut tillståndet dynamiskt till konsolen.
	•	Main läser en fil och skickar kommandon rad för rad.

Roboten respekterar alltid säkerhetsregler — den faller aldrig av, och ogiltiga kommandon ignoreras säkert.

Jag skrev enhetstester med JUnit 5 för att täcka rörelse, svängning, gränsfall och kommandoparsning.

Systemet är lätt att utöka; du kan skala rutnätsstorleken, lägga till hinder eller till och med flera robotar med minimal förändring av kärnlogiken.

Den ASCII-baserade visualiseringen är bara ett bonuslager för tydlighet under demonstration — den anpassar sig automatiskt till vilken rutnätsstorlek som helst.

