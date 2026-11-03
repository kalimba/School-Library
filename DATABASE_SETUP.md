# Database Installation — School Library

This project uses **Apache Derby Network Server**, not MySQL.

## 1. Database settings

The application reads the host and database name from the fourth and fifth lines of `config.txt`:

| Setting | Value |
|---|---|
| Host | `localhost` |
| Port | `1527` |
| Database | `KGLLIBRARY` |
| Database user / schema | `KGL` |
| JDBC URL | `jdbc:derby://localhost:1527/KGLLIBRARY;` |

The first three lines of `config.txt` are project-relative paths for the images and backup output. Do not replace them with machine-specific absolute paths.

## 2. Configure Derby authentication

1. Create a folder for Derby's system files, for example `%USERPROFILE%\\SchoolLibraryDerby`.
2. Copy `derby.properties.example` into that folder and rename the copy to `derby.properties`.
3. Replace `CHANGE_ME_TO_A_STRONG_PASSWORD` with a strong password.
4. Do not commit the real `derby.properties` file to GitHub.

The file should contain:

```properties
derby.connection.requireAuthentication=true
derby.authentication.provider=BUILTIN
derby.user.KGL=YOUR_STRONG_PASSWORD
derby.drda.host=localhost
```

## 3. Start the Derby Network Server

Open PowerShell in the project folder. The required Derby libraries are already in `lib/`.

Start the server using the Derby system-home folder you created:

```powershell
java "-Dderby.system.home=$env:USERPROFILE\\SchoolLibraryDerby" -cp "lib\\derby.jar;lib\\derbynet.jar;lib\\derbyclient.jar" org.apache.derby.drda.NetworkServerControl start -h localhost -p 1527
```

Keep this terminal open while using the application. If Derby is installed separately, you can also start the server using its own Derby tools.

## 4. Create the database

Create the database `KGLLIBRARY` while authenticating as user `KGL`. For example, with the Derby `ij` tool:

```text
connect 'jdbc:derby://localhost:1527/KGLLIBRARY;create=true;user=KGL;password=YOUR_STRONG_PASSWORD';
```

Use the SQL script in the next step to create the application tables.

If the database already exists, connect to it as `KGL` without `create=true`.

## 5. Create the tables and indexes

After connecting to `KGLLIBRARY` as `KGL`, execute:

```text
run 'sql/01_create_schema.sql';
```

The script creates the `KGL` schema, 12 application tables, primary keys, and the three non-unique indexes found in the supplied database.

The source database did not declare foreign-key constraints. The schema file preserves this; it does not invent relationships that were not present in the original database.

The script is intended for a **new, empty database**. Do not run it a second time on a database where the schema/tables already exist.

## 6. Configure the application credentials

The application reads credentials from environment variables. It does not contain a hardcoded database password.

For a temporary PowerShell session:

```powershell
$env:KGL_DB_USER = "KGL"
$env:KGL_DB_PASSWORD = "YOUR_STRONG_PASSWORD"
```

Start NetBeans from that same PowerShell session so the IDE and the application inherit those variables. Alternatively, create Windows user environment variables with these names, then close and reopen NetBeans.

Do not put the real password in `config.txt`, Java source files, or Git.

## 7. Run the application

1. Make sure Derby Network Server is running on `localhost:1527`.
2. Confirm that `config.txt` contains `localhost` and `KGLLIBRARY` on lines 4 and 5.
3. Confirm that `KGL_DB_USER` and `KGL_DB_PASSWORD` are available to the NetBeans process.
4. Open the `School-Library` project in NetBeans.
5. Choose **Clean and Build**, then **Run**.

## 8. Existing database and records

Live Derby database files are not included in the repository. Database files may contain private student, librarian, and circulation records and should not be stored in source control.

The included SQL file is a **schema-only** reconstruction from that database. It does not migrate or preserve the existing records. If you need to move existing data, export it separately to a secured backup and import it into the new database after the schema is created. Do not publish that backup in a public repository.
