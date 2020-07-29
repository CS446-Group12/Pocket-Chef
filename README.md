# App

## Generating DB

Requirements: python version >= 3, `sqlite3`

```sh
pip install sqlite3
```

After everything's installed, run:
```sh
python db/make_db.py
```

For debugging, it's better to use a subset of recipes (for speed):
```sh
pyhton db/make_db.py 100
```

The .db file will be in the correct location for everything to work:
`PocketChef/app/src/main/assets/recipes.db`.

## DB Schema

The Room library generates a schema from definitions in the code, and is stored here:
```
PocketChef/app/schemas/cs446.uwaterloo.pocketchef.data.CookingDatabase/1.json
```

The schema at `db/tables.sql` should match the schema above, otherwise there will be an error.

## Delete app data when changing DB schema

Otherwise there will be many errors
