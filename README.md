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

    