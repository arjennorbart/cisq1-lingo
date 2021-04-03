# Vulnerability Analysis

## A1:2017 Injection

### Description
Als we specifiek kijken naar SQL Injection, betekend dit dat je een gedeelte van een query kan invoeren 
op een plek waar het eigenlijk niet hoort, zodat je informatie uit de database kan krijgen, 
wat natuurlijk niet de bedoeling is. Het gaat dan fout als je in je code string concatenatie doet en de meegegeven 
String (van de gebruiker) in een query gaat verwerken en het resultaat teruggeeft. 

### Risk
Het risico is laag, omdat ik alleen de findById() gebruik in de JPA repository. Hibernate is sql injection proof.
Als er een custom query geschreven wordt, zorg dan dat je gebruik maakt van de PreparedStatement, 
welke ook sql injection voorkomt.

Authenticatie kan omzeild worden door sql injection. Echter wordt authenticatie en autorisatie afgevangen door 
Spring Security. We maken in dit project geen gebruik van authenticatie en autorisatie, maar mocht dit wel het geval 
zijn kunnen we Spring Security hiervoor gebruiken.

### Counter-measures
We maken gebruik van een boel externe dependencies in ons project. Belangrijk is om te zorgen om altijd up-to-date te 
zijn. Hiervoor gebruiken we Dependabot.

## A2:2017 Broken Authentication

### Description
Broken authentication is wanneer de aanvaller één of meerdere probeert over te nemen, waardoor de aanvaller dezelfde 
privileges heeft als het account die is overgenomen.

### Risk
Omdat we, zoals eerder gezegd, geen gebruik maken van authenticatie is er momenteel geen risico in dit project.


### Counter-measures
Mochten wel gebruik maken van authenticatie, zorgen we voor wachtwoord vereisten 
(minimaal zo lang en verschillende tekens). Tevens gebruiken we absoluut geen default credentials (admin admin). Dit 
moet niet mogelijk zijn om in te stellen.

## A3:2017 Sensitive Data Exposure


### Description
Sensitive data exposure is wanneer gevoelige informatie (wachtwoorden, creditcard gegevens enz.) wordt vrijgegeven, door
bijvoorbeeld een hacker die een systeem binnen komt en de data steelt. Dit is een belangrijk en veelvoorkomend probleem.

### Risk
Omdat we in dit project geen gebruik maken van authenticatie en autorisatie is er momenteel geen risico hiervoor.
Als we dit wel zouden implementeren, kunnen gebruikers inloggen met hun wachtwoord. Dit is uiteraard gevoelige data
waar we zorgvuldig mee om dienen te gaan.

### Counter-measures
Als we met wachtwoorden zouden gaan werken zouden we deze hashen. Niet encrypten, omdat hashing geschikter is voor
wachtwoorden (wachtwoord goedkeuren, zonder het wachtwoord te laten zien).
