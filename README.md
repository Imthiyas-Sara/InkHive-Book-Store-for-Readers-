# InkHive – Book App

**InkHive** is a simple Android application developed as part of the **Human-Computer Interaction (HCI)** module. 
The app allows users to browse, search, and save favorite books using the **Google Books API**, with user authentication and data storage handled via **Firebase**.  

This project was built entirely in **Android Studio** using **Java**, with emulator testing for Android devices.

---

## Features
- User authentication via Firebase:
  - Login
  - Signup
  - Logout
- Profile management for each user.
- Browse popular and recommended books on the Home screen.
- View detailed information for each book, including title, author, page count, description, and preview link.
- Add books to a personalized **Favourites** list.
- Search for books using the Google Books API.
- Remove books from the Favourites list.

---

## Technologies & Tools

- **Programming Language:** Java
- **IDE:** Android Studio
- **Database & Authentication:** Firebase
- **APIs:** Google Books API
- **UI Components:** RecyclerView, Buttons, TextViews, CardViews
- **Development Environment:** Android Emulator
- **Additional Libraries/Tools:** Firebase Authentication SDK, Firebase Realtime Database, Kotlin plugins where necessary
---

## Members & Contributions

| Member | Contribution |
|--------|-------------|
| **I Sara (SA24610703)** | Group Leader; Home Page UI design and implementation, Popular & Recommended sections, RecyclerViews, book click & “Add to Favourites” functionality, led project completion and coordinated team efforts. |
| **M S F Zikra (SA24610771)** | Profile, Login, and Register pages; Firebase Authentication integration for login/signup/logout; managed user data storage. |
| **F Z Insarf (SA24610519)** | Details Page: search functionality using Google Books API; displayed book details (title, author, page count, description, preview link); preview link implementation. |
| **S Mahisha (SA24610770)** | Favourites Page: designed and implemented Favourites screen; displayed saved books; remove functionality; RecyclerView implementation. |

---

## Project Workflow

1. **Browse** – Users can explore popular and recommended books on the Home screen.
2. **Read** – Clicking on a book opens the Details page with complete book information fetched from the Google Books API.
3. **Save** – Users can add books to their Favourites list.
4. **Manage** – Users can search for books, view details, and remove books from the Favourites list.
5. **Authentication** – Users can create accounts, login, and manage their profiles using Firebase Authentication.

---

## How to Run

1. Clone the repository:

```bash
git clone https://github.com/Imthiyas-Sara/InkHive-Book-Store-for-Readers-.git
