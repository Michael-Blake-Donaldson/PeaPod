# PeaPod - Couples Task Manager App

## Introduction

The PeaPod app is designed to help couples manage their day-to-day tasks efficiently. One partner can assign tasks to the other, and once the tasks are completed, they can be marked as done. Additionally, users can track their progress over time with task completion stats.

This app features user authentication with Firebase, real-time task management using Firestore, and a modern UI built with Material Design principles for an easy and intuitive user experience.

---

## Features

- **User Authentication**: Sign-up and log-in with Firebase Authentication.
- **Task Assignment**: Assign and mark tasks as complete for your partner.
- **Custom Task Creation**: Create custom tasks for unique chores.
- **Progress Tracking**: View task completion statistics (daily, weekly, total).
- **Modern UI**: Clean, minimalistic design with Material Components.
- **Data Storage**: Securely store user and task data with Firebase Firestore.

---

## Technologies Used

### Frontend
- **Kotlin**: Kotlin was only language used.
- **Android Studio**: The IDE I chose for developement.
- **Material Design**: Material Components for a modern UI/UX with smooth transitions and a clean layout.

### Backend
- **Firebase Authentication**: Firebase was used for secure user authentication (sign-up and log-in).
- **Firebase Firestore**: Firestore was used for real-time data storage and syncing for tasks, user profiles, and progress tracking.

### Libraries and Tools
- **Material Components Library**: For buttons, text fields, and layout elements that follow Material Design guidelines.
- **Google Firebase SDK**: For user authentication and database management.

---

## Project Structure

- **MainActivity.kt**: The app's entry point, where users choose to log in or sign up.
- **SignUpActivity.kt**: Handles user sign-up with Firebase Authentication.
- **LoginActivity.kt**: Handles user log-in with Firebase Authentication.
- **TaskActivity.kt**: The main screen where users can assign tasks and view stats.
- **StatsActivity.kt**: Displays stats related to task completion (daily, weekly, total).
- **ProfileActivity.kt**: Displays user information and handles sign-out functionality.
- **TaskListActivity.kt**: Displays a list of all completed tasks for the user.
