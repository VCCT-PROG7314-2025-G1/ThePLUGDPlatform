# The PLUGD Application
_A Smart Platform to Discover, Connect, and Experience Events._  

Created by **Emma Jae Dunn**  
Student Number: **ST10301125**  
Module: **Programming 3D - PROG7314**  

---

## 📌 Overview  
PLUGD is a mobile application designed to help users **discover events, artists, and organizers** in a simple and interactive way. The PLUGD App is intended to be an innovative and dynamic platform that unifies artist promotion & recognition, event discovery, brand partnership & sponsoring and community
engagement for a vibrant single ecosystem. The PLUGD platform revolutionizes creativity and discovery with a virtually assisted approach, emphasizing community and diversity for emerging talent and
professionals.

It is built entirely with **Kotlin** and **Jetpack Compose (Material 3)** in Android Studio, making it modern, scalable, and easy to maintain.  

The app was developed as part of the **PROG7312** module to demonstrate skills in:  
- Declarative UI with Jetpack Compose  
- State management  
- Navigation between screens  
- Data synchronization (local + remote repository pattern)  
- Applying good UI/UX principles (search, filters, navigation bars)  

---

## 🚀 Features  
- **Onboarding Flow** → Guides first-time users through app introduction.  
- **Authentication** → Simple login/register screens.  
- **Event Discovery Page** → Users can browse events, apply filters, and search.  
- **Search Integration** → Search events, artists, or organizers directly from the **TopBar**.  
- **Navigation**  
  - **Bottom Navigation Bar**: Provides quick access to core sections.  
  - **Top App Bar**: Transparent with search bar, logo, and filter/menu button.  
  - **Back Button Handling**: Appears only when navigating into sub-screens (e.g., Filters, Settings).  
- **Event Synchronization** → Repository pattern allows syncing events between remote API and local database.  

---

## 📱 Screens  

### 1. Onboarding  
- Shown only on first app launch.  
- Explains core features of PLUGD.  

### 2. Authentication  
- **Login** and **Register** screens.  
- Minimal user input required (Name, Username, Email, Password).  
- Additional details (e.g., Date of Birth, Account Type) captured later in the profile screen.  

### 3. Home (Discovery)  
- Displays events with filters and search functionality.  
- Includes:  
  - **TopBar** → Transparent with logo, search bar, and menu.  
  - **Search** → Allows querying events/artists in real-time.  
  - **Bottom Navigation** → Navigation between main features.  

### 4. Add Plug  
- Screen for adding your own event ("Plug").  
- Accessible from the home screen navigation.  

### 5. Settings & Filters  
- Accessible via menu button.  
- Provides filters for event discovery (by date, location, category).  
- Back button enabled to return to Home.  

---

## 🏗️ Architecture  
The app follows **MVVM (Model-View-ViewModel)** with **Repository Pattern**:  

- **UI Layer (Compose)** → Stateless Composables (Screens & Components).  
- **ViewModel** → Handles UI state, event syncing, and business logic.  
- **Repository** → Central data manager for events (fetching from local DB + remote API).  
- **Navigation** → Handled using `NavHost` and `NavController` (Jetpack Compose Navigation).  

---

## ⚙️ Technologies Used  
- **Kotlin** (Primary language)  
- **Jetpack Compose (Material 3)** for UI  
- **Navigation Component (Compose)** for in-app navigation  
- **ViewModel & LiveData/StateFlow** for state management  
- **Room Database (optional for local storage)**  
- **Coroutines** for asynchronous operations  

---

## 📂 Project Structure  
