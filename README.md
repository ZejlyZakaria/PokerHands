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