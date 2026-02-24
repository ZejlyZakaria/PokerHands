# PokerHands

## Présentation

Ce projet consiste à implémenter un évaluateur de mains de poker en Java.
L’objectif final est de comparer deux mains et de déterminer le gagnant selon les règles classiques du poker.

Le développement est réalisé de manière progressive, avec des commits incrémentaux, afin de mettre en évidence les choix de conception et l’évolution de l’architecture.

---

## Étape 1 – Modélisation d’une carte

Cette première étape consiste à mettre en place le modèle de base représentant une carte de poker.

### Enum Rank

L’enum `Rank` représente la valeur d’une carte (de 2 à As).

Chaque constante contient :
- Un symbole (ex : '2', 'T', 'A')
- Une valeur numérique (2 à 14) utilisée pour les comparaisons entre cartes

Exemple :
- ACE('A', 14)

Une méthode statique `Rank.from(char symbol)` permet de convertir un caractère en la valeur correspondante.

Cela permet de centraliser la logique de conversion et d’éviter l’utilisation de structures conditionnelles répétitives (`switch`, `if` multiples).

---

### Enum Suit

L’enum `Suit` représente l’enseigne  d’une carte :

- C : Clubs (Trèfle)
- D : Diamonds (Carreau)
- H : Hearts (Cœur)
- S : Spades (Pique)

Une méthode statique `Suit.from(char symbol)` permet également de convertir un caractère en valeur d’énumération correspondante.

---

### Classe Card

La classe `Card` représente une carte définie par :

- Un `Rank`
- Un `Suit`

La classe est immuable :
- Les attributs sont déclarés `final`
- Aucun setter n’est exposé
- Une carte ne peut pas être modifiée après sa création

Une méthode factory `Card.from(String representation)` permet de créer une carte à partir d’une représentation textuelle.

Exemples :

- "AS" → As de Pique
- "2H" → Deux de Cœur

Des validations défensives sont mises en place :
- La représentation ne peut pas être `null`
- Elle doit contenir exactement 2 caractères
- Le rang et la couleur doivent être valides, sinon une `IllegalArgumentException` est levée

---

## Étape 2 – Modélisation d’une main

Cette étape consiste à modéliser une main de poker composée de cinq cartes.

### Classe Hand

La classe `Hand` représente une main composée exactement de 5 cartes.

Des validations sont mises en place :
- La liste de cartes ne peut pas être `null`
- Une main doit contenir exactement 5 cartes

La classe est immuable :
- La liste interne est copiée via `List.copyOf`
- Aucune modification n’est possible après création

Une méthode factory `Hand.from(String line)` permet de créer une main à partir d’une représentation textuelle.

Exemple :

- "AS KH 2D 3C 4S"

Une méthode `sorted()` permet de retourner une nouvelle instance de `Hand` dont les cartes sont triées par valeur décroissante.  
Cette méthode prépare le terrain pour la future logique d’évaluation des combinaisons.

---

## Étape 3 – Catégorisation des mains

Cette étape introduit la notion de catégorie de main.

### Enum HandCategory

L’enum `HandCategory` représente les différentes combinaisons possibles au poker :

- HIGH_CARD
- PAIR
- TWO_PAIRS
- THREE_OF_A_KIND
- STRAIGHT
- FLUSH
- FULL_HOUSE
- FOUR_OF_A_KIND
- STRAIGHT_FLUSH

Chaque catégorie contient :

- Un niveau de force numérique permettant de comparer les combinaisons entre elles
- Un nom d’affichage (`displayName`) utilisé pour la présentation du résultat

Le niveau de force permet de déterminer facilement quelle main est supérieure à une autre sans dépendre de l’ordre de déclaration de l’enum.

Cette enum prépare la mise en place de la logique d’évaluation des mains.


---

---

## Étape 4 – Résultat d’évaluation

Cette étape introduit la classe représentant le résultat produit après l’analyse d’une main.

### Classe HandResult

La classe `HandResult` encapsule :

- La catégorie de la main (`HandCategory`)
- Une description textuelle destinée à l’affichage
- Une liste de valeurs numériques utilisées pour départager deux mains de même catégorie

La liste des valeurs (tie-breakers) est ordonnée du critère le plus important au moins important.

Exemples :

- Four of a Kind (Jacks avec kicker 9) → [11, 9]
- Full House (Ace over King) → [14, 13]
- Two Pairs (9 et 7 avec kicker As) → [9, 7, 14]

La classe est immuable : une fois créé, un résultat ne peut pas être modifié.

---

## Étape 5 – Évaluation des combinaisons

Cette étape introduit la classe responsable de l’analyse des mains.

### Classe HandEvaluator

La classe `HandEvaluator` contient la logique permettant de déterminer la meilleure combinaison possible pour une main de cinq cartes.

Les combinaisons sont évaluées de la plus forte à la plus faible :

- STRAIGHT_FLUSH
- FOUR_OF_A_KIND
- FULL_HOUSE
- FLUSH
- STRAIGHT
- THREE_OF_A_KIND
- TWO_PAIRS
- PAIR
- HIGH_CARD

Le cas particulier du straight A-2-3-4-5 est correctement pris en compte.

Les descriptions sont générées dynamiquement, par exemple :

- Two pair : 9 over 7
- Straight : Ace high
- Flush : King high
- Four of a Kind : Jacks

Cette organisation sépare clairement la logique d’évaluation de la représentation du résultat.

---

## Étape 6 – Comparaison entre deux mains

Une fois les deux mains évaluées (Black et White), la comparaison se fait via deux méthodes statiques dans `HandResult` :

- `compare(a, b)` : retourne un entier (1 si a gagne, -1 si b gagne, 0 si égalité)
    - Compare d’abord la catégorie (via `getStrength()`)
    - En cas d’égalité de catégorie, compare les tie-breakers position par position (du plus important au moins important)

- `whoWins(black, white)` : retourne directement le message final attendu : 
  • "Black wins. - with [description]" (ex. : "Black wins. - with full house: 4 over 2")  
  • "White wins. - with [description]" (ex. : "White wins. - with high card: Ace")  
  • "Tie."

---

## Étape 7 – Parsing de l’entrée et traitement complet

Cette étape finalise le flux complet de l’application en introduisant le parsing des lignes d’entrée.

### Classe InputParser

La classe `InputParser` est responsable de transformer une ligne d’entrée brute (format du sujet) en résultat final.

Méthode principale : `processLine(String line)`

Fonctionnement :

1. Validation de base : ligne non vide et non nulle
2. Split de la ligne sur les espaces
3. Vérification du format strict : "Black:" + 5 cartes + "White:" + 5 cartes
4. Détection des doublons globaux (entre les deux mains) via un `HashSet<String>`
5. Extraction des cartes Black et White sous forme de chaînes
6. Création des deux objets `Hand` via `Hand.from(String)`
7. Évaluation des deux mains avec `HandEvaluator.evaluate()`
8. Comparaison via `HandResult.whoWins()` et retour du message final

Gestion des erreurs :
- Format invalide → "Invalid format"
- Carte invalide → "Invalid card: [détail]"
- Doublon → "Invalid input: duplicate card [carte]"

Exemple d’entrée : Black: 2H 3D 5S 9C KD White: 2C 3H 4S 8C AH

Sortie : White wins - with high card: Ace

Cette classe relie toutes les briques précédentes (modèle, évaluation, comparaison) et produit exactement le résultat attendu.

---

## Tests unitaires

Les tests sont écrits avec JUnit 5.

La classe HandEvaluatorTest couvre :

- Évaluation des différentes catégories (Pair, Two Pairs, etc.)
- Cas particuliers (Straight A-2-3-4-5)
- Comparaison entre deux mains
- Cas d’égalité
- Gestion des erreurs

Les tests permettent de valider la logique métier indépendamment de l’interface d’entrée.

--- 

## Comment lancer le programme

1. Compilez et exécutez la classe `Main.java` (ou via `mvn exec:java`)
2. Choisissez un mode :
  - `1` : saisie manuelle interactive
  - `2` : lecture d’un fichier)
3. Pour le mode fichier, placez un fichier texte à la racine du projet (ex: input.txt) avec une ligne par partie.

Exemple de fichier input.txt :

Black: 2H 3D 5S 9C KD White: 2C 3H 4S 8C AH

Black: 2H 4S 4C 2D 4H White: 2S 8S AS QS 3S