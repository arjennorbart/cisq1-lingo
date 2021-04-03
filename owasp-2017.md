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
We maken gebruik van een boel externe dependencies in ons project. Belangrijk is om te zorgen om altijd up to date te 
zijn. Hiervoor gebruiken we Dependabot.

## A2:2017 Broken Authentication

### Description

### Risk

### Counter-measures

## A3:2017 Sensitive Data Exposure

### Description

### Risk

### Counter-measures

