# School Library Management System

<<<<<<< HEAD
School Library Management System is a desktop application designed to support the daily administration of a school library. It provides tools for maintaining library records, managing books and computers, handling loans and returns, and viewing library reports.

## Application functionalities

### User access
- Provides a login screen for accessing the application.

### Student and staff management
- Register and maintain student records.
- Organize students by class and department.
- Register and maintain librarian records.
- View current students and graduate records.

### Library collection management
- Register and maintain books.
- Organize books by author and category.
- Register and maintain computers and other library equipment.
- View lists of registered books and computers.

### Lending and returns
- Issue books to students and record loan details.
- Issue computers to students and record loan details.
- Process returned books and update their availability.
- Process returned computers and update their availability.

### Reports and information
- View available books and computers.
- View issued books and computers.
- Identify overdue books and computers.
- View student, book, and computer lists.
- Export supported report data for further use.

## Technology

- Java Swing desktop interface
- Apache Derby database
- NetBeans project structure
=======
A Java Swing desktop application for managing a school library. The application is maintained as a NetBeans Ant project and uses Apache Derby for database storage.

The project branding has been updated to **School Library / Kigali**. Previous institution and organization identifiers have been updated to Kigali and KGL.

## Main features

- User login
- Student, class, and department registration
- Librarian registration
- Book, author, and category management
- Computer inventory management
- Book and computer issuing and returning
- Overdue, available-item, and student reports
- Current-student and graduate views

## Technology

- Java Swing (Java source and NetBeans `.form` files)
- NetBeans Ant project
- Java source/target compatibility: Java 8
- Apache Derby 10.12 libraries
- Apache POI and supporting libraries for spreadsheet/report functionality

## Open the project

1. Install a JDK compatible with Java 8 source/target and Apache NetBeans with Java SE support.
2. Download or clone this repository.
3. In NetBeans, choose **File → Open Project**.
4. Select the `School-Library` folder.
5. Confirm that the bundled dependencies under `lib/` are available.
6. Follow [DATABASE_SETUP.md](DATABASE_SETUP.md) before running the application.
7. Right-click the project and select **Clean and Build**, then **Run**.

The JAR references in `nbproject/project.properties` are project-relative (`${basedir}/lib/...`); they no longer depend on a developer's `D:\` or `E:\` folders.

## Database

The application uses **Apache Derby Network Server**. The database name is `KGLLIBRARY`, the application database user is `KGL`, and the schema is `KGL`.

- Database setup: [DATABASE_SETUP.md](DATABASE_SETUP.md)
- Schema-only SQL: [`sql/01_create_schema.sql`](sql/01_create_schema.sql)
- Derby configuration template: [`derby.properties.example`](derby.properties.example)

The SQL file was generated from the structure of the Derby database included in the supplied project archive. It contains table definitions and indexes only; it does **not** contain student, librarian, book, or circulation records. The source database had no declared foreign-key constraints, so the schema preserves that original design.


## Upload to GitHub\n\nFollow [GITHUB_UPLOAD.md](GITHUB_UPLOAD.md) for the Git commands and first push.\n\n## Repository contents

- `src/` — Java source and NetBeans form files
- `image/` — supplied application images
- `lib/` — project dependencies
- `nbproject/` — portable NetBeans project settings
- `sql/` — database schema
- `config.txt` — application paths and Derby host/database settings
- `backup/` — local backup destination (contents ignored by Git)

Generated `build/` and `dist/` folders, NetBeans private settings, live Derby database files, logs, and local backups are intentionally excluded from Git.


## Known setup notes

- The original archive referenced several button-icon files that were not present in its `image/` directory (`registerBook.png`, `issueBook.png`, `issueComputer.png`, `returnBook.png`, and `returnComputer.png`). Add the original image assets or update the corresponding lines in `config.txt` if those icons are required by your build.
- The database schema is reconstructed from the supplied database metadata. Test the project against a fresh Derby database before using it for real school records.
"# School-Library" 

