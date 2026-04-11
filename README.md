# TP Jenkins — Spring Boot + DevOps

Projet Spring Boot avec pipeline CI/CD complet :
**Jenkins** → **SonarQube** → **Nexus** + **PostgreSQL**, le tout via **Docker Compose**.

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────┐
│                 Docker Network                  │
│                                                 │
│  ┌───────────┐   ┌───────────┐   ┌──────────┐  │
│  │  Jenkins  │-->│ SonarQube │   │  Nexus   │  │
│  │  :8080    │   │   :9000   │   │  :8081   │  │
│  └───────────┘   └───────────┘   └──────────┘  │
│                                                 │
│  ┌───────────┐   ┌───────────┐                 │
│  │PostgreSQL │   │ Sonar DB  │                 │
│  │  :5432    │   │(interne)  │                 │
│  └───────────┘   └───────────┘                 │
└─────────────────────────────────────────────────┘
          ↑
   Spring Boot App (build par Jenkins)
```

## 📁 Structure du projet

```
tp_jenkins/
├── src/
│   ├── main/java/com/devops/tpjenkins/
│   │   ├── TpJenkinsApplication.java
│   │   ├── entity/Student.java
│   │   ├── repository/StudentRepository.java
│   │   ├── service/StudentService.java
│   │   └── controller/StudentController.java
│   ├── main/resources/application.properties
│   └── test/...
├── docker-compose.yml       ← Infrastructure Docker
├── Dockerfile               ← Build de l'image Spring Boot
├── Jenkinsfile              ← Pipeline CI/CD
├── settings-docker.xml      ← Config Maven pour Nexus
├── sonar-project.properties ← Config SonarQube
└── pom.xml
```

## 🚀 Démarrage rapide

### 1. Démarrer l'infrastructure Docker

```powershell
cd "d:\projets\Mr Hajarisena\devops\tp_jenkins"
docker compose up -d
```

Attendez ~60 secondes pour que tous les services démarrent.

### 2. Vérifier les services

| Service     | URL                        | Login         |
|-------------|----------------------------|---------------|
| Jenkins     | http://localhost:8080       | (voir ci-dessous) |
| SonarQube   | http://localhost:9000       | admin / admin |
| Nexus       | http://localhost:8081       | admin / (voir ci-dessous) |
| PostgreSQL  | localhost:5432              | postgres / oni |

**Mot de passe Jenkins initial :**
```powershell
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

**Mot de passe Nexus initial :**
```powershell
docker exec nexus cat /nexus-data/admin.password
```

---

## ⚙️ Configuration Jenkins

### Plugins à installer (en plus des suggestions)
- Pipeline
- SonarQube Scanner
- Nexus Artifact Uploader
- JaCoCo
- Blue Ocean (optionnel)

### Variables à configurer (Manage Jenkins > System)

**SonarQube Server :**
- Name: `SonarQube`
- Server URL: `http://sonarqube:9000`
- Token: (générer depuis SonarQube > My Account > Security)

**Credentials Nexus :**
- ID: `nexus-credentials`
- Username: `admin`
- Password: (mot de passe Nexus initial)

**Maven Tool (Manage Jenkins > Tools) :**
- Name: `Maven-3.9`
- Version: 3.9.x (installer automatiquement)

### Créer le Pipeline
1. New Item → Pipeline
2. Cocher "Pipeline script from SCM"
3. SCM: Git, URL du repo
4. Script Path: `Jenkinsfile`

---

## 🗄️ Base de données

- **Nom:** `springboot_jenkins`
- **Utilisateur:** `postgres`
- **Mot de passe:** `oni`
- **Port:** `5432`

---

## 🌐 API REST

Endpoints disponibles une fois l'app démarrée sur le port `8085` :

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | `/api/students` | Liste tous les étudiants |
| GET | `/api/students/{id}` | Récupère un étudiant |
| POST | `/api/students` | Crée un étudiant |
| PUT | `/api/students/{id}` | Met à jour un étudiant |
| DELETE | `/api/students/{id}` | Supprime un étudiant |

Exemple de corps POST :
```json
{
  "nom": "Dupont",
  "prenom": "Jean",
  "age": 22,
  "email": "jean.dupont@email.com"
}
```

---

## 🔧 Lancer l'app localement (sans Docker)

Assurez-vous que PostgreSQL tourne (via Docker ou local) puis :

```powershell
mvn spring-boot:run
```

---

## 🧪 Lancer les tests

```powershell
mvn test
```

## 📊 Rapport de couverture (JaCoCo)

```powershell
mvn verify
# Rapport dans : target/site/jacoco/index.html
```
