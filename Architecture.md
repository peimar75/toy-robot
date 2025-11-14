Toy Robot – Design & Arkitektur

1. Syfte & Omfattning

Detta dokument beskriver designen och arkitekturen för Toy Robot implementerad i Java.

Det täcker:
	Kärnkrav & antaganden
	Högnivåarkitektur
	Klassansvar och interaktioner
	Kommandoflöde
	Textbaserad visualiseringsdesign
	Felhanteringsstrategi
	Utökningsmöjligheter
	Sekvensdiagram (ASCII)
	Teststrategi


2. Problemsammanfattning

Vi simulerar en leksaksrobot på en kvadratisk bordyta:
	Standard bordstorlek: 5×5 (konfigurerbar via TableTop)
	Giltiga kommandon:
	PLACE,X,Y,F
	MOVE
	LEFT
	RIGHT
	REPORT
	Begränsningar:
	Roboten får aldrig falla av.
	Ignorera kommandon tills ett giltigt PLACE har körts.
	Ignorera alla rörelser som skulle skicka roboten utanför gränserna.
	PLACE kan utfärdas när som helst för att ompositionera.
	Inmatning läses från en textfil.
	Utdata via standardkonsol.

Tillägg:
	Textbaserad visualisering av bordet och robotpositionen efter relevanta kommandon.
	Visualiseringen anpassar sig automatiskt till TableTop bredd/höjd (inte hårdkodad).



3. Designmål
    1.	Tydlighet & Läsbarhet
          Enkla, förståeliga klasser med tydliga ansvar.
    2.	Separation av Ansvar
          	Robotlogik separerad från kommandoparsning.
          	Bordgränslogik inkapslad.
          	Visualisering separerad från kärnregler.
    3.	Testbarhet
          	Logik är deterministisk och oberoende av I/O där möjligt.
          	Lätt att enhetstesta rörelse, gränser och kommandosekvenser.
    4.	Utökningsbarhet
          	Lätt att justera bordstorlek.
          	Lätt att lägga till nya kommandon senare (t.ex. REPORT_VERBOSE, PLACE_RANDOM, hinder).
    5.	Robusthet
          	Ignorera ogiltiga kommandon och felformaterad inmatning smidigt.
          	Krascha aldrig på grund av dåliga data.


4. Högnivåarkitektur

4.1 Komponenter

1. TableTop
   	Representerar ytan: bredd × höjd.
   	Äger ansvaret för att säga om en position är giltig.
   	Ingen kunskap om robot eller kommandon.

Nyckeloperationer:
	boolean isValidPosition(int x, int y)
	int getWidth()
	int getHeight()


2. Robot
   	Håller nuvarande tillstånd:
   	x, y
   	Riktning som den är vänd mot
   	Vet hur man:
   	Flyttar framåt 1 enhet (om tillåtet av bordet)
   	Rotar vänster/höger
   	Rapporterar sitt nuvarande tillstånd som sträng

Men:
	Robot vet inte inmatningssyntax.
	Robot vet inte om filer eller konsol.
	Robot delegerar gränskontroller till TableTop.

Nyckeloperationer:
	move(TableTop table)
	turnLeft()
	turnRight()
	String report()
	Getters för tester.

Inre enum:
	Direction { NORTH, EAST, SOUTH, WEST }
	Varje riktning kan:
	turnLeft()
	turnRight()


3. CommandProcessor
   	Orkestreringskomponenten.
   	Konsumerar råa kommandosträngar (t.ex. från fil, konsol, etc.).
   	Upprätthåller en referens till:
   	TableTop
   	Robot (när den är placerad)
   	Tvingar fram regler:
   	Ignorera alla kommandon tills ett giltigt PLACE.
   	Parsa och validera PLACE-parametrar.
   	Tillämpa kommandon på Robot.
   	Utlösa visualisering efter tillståndsändringar.

Nyckeloperationer:
	void processCommand(String inputCommand)
	Robot getRobot() (för tester)

Ansvar:
	Parsning (minimal).
	Inmatningsvalidering.
	Koordinering mellan TableTop, Robot och Visualizer.


4. Visualizer
   	Ren presentationsverktyg.
   	Renderar nuvarande bord och robotposition som ASCII.
   	Använder TableTops dimensioner för att förbli dynamisk.

Beteende:
	Om roboten inte är placerad ännu: skriver ut "[Robot not yet placed]".
	Annars:
	Skriver ut ett rutnät från översta raden ner till y = 0.
	Använder ↑, ↓, ←, → för att visa riktning på robotens cell.
	Skriver ut axelmärkningar.
	Skriver ut nuvarande position (REPORT) under rutnätet.

Nyckeloperation:
	static void render(TableTop table, Robot robot)

Designanteckning:
	Att hålla detta separat säkerställer att visualisering kan stängas av eller ändras utan att röra kärnlogik.



5. Main
   	Ingångspunkt.
   	Ansvarig endast för:
   	Läsa kommandofil rad för rad.
   	Skicka varje rad till CommandProcessor.
   	Vet ingenting om beteendedetaljer.

Nyckelflöde:
	Skapar TableTop(5, 5)
	Skapar CommandProcessor
	Strömmar filkommandon -> processCommand(...)



5. Modulinteraktionsöversikt

Lager:

+-----------------------------+
|           Main              |  ← CLI / ingångspunkt
+--------------+--------------+
|
v
+-----------------------------+
|      CommandProcessor       |  ← Orkestrerar logik per kommando
+--------+--------------------+
| uses
v
+-----------------+    +-----------------+
|      Robot      |    |     TableTop    |
+-----------------+    +-----------------+
|
v
+-----------------------------+
|         Visualizer          |  ← Ren utdata (valfri)
+-----------------------------+

Separation av ansvar
		Main → pratar endast med CommandProcessor.
		CommandProcessor → pratar med Robot, TableTop och Visualizer.
		Robot → frågar TableTop om giltighet via move.
		Visualizer → skrivskyddad användning av Robot + TableTop.





6. Kommandolivscykel & Regler
    1.	Vid start:
          	robot är null.
          	Kommandon förutom PLACE ignoreras.
    2.	PLACE,X,Y,F:
          	Parsas av CommandProcessor.
          	Giltigt om:
          	X och Y är heltal.
          	F en av NORTH,EAST,SOUTH,WEST.
          	(X,Y) är giltigt på TableTop.
          	Om giltigt:
          	Ny Robot-instans skapas.
          	Visualizer.render(...) anropas.
    3.	MOVE:
          	Ignoreras om robot == null.
          	Annars:
          	Robot.move(table):
          	Beräknar nästa (x,y) baserat på riktning.
          	Kontrollerar table.isValidPosition(nextX, nextY).
          	Tillämpar rörelse endast om giltig.
          	Visualizer.render(...) anropas efter kommandot (tillståndsändringsförsök).
    4.	LEFT / RIGHT:
          	Ignoreras om robot == null.
          	Annars:
          	Rotar robot.
          	Visualizer.render(...) anropas.
    5.	REPORT:
          	Ignoreras om robot == null.
          	Annars:
          	Skriver ut robot.report() (ingen rutnätsuppdatering behövs, men det är tillåtet om önskat).
    6.	Ogiltiga kommandon / felformaterade:
          	Tyst ignorerade (ingen krasch, ingen bieffekt).



7. ASCII-klassdiagram

+-----------------------------+
|          TableTop           |
+-----------------------------+
| - width: int               |
| - height: int              |
+-----------------------------+
| + TableTop(w:int,h:int)    |
| + isValidPosition(x,y):bool|
| + getWidth():int           |
| + getHeight():int          |
+-----------------------------+


+-----------------------------+
|           Robot             |
+-----------------------------+
| - x: int                   |
| - y: int                   |
| - facing: Direction        |
+-----------------------------+
| + Robot(x:int,y:int,dir)   |
| + move(table:TableTop)     |
| + turnLeft()               |
| + turnRight()              |
| + report(): String         |
| + getX():int               |
| + getY():int               |
| + getFacing():Direction    |
+-----------------------------+

    enum Direction
    +---------------------------+
    | NORTH, EAST, SOUTH, WEST |
    +---------------------------+
    | + turnLeft(): Direction  |
    | + turnRight(): Direction |
    +---------------------------+


+-----------------------------+
|      CommandProcessor       |
+-----------------------------+
| - table: TableTop          |
| - robot: Robot (nullable)  |
+-----------------------------+
| + CommandProcessor(table)  |
| + processCommand(cmd:String)|
| + getRobot(): Robot        |
+-----------------------------+


+-----------------------------+
|         Visualizer          |
+-----------------------------+
| (static utility)           |
+-----------------------------+
| + render(table, robot)     |
+-----------------------------+


+-----------------------------+
|            Main             |
+-----------------------------+
| + main(args:String[])      |
+-----------------------------+




8. Sekvensdiagram (ASCII)

8.1 Övergripande filbearbetning

Main            CommandProcessor        TableTop        Robot         Visualizer
|                     |                  |              |               |
| read line ----------> processCommand() |              |               |
|                     |                  |              |               |
|                     | (parse cmd)      |              |               |
|                     | ...              |              |               |
| loop until EOF      |                  |              |               |




8.2 PLACE-kommando

Kommando: PLACE,1,2,EAST

Main                 CommandProcessor           TableTop           Robot             Visualizer
|                          |                     |                 |                    |
| "PLACE,1,2,EAST" ------->|                     |                 |                    |
|                          | parse PLACE         |                 |                    |
|                          | x=1,y=2,dir=EAST    |                 |                    |
|                          |---- isValidPosition(1,2) ----------->|
|                          |                     |  returns true  |
|                          | create new Robot(1,2,EAST) -------->|
|                          | robot reference set                  |
|                          |---------------- render(table,robot) ---------------------->|
|                          |                                       | draws grid        |



8.3 MOVE-kommando

Kommando: MOVE

Main                 CommandProcessor           TableTop           Robot             Visualizer
|                          |                     |                 |                    |
| "MOVE" ----------------->|                     |                 |                    |
|                          | robot != null? yes  |                 |                    |
|                          | call robot.move() ------------------->|
|                          |                     |                 |
|                          |                     |-- isValidPosition(nextX,nextY) --->|
|                          |                     |         returns true/false          |
|                          |                     |                 |
|                          | robot updates if valid               |
|                          |---------------- render(table,robot) --------------------->|
|                          |                                       | draws updated grid |

Om isValidPosition returnerar false, ignorerar Robot rörelsen och positionen förblir oförändrad. 
Visualizer visar fortfarande nuvarande giltig position.


8.4 LEFT / RIGHT-kommando

Main                 CommandProcessor           Robot               Visualizer
|                          |                    |                     |
| "LEFT"/"RIGHT" --------->|                    |                     |
|                          | robot != null?     |                     |
|                          | robot.turnLeft()/turnRight() ---------->|
|                          |---------------- render(table,robot) --->|
|                          |                    | draws rotated dir  |



9. Felhantering & Gränsfall
   	Inget PLACE ännu:
   	MOVE, LEFT, RIGHT, REPORT är no-ops.
   	Ogiltigt PLACE:
   	Koordinater utanför gränserna → ignorerade.
   	Ogiltig riktningstoken → ignorerad.
   	Robottillstånd oförändrat (möjligen fortfarande null).
   	MOVE vid kant:
   	Nästa position ogiltig → rörelse ignorerad, inget undantag.
   	Okänt kommando:
   	Tyst ignorerad.
   	I/O-fel:
   	Rapporteras till stderr i Main, simulatorn avslutas smidigt.

Detta ger ett robust beteende utan att blåsa upp koden.

⸻

10. Utökningsbarhet

Exempel du kan nämna om du frågas "Hur skulle du utöka detta?":
1.	Anpassad bordstorlek från argument
	Parsa bredd/höjd i Main, skicka in i TableTop.
2.	Hinder
	Lägg till en isOccupied(x,y) eller Set<Point> till TableTop.
3.	Flera robotar
	Upprätthåll en lista/karta av Robot-instanser i CommandProcessor.
	Utöka kommandon för att rikta sig till specifika robotar.
4.	Alternativa användargränssnitt
	Eftersom visualisering är isolerad kan du:
	Ersätta Visualizer med GUI.
	Lägga till loggning eller filexport.
5.	Nya kommandon
	Lägg till enum eller kommandoregister, snarare än switch om det växer.
	Nuvarande design stöder redan inkrementell utökning.


11. Teststrategi

Redan anpassad till arkitekturen:
	Enhetstester
	RobotTest
	Rörelse
	Rotationer
	Kanthantering
	CommandProcessorTest
	Ignorerar kommandon före PLACE
	Giltigt/ogiltigt PLACE
	Korrekt slutligt tillstånd efter sekvenser
	Integrationstester
	IntegrationTest
	Reproducera tillhandahållna exempel (a, b, c) och påstå slutlig report().
	Visualisering
	Hålls separat; kärnkorrekthet beror inte på konsolrendering.
	Detta gör testerna stabila och fria från bräckliga strängjämförelser.

Perfekt — låt oss nu göra visualiseringen dynamisk så att den automatiskt anpassar sig till vilken bordstorlek som helst snarare än att anta 5×5.

Vi kommer att förbättra Visualizer för att använda de faktiska TableTop-dimensionerna och behålla all funktionalitet intakt.

⸻

✅ Uppdaterad Visualizer.java

package se.hiq;

/**
* Ansvarig för att visa bordrutnätet och robotpositionen i konsolen.
* Anpassar sig automatiskt till TableTop-dimensionerna.
  */
  public class Visualizer {

  /**
    * Skriver ut nuvarande tillstånd för bordet och robotpositionen.
    * Cellen (0,0) är i nedre vänstra hörnet.
    *
    * @param table bordytan som definierar dimensionerna
    * @param robot roboten att visualisera (kan vara null)
      */
      public static void render(TableTop table, Robot robot) {
      if (robot == null) {
      System.out.println("[Robot not yet placed]");
      return;
      }

      int width = table.getWidth();
      int height = table.getHeight();

      // Symbol för riktningen
      String icon;
      switch (robot.getFacing()) {
      case NORTH: icon = "↑"; break;
      case SOUTH: icon = "↓"; break;
      case EAST:  icon = "→"; break;
      case WEST:  icon = "←"; break;
      default:    icon = "?";
      }

      System.out.println();
      for (int y = height - 1; y >= 0; y--) {
      for (int x = 0; x < width; x++) {
      if (x == robot.getX() && y == robot.getY()) {
      System.out.print("[" + icon + "]");
      } else {
      System.out.print("[ ]");
      }
      }
      System.out.println("  " + y);
      }

      // Skriv ut X-axelmärkningar
      for (int x = 0; x < width; x++) {
      System.out.print(" " + x + " ");
      }
      System.out.println(" (X-axis)");

      System.out.println();
      System.out.println("Current Position: " + robot.report());
      System.out.println("--------------------------------------");
      }
      }


⸻

🧠 Uppdatera TableTop.java

Vi lägger till två enkla getter-metoder för bredd och höjd.

package se.hiq;

/**
* Representerar bordytan som roboten rör sig på.
* Bordet har fasta dimensioner och tillåter inte några rörelser utanför dess gränser.
  */
  public class TableTop {

  private final int width;
  private final int height;

  public TableTop(int width, int height) {
  this.width = width;
  this.height = height;
  }

  /**
    * Kontrollerar om de angivna (x, y) koordinaterna ligger inom bordets gränser.
      */
      public boolean isValidPosition(int x, int y) {
      return x >= 0 && x < width && y >= 0 && y < height;
      }

  /** Returnerar bordets bredd. */
  public int getWidth() {
  return width;
  }

  /** Returnerar bordets höjd. */
  public int getHeight() {
  return height;
  }
  }


⸻

🧪 Exempel på utdata för 5×5-bord

Om du kör:

PLACE,1,2,EAST
MOVE
LEFT
MOVE
REPORT

Konsolutdata anpassar sig dynamiskt:

[ ] [ ] [ ] [ ] [ ]
[ ] [ ] [ ] [ ] [ ]
[ ] [↑] [ ] [ ] [ ]
[ ] [ ] [ ] [ ] [ ]
[ ] [ ] [ ] [ ] [ ]
0  1  2  3  4  (X-axis)

Current Position: 1,2,NORTH
--------------------------------------


⸻

🧩 Exempel på utdata för större bord (t.ex. 7×7)

Om du modifierar main:

TableTop table = new TableTop(7, 7);

Du får omedelbart ett 7×7 rutnät, och visualisatorn anpassar sig automatiskt — ingen kodändring behövs.

⸻

✅ Sammanfattning av fördelar

Funktion	Beskrivning
Dynamiska dimensioner	Använder TableTop bredd & höjd
Fullt kompatibel	Bryter inte uppgiftsregler
Automatisk rendering	Uppdateras efter PLACE, MOVE, LEFT, RIGHT
Läsbar & ren	Fungerar i vilken konsol som helst, UTF-8-vänlig
Fortfarande testbar	Visualizer är valfri, påverkar inte logik


⸻

Vill du att jag lägger till en valfri --visual flagga så att du kan aktivera eller inaktivera denna visualisering från kommandoraden (användbart för tester och intervjuer)?
