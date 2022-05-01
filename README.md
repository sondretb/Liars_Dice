# Liars_Dice 

To correctly run the app you must use an Android 22 Lollipop API or later.
You'll need two emulators to play the game.

It is adviced to have Ndm and NodeJs installed.
For windows: https://docs.microsoft.com/en-us/windows/dev-environment/javascript/nodejs-on-windows
For mac and linux: https://github.com/nvm-sh/nvm


# How to run, and connect to server ##
1. In Liars_dice/server run "npm install" in the terminal.
2. To Start the server run "npm run start:dev"
3. If the server is running correctly there should be a "listening on 3000" message in the terminal.
4. Start the emulator and vertify that the client joined the server. There should be a message in the terminal that the client joined.

# Structure ##
The client side classes are found in app/src/main/java/com/example/liars_dice  
  The activity classes are in this directory  
*  /adapters contains the recyclerViewAdapters classes  
*  /model contains models for the MVC pattern  
*  /api contains the client APIs  

The assets and frontend layouts are found in app/src/main/res  
*  /layouts contains the XLM files for the activities.  

The server side classes are found in server/src  
*  The server instaziation and idcreation classes are in this directory  
*  /apis contains the server APIs  
*  /models contains the classes for the server side models  
*  /services contains the classes with the different server services.  
  



