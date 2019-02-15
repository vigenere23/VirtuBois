# VirtuBois Editor

Bienvenue au projet VirtuBois Editor, l'éditeur de cours à bois le plus avancé!

Avant de contribuer, assurez-vous de lire les règles de [contribution](CONTRIBUTION.md)!

## Installation

Voici les étapes à suivre afin de pouvoir contribuer au projet : 

1. Télécharger et installer la dernière version de Java 8 (1.8) venant avec JavaFX.
2. Télécharger et installer la dernière version de IntelliJ IDEA.
3. Cloner le repo.
4. Ouvrir IntelliJ et sélectionner "Ouvrir" (et NON "Créer" ou "Nouveau") en choisissant le dossier du repo.
5. Si demander, choisir le SDK 1.8 comme SDK de Java
6. Créer une "build configuration" (à côté du marteau)
   1. Choisir le template "Application"
   2. Mettre comme classe principale "presentation.Main"
7. Enjoy!


## Création du JAR

Voici comment créer une version compilée du programme. 

1. Ouvrir IntelliJ selon la démarche ci-haut
2. Aller dans "File > Projet Structure"
3. Aller dans "Artefacts"
4. Appuyer sur le "+", puis "JavaFX Application"
5. Choisir "presentation.Main" comme classe principale
6. Choisir "equipe2" comme nom
7. Cocher "Include in project build" si vous voulez que le .jar soit généré à chaque compilation
    1. Si jamais vous ne faite pas ça, aller dans "Build > Build artefacts...", puis cliquer sur "build"
8. Sauvegarder et fermer la fenêtre
