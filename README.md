# ChatbotApp Praxisbericht
Diese Chatbot Applikation ist eine Spring Boot Application.
Es ist als Showcase für meinen Praxisbericht an der Provadis Hochschule gedacht.
Es wurde eine Anwendung erstellt, welche mithilfe des Watson Assistant, nicht
nur statische Antworten ausgeben kann, sondern zusätzlich HTTP Anfragen schicken kann 
und Werte aus der HTTP Response auch in die ChatbotAntwort einfügen kann.   

Zusätzlich braucht man noch eine MongoDB Datenbank, welche auf den Standart Port 
mongodb://localhost:27017 eine DB namens Praxisbericht hat. Dort müssen die zwei Collections
BotConfig und Answer importiert werden, welche in diesem Projekt unter "src/main/ressources/databaseCollections"
zu finden sind. Die Collection ChatContext wird bei Laufzeit automatisch erstellt. 
Da dieses Projekt auf Github veröffentlicht ist, möchte ich potentiell n api 
Aufrufe auf meinen WA Bot vermeiden und daher muss in der Collection BotConfig, der Datensatz
wetterBot noch mit dem Usernamen und dem Passwort ergänzt werden. 
Bei der Answer Collection im wetter Datensatz muss in httpAction/request/queryParams/APPID 
der apiKey für die Wetterapi ergänzt werden.
Diese Werte sind in meinem Praxisbericht zu finden. 

Die Datenbank sollte dann so aussehen: 

![db screenshot](./src/main/resources/screenshotsReadme/praxisbericht_db_appearance.PNG?raw=true)

Wenn die Datenbank mit den entsprechenden Werten lokal läuft und man die Anwendung startet, 
sieht man im Browser auf der Adresse localhost:8080 das Chatfenster und kann den Bot nach dem Wetter in 
einer der 25 größten Hauptstädte Deutschlands fragen. Das sollte dann so aussehen:
![db screenshot](./src/main/resources/screenshotsReadme/Chat.PNG?raw=true)

Benötigte Software um die Applikation starten zu können:
1. Java 8
2. Maven (Dependency Management Tool)
3. MongoDB 

