# 🚨 SafetyNet Alerts

## 🧭 Présentation du projet

**SafetyNet Alerts** est une application **backend** développée en **Java 21 ☕** avec **Spring Boot**, dont l’objectif est de fournir des **alertes de sécurité** à partir d’un fichier de données **JSON** (personnes 👨‍👩‍👧‍👦, casernes de pompiers 🚒, dossiers médicaux 🩺).

Ce projet a été réalisé dans le cadre d’un **projet de formation Bac+3 🎓**.

### 🎯 Objectifs principaux

* Exposer une **API REST** conforme aux spécifications
* Charger les données depuis un fichier JSON et **retourner des réponses JSON**
* Fournir des **endpoints CRUD** (POST / PUT / DELETE)
* Implémenter une **persistance JSON réelle** (écriture sur disque) afin que les données survivent au redémarrage 🔄
* Produire des **tests automatisés** (unitaires + web MVC)
* Générer les rapports **Surefire** et **JaCoCo** 📊
* Respecter les bonnes pratiques : **SOLID**, **MVC**, logs structurés, code maintenable

---

## 🛠️ Stack technique

* **Java 21**
* **Spring Boot** 🌱
* **Maven**
* **Jackson** (lecture / écriture JSON)
* **JUnit 5 / Mockito** (tests unitaires)
* **Spring MockMvc** (`@WebMvcTest`) (tests web)
* **JaCoCo** (couverture de code)
* **SLF4J / Logback** (logging)
* **ProblemDetail** (RFC 7807) pour les erreurs HTTP

---

## ⚡ Démarrage rapide

### 📋 Prérequis

* Java 21
* Maven

### ▶️ Lancer l’application

```bash
mvn spring-boot:run
```

Par défaut, l’application démarre sur :

* `http://localhost:8080`

### 🧪 Lancer les tests

```bash
mvn clean test
```

### 📁 Rapports générés

Après exécution des tests :

* **Surefire** : `target/surefire-reports/`
* **JaCoCo** : `target/site/jacoco/index.html`

---

## 💾 Données et persistance

### 📄 Source des données

Les données initiales sont chargées depuis :

* `src/main/resources/data.json`

### ✍️ Fichier persistant (écriture)

Pour permettre la modification des données via les endpoints CRUD **et conserver les changements après redémarrage**, l’application utilise un fichier **écrivable** défini par :

```properties
safetynet.data.path=./data/data.json
```

* Au **premier démarrage**, si le fichier n’existe pas, il est créé en **copiant** le `data.json` du classpath
* Ensuite, l’application lit et écrit **exclusivement** dans ce fichier persistant

---

## 🧱 Architecture

L’application suit une architecture **MVC** avec une **séparation stricte des responsabilités**.

```
controller
 ├─ endpoints fonctionnels
 └─ endpoints CRUD
service
 ├─ logique métier
 └─ utilitaires (ex: AgeService)
repository
 ├─ repositories spécialisés (Person / Firestation / MedicalRecord)
 └─ persistance JSON / store
config
 └─ GlobalExceptionHandler (ProblemDetail)
dto
 ├─ DTO fonctionnels (par endpoint)
 └─ DTO CRUD (request / response + mapper)
model
 ├─ entités
 └─ exceptions métier
```

### 📐 Principes appliqués

* Les **controllers** délèguent au service (aucune logique métier)
* Les **services** portent les règles fonctionnelles
* Les **repositories** gèrent l’accès et la mutation des données
* Les **DTO** clarifient les contrats et évitent d’exposer directement le modèle
* Injection par **constructeur** (DIP)

---

## 🪵 Stratégie de logs

Logging conforme aux exigences du projet :

* **INFO** : requêtes + réponses réussies
* **DEBUG** : étapes intermédiaires / calculs (ex: tailles de listes, calcul d’âge)
* **ERROR** : exceptions et erreurs techniques

---

## ❗ Gestion des erreurs

Les erreurs sont gérées de manière centralisée via un `@RestControllerAdvice` et renvoyées au format **ProblemDetail** :

* `400` Bad Request (paramètre manquant, type invalide, JSON illisible, règles métier)
* `404` Not Found
* `409` Conflict
* `500` Internal Server Error

---

## 🔌 Endpoints

> Les endpoints ci-dessous correspondent aux spécifications attendues. Les réponses sont en JSON.

### 📡 Endpoints fonctionnels

* `GET /firestation?stationNumber=<n>`
* `GET /childAlert?address=<address>`
* `GET /phoneAlert?firestation=<n>`
* `GET /fire?address=<address>`
* `GET /flood/stations?stations=<n1,n2,...>`
* `GET /personInfo?lastName=<lastName>`
* `GET /communityEmail?city=<city>`

### ✏️ Endpoints CRUD

#### Person

* `POST /person`
* `PUT /person`
* `DELETE /person?firstName=<firstName>&lastName=<lastName>`

#### Firestation

* `POST /firestation`
* `PUT /firestation`
* `DELETE /firestation?address=<address>`

#### MedicalRecord

* `POST /medicalRecord`
* `PUT /medicalRecord`
* `DELETE /medicalRecord?firstName=<firstName>&lastName=<lastName>`

---

## 🧪 Exemples Postman / curl

### ➕ Ajouter une personne

```bash
curl -X POST "http://localhost:9000/person" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jonat",
    "lastName": "Boyd",
    "address": "1509 Culver St",
    "city": "Culver",
    "zip": "97451",
    "phone": "841-874-6512",
    "email": "joubou@email.com"
  }'
```

### 🔄 Mettre à jour une personne

```bash
curl -X PUT "http://localhost:9000/person" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jonat",
    "lastName": "Boyd",
    "address": "NEW ADDRESS",
    "city": "Culver",
    "zip": "97451",
    "phone": "000-000-0000",
    "email": "new@email.com"
  }'
```

### ❌ Supprimer une personne

```bash
curl -X DELETE "http://localhost:9000/person?firstName=Jonat&lastName=Boyd"
```

---

## ✅ Tests

* **Tests unitaires** : services et utilitaires (ex: `AgeService`)
* **Tests Web MVC** : contrôleurs (`@WebMvcTest`) + gestion globale des erreurs
* **Tests de persistance** : lecture / écriture du fichier JSON

Commande :

```bash
mvn clean test
```

---

## 🌟 Points forts

* Architecture MVC claire
* Persistance JSON réelle (CRUD qui survit au redémarrage)
* Gestion d’erreurs centralisée
* Logging conforme aux exigences
* Bon niveau de tests + rapports exploitables

---

## 🚀 Axes d’amélioration

* Ajouter la **Bean Validation** (`spring-boot-starter-validation`) - **Non souhaitée dans ce projet**
* Mettre en place une **sécurité basique** (Spring Security) - **Non souhaitée dans ce projet**

---

## 👤 Auteur

* **Brian TABART**
