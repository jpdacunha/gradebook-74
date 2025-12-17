# 📘 Gradebook – Liferay 7.4 GA

## 📌 Description

**Gradebook** est un portlet Liferay 7.4 GA permettant de gérer des entités métier exposées comme des **Assets Liferay**.  
Le projet fournit une interface riche avec :

- 🔍 Recherche indexée (Search / Index Liferay)
- 📄 Pagination
- 🔄 Bascule dynamique entre les modes d’affichage :
  - Table
  - Cards
  - List
- 🔐 Gestion fine des permissions (view, add, update, delete)
- 🏷️ Taggage et catégorisation via l’Asset Framework
- 🧩 Intégration complète avec le Control Panel

Le portlet respecte les patterns Liferay (MVC, AssetRenderer, PermissionChecker, SearchContainer).

---

## 🧱 Architecture du projet

Le projet est composé de plusieurs modules Liferay :

- **gradebook-api**
  - Interfaces
  - Constantes
  - Permission definitions
- **gradebook-service**
  - Service Builder
  - Entités métier
  - Persistence
- **gradebook-web**
  - Portlet UI
  - Toolbar (management-toolbar)
  - Cards / List / Table views
  - Actions (add, edit, delete)
  - Permissions UI

---

## ⚙️ Prérequis

- Java 11
- Liferay **7.4 GA** (GA107 recommandé)
- Gradle (fourni avec Liferay Workspace)
- Base de données configurée (PostgreSQL / MySQL / H2)
- Git

---

## 🚀 Installation & Lancement

### 1️⃣ Créer un Liferay Workspace

```bash
blade init -v 7.4 liferay-workspace
cd liferay-workspace
