# Todo

- Spec 4K resolution in viewPlane in DSL 

- https://github.com/kotest/kotest
- Logging
    - https://www.baeldung.com/kotlin-logging
    - https://github.com/MicroUtils/kotlin-logging

- Lenses needed?



- World definition ohne Camera und Viewplane. Dieses muss in eine Extra-Def.
- Konvertierung
    - Bisherige Welten einlesen und Welt mit saveAsJSON() speichern
    - Teilweise wird aber gerechnet in der Welt-Konfiguration

- Viewplane
    - Gamma und ColorCorrection gehören da nicht rein
    - maxDepth Rekursionstiefe ist das nicht eine Eigenschaft des Renderers
    - Eigene Klasse für ColorCorrection+Gamma

- World
    - Sollte den Zustand der Welt darstellen, statisch
    - Wiederverwendbar
    - Unabhängig von Kamera, Renderer, etc.
    - Nur Objekte + Licht
    - ViewPlane, Tracer und Camera gehören da nicht hinein

- Viewplane und Tracer sieht ja statisch, während die Kamera evtl. nicht statisch ist, sondern herumfährt
- Evtl. ist alles statisch bis auf die Welt


Performance des Grids
World 39

NUM = 10
boolean hasShadows = false

2010-05-14 22:44:56,797 INFO  [main] net.dinkla.raytracer.objects.Gridount: numCells =9261, numZeroes = 1261, numOnes = 8000, numTwos = 0, numThrees = 0, numGreater = 0
2010-05-14 22:44:56,799 INFO  [main] net.dinkla.raytracer.worlds.WorldBuilderount: World Builder took 1170 ms
2010-05-14 22:44:59,832 INFO  [main] net.dinkla.raytracer.cameras.Pinholeount: rendering took 2853 ms

NUM = 20
boolean hasShadows = false

2010-05-14 22:45:16,968 INFO  [main] net.dinkla.raytracer.objects.Gridount: numCells =68921, numZeroes = 4921, numOnes = 64000, numTwos = 0, numThrees = 0, numGreater = 0
2010-05-14 22:45:16,970 INFO  [main] net.dinkla.raytracer.worlds.WorldBuilderount: World Builder took 2399 ms
2010-05-14 22:45:19,820 INFO  [main] net.dinkla.raytracer.cameras.Pinholeount: rendering took 2679 ms

NUM = 30
boolean hasShadows = false

2010-05-14 22:46:17,551 INFO  [main] net.dinkla.raytracer.objects.Gridount: numCells =226981, numZeroes = 10981, numOnes = 216000, numTwos = 0, numThrees = 0, numGreater = 0
2010-05-14 22:46:17,553 INFO  [main] net.dinkla.raytracer.worlds.WorldBuilderount: World Builder took 4082 ms
2010-05-14 22:46:20,436 INFO  [main] net.dinkla.raytracer.cameras.Pinholeount: rendering took 2707 ms

NUM = 40
boolean hasShadows = false

2010-05-14 22:45:49,306 INFO  [main] net.dinkla.raytracer.objects.Gridount: numCells =531441, numZeroes = 19441, numOnes = 512000, numTwos = 0, numThrees = 0, numGreater = 0
2010-05-14 22:45:49,308 INFO  [main] net.dinkla.raytracer.worlds.WorldBuilderount: World Builder took 6480 ms
2010-05-14 22:45:52,119 INFO  [main] net.dinkla.raytracer.cameras.Pinholeount: rendering took 2638 ms

NUM = 100
boolean hasShadows = false

2010-05-14 22:48:07,950 INFO  [main] net.dinkla.raytracer.objects.Gridount: numCells =8120601, numZeroes = 120601, numOnes = 8000000, numTwos = 0, numThrees = 0, numGreater = 0
2010-05-14 22:48:07,952 INFO  [main] net.dinkla.raytracer.worlds.WorldBuilderount: World Builder took 72882 ms
2010-05-14 22:48:11,469 INFO  [main] net.dinkla.raytracer.cameras.Pinholeount: rendering took 3349 ms

NUM = 10
boolean hasShadows = true

2010-05-14 23:02:42,251 INFO  [main] net.dinkla.raytracer.objects.Gridount: numCells =9261, numZeroes = 1261, numOnes = 8000, numTwos = 0, numThrees = 0, numGreater = 0
2010-05-14 23:02:42,253 INFO  [main] net.dinkla.raytracer.worlds.WorldBuilderount: World Builder took 1176 ms
2010-05-14 23:02:45,339 INFO  [main] net.dinkla.raytracer.cameras.Pinholeount: rendering took 2907 ms

NUM = 20
boolean hasShadows = true

2010-05-14 23:03:09,764 INFO  [main] net.dinkla.raytracer.objects.Gridount: numCells =68921, numZeroes = 4921, numOnes = 64000, numTwos = 0, numThrees = 0, numGreater = 0
2010-05-14 23:03:09,766 INFO  [main] net.dinkla.raytracer.worlds.WorldBuilderount: World Builder took 2476 ms
2010-05-14 23:03:12,829 INFO  [main] net.dinkla.raytracer.cameras.Pinholeount: rendering took 2892 ms

NUM = 30
boolean hasShadows = true

2010-05-14 23:03:34,067 INFO  [main] net.dinkla.raytracer.objects.Gridount: numCells =226981, numZeroes = 10981, numOnes = 216000, numTwos = 0, numThrees = 0, numGreater = 0
2010-05-14 23:03:34,069 INFO  [main] net.dinkla.raytracer.worlds.WorldBuilderount: World Builder took 4068 ms
2010-05-14 23:03:37,326 INFO  [main] net.dinkla.raytracer.cameras.Pinholeount: rendering took 3086 ms

NUM = 40
boolean hasShadows = true

2010-05-14 23:04:08,864 INFO  [main] net.dinkla.raytracer.objects.Gridount: numCells =531441, numZeroes = 19441, numOnes = 512000, numTwos = 0, numThrees = 0, numGreater = 0
2010-05-14 23:04:08,865 INFO  [main] net.dinkla.raytracer.worlds.WorldBuilderount: World Builder took 6391 ms
2010-05-14 23:04:12,029 INFO  [main] net.dinkla.raytracer.cameras.Pinholeount: rendering took 2991 ms


## Old

- World-Description-Language momentan Groovy, würde XML oder JSON gehen?

