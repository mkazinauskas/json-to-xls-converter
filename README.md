# Json to xls converter

[![Build Status](https://travis-ci.org/modestukasai/json-to-xls-converter.svg?branch=master)](https://travis-ci.org/modestukasai/json-to-xls-converter)

[![Coverage Status](https://coveralls.io/repos/github/modestukasai/json-to-xls-converter/badge.svg?branch=master)](https://coveralls.io/github/modestukasai/json-to-xls-converter?branch=master)

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

You can configure what storage it will use by `converter.persistence.type` property. 
There are two options `memory` or `file`. 

By default file is saved to in memory cache and it will be removed after 20 minutes. 
If you want to adjust it, please add `converter.persistence.time-in-minutes` property to `application.yml` file. 

* Do 'GET' request to `/download?id={your-xls-id}` to download xls file.

If any issues, please create an issue `https://github.com/modestukasai/json-to-xls-converter`

## Licenses

This project is without any licenses, but it uses [Apache POI - the Java API for Microsoft Documents](https://poi.apache.org) 
