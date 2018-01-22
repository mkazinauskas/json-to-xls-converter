# Json to xls converter

## Usage

Do `POST` request to `/convert` endpoint with body

```$json
{
  "fileName": "My file name",
  "sheets": [
    {
      "name": "My sheet",
      "rows": [
        {
          "columns": [
            {
              "data": "My column data"
            }
          ]
        }
      ]
    }
  ]
}
```

If any issues, please create an issue `https://github.com/modestukasai/json-to-xls-converter`


## Licenses

This project is without any licenses, but it uses [Apache POI - the Java API for Microsoft Documents](https://poi.apache.org) 