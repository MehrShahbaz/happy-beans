# Database setup

## Install postgresql (latest version)
```
brew install postgresql@17
```

## Start psql service
```
brew services start postgresql@17
```

## In the terminal than run
```
createuser -s your_user_name_or_any_name_eg_postgres
```

## After creating the user set the password
```
psql -U your_user_name postgres

ALTER USER your_user_name WITH PASSWORD 'your_password_here';
```


## Run this command to get into the psql server
```
psql -U your_user_name postgres
```

## Run this to create the db
```
CREATE DATABASE happy_beans_db;
```


## Just to double-check
```
psql -U your_user_name postgres

postgres=# SHOW port;
```
### Sample Output
```
psql (17.6 (Homebrew))
Type "help" for help.

postgres=# SHOW port;
port
------
5432
(1 row)

postgres=#\q
```

## Note
### And make changes to .env depending upon your username and pw

### See your tables
```
\l
```

### Run to get out of the db
```
\q
```
OR
```
(control + d) [macOS]
```

### If using `~/.zshrc`
```
echo 'export PATH="/opt/homebrew/opt/postgresql@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```