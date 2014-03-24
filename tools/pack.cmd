del /Q ..\animation-packed\*
java -cp ..\..\libgdx\gdx.jar;..\..\libgdx\extensions\gdx-tools\gdx-tools.jar com.badlogic.gdx.tools.imagepacker.TexturePacker2 ..\animation ..\animation-packed
copy /Y ..\animation-packed\* ..\src\aitrinity-android\assets\data
