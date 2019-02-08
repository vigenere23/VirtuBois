# Contribution

Ici sera listé l'ensemble des règles concernant la contribution au projet. 

## Coding

### Propriétés de classes (fields)

Les propriétés de classes, ou "variables membres" ou "class fields", ne devraient jamais contenir de préfix ou suffix quelconque. 

Si besoin est de distinguer les paramètres des variables membres, utiliser "this.".

```java
// NON

public class Person {
  private String _name;
  private LocalDate d_birthDate;
  // ...
}

// OUI

public class Person {
  private String name;
  private String birthDate;
  // ...

  public void setName(String name) {
    this.name = name;
  }
  // ...
}
```

### Paramètres de fonction

Les paramètres de fonction ne devraient jamais inclure de préfix ou suffix quelconque. 

```java
// NON
public Person(String p_name, LocalDate p_birthDate)

// OUI
public Person(String name, LocalDate birthDate) {
  this.name = name;
  this.birthDate = birthDate;
}
```

### ...

## Git

### Commit

* Idéalement, il faudrait commit à chaque petite modification spécifique. Bien qu'il soit parfois difficile de le faire, il est quand même bien de se le rappeler. 
* Toujours mettre un message clair et précis. Pas de "bientot fini", "ajout", etc. Préférer des messages du type "création de FactoryPerson pour faciliter l'instanciation".

### Push

* Essayer de pusher le plus souvent possible, afin que tous puisse être à jour.
* NE JAMAIS push une version instable (contenant des erreurs / non-testée) dans le master. Ce dernier est toujours censé être fonctionnel, du moins à la compilation (et pour les fonctionnalités de base).

### ...