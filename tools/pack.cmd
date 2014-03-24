del /Q ..\animation-packed\*
java -cp gdx.jar;gdx-tools.jar com.badlogic.gdx.tools.imagepacker.TexturePacker2 ..\animation ..\animation-packed
copy /Y ..\animation-packed\* ..\src\aitrinity-desktop\bin\data
