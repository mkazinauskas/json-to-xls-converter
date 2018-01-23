# Json to xls converter

[![Build Status](https://travis-ci.org/modestukasai/json-to-xls-converter.svg?branch=master)](https://travis-ci.org/modestukasai/json-to-xls-converter)

## Usage

* Do `POST` request to `/convert` endpoint with body

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
It will return `location` header with link to saved xls file. 
File is saved to in memory cache and it will be removed after 20 minutes (default). 
If you want to adjust it, please add `converter.persistence.time-in-minutes` property to `application.yml` file. 

* Do 'GET' request to `/download?id={your-xls-id}` to download xls file.

If any issues, please create an issue `https://github.com/modestukasai/json-to-xls-converter`

## Licenses

This project is without any licenses, but it uses [Apache POI - the Java API for Microsoft Documents](https://poi.apache.org) 