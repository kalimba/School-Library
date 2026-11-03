# Set Up the School Library Database

Follow these steps to configure Apache Derby and run the School Library application. This project uses **Apache Derby Network Server**.

## 1. Check the database settings

The application reads the database host and name from lines 4 and 5 of `config.txt`:

| Setting | Value |
|---|---|
| Host | `localhost` |
| Port | `1527` |
| Database | `KGLLIBRARY` |
| Database user / schema | `KGL` |
| JDBC URL | `jdbc:derby://localhost:1527/KGLLIBRARY;` |

Keep the first three lines of `config.txt` as project-relative paths for the images and backup output. This lets the project find those folders without relying on paths specific to one computer.

## 2. Set up Derby authentication

1. Create a folder for Derby's system files, such as `%USERPROFILE%\SchoolLibraryDerby`.
2. Copy `derby.properties.example` into that folder and rename the copy to `derby.properties`.
3. In the copied file, replace `CHANGE_ME_TO_A_STRONG_PASSWORD` with a strong password of your choice.
4. Keep this password private, and do not add your personal `derby.properties` file to the project or commit it to GitHub.

The file should contain:

```properties
derby.connection.requireAuthentication=true
derby.authentication.provider=BUILTIN
derby.user.KGL=YOUR_STRONG_PASSWORD
derby.drda.host=localhost
```

## 3. Start the Derby Network Server

Open PowerShell in the project folder. The Derby libraries needed to start the server are included in `lib/`.

Run this command to start Derby using the system-home folder you created:

```powershell
java "-Dderby.system.home=$env:USERPROFILE\SchoolLibraryDerby" -cp "lib\derby.jar;lib\derbynet.jar;lib\derbyclient.jar" org.apache.derby.drda.NetworkServerControl start -h localhost -p 1527
```

Leave this PowerShell window open while you use the application. If you have Derby installed separately, you can start its Network Server with the Derby tools instead.

## 4. Create the database

Connect to `KGLLIBRARY` as user `KGL` and create the database. For example, with the Derby `ij` tool, run:

```text
connect 'jdbc:derby://localhost:1527/KGLLIBRARY;create=true;user=KGL;password=YOUR_STRONG_PASSWORD';
```

Replace `YOUR_STRONG_PASSWORD` with the password you set in `derby.properties`. If `KGLLIBRARY` already exists, connect as `KGL` without `create=true`.

## 5. Create the tables and indexes

After connecting to `KGLLIBRARY` as `KGL`, run:

```text
run 'sql/01_create_schema.sql';
```

This script creates the `KGL` schema, 12 application tables, their primary keys, and three non-unique indexes. It is intended for a **new, empty database**; do not run it again if the schema or tables already exist.

The script does not add foreign-key constraints. The application schema is set up without them.

## 6. Set the application credentials

The application reads its database username and password from environment variables. Set them in PowerShell for the current session:

```powershell
$env:KGL_DB_USER = "KGL"
$env:KGL_DB_PASSWORD = "YOUR_STRONG_PASSWORD"
```

Start NetBeans from this same PowerShell window so it inherits the variables. You can also set Windows user environment variables with these names; after setting them, close and reopen NetBeans so it picks them up.

Keep your password out of `config.txt`, Java source files, and Git. Use your own password in place of `YOUR_STRONG_PASSWORD` wherever it appears in these instructions.

## 7. Run the application

1. Make sure the Derby Network Server is running at `localhost:1527`.
2. Check that lines 4 and 5 of `config.txt` contain `localhost` and `KGLLIBRARY`, respectively.
3. Check that `KGL_DB_USER` and `KGL_DB_PASSWORD` are available to NetBeans.
4. Open the `School-Library` project in NetBeans.
5. Select **Clean and Build**, then **Run**.

