# File Search Service

A simple service which helps index files from the cloud storage

---

## Prerequisites

### S3 Setup

1. The service currently only supports reading from S3.
2. Make a bucket on S3 which which store all files.
3. Update `config/local.yml` with the correct bucket name.

### Elasticsearch setup
1. Elasticsearch is currently used to maintain the file index.
2. Once the cluster has been setup, cofigure it with below mapping:
```shell
curl -X PUT "localhost:9200/file-info?pretty" -H 'Content-Type: application/json' -d'
{
  "settings": {
    "index": {
      "analysis": {
        "analyzer": {
          "analyzer_letter": {
            "tokenizer": "letter",
            "filter": "lowercase"
          }
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "url": {
        "type": "text"
      },
      "content": {
        "type": "text",
        "store": false,
        "analyzer": "analyzer_letter"
      },
      "modified_at": {
        "type": "long"
      }
    }
  }
}
'
```
3. Update `config/local.yml` to point to the correct ES cluster.

### SQS setup

1. SQS is used to read events from S3 and automatically insert or delete files from the index.
2. Create a SQS queue and configure it to receive notification from S3. Refer to following [document](https://docs.aws.amazon.com/AmazonS3/latest/userguide/ways-to-add-notification-config-to-bucket.html).
3. Once the queue has been created, update `config/local.yml` to the correct queue name.
---



## Setup

1. Git clone the repository

```shell
$ git clone https://github.com/agaviral/hevo-take-home-test.git
```

2. Build a jar

```shell
$ cd hevo-take-home-test
$ ./gradlew shadowJar
```

3. Start the server

```shell
$ java -jar ./build/libs/hevo-take-home-test-0.0.1-all.jar server config/local.yml
```

4. This starts an HTTP server at http://localhost:8080

---

## API Documentation

The server supports the following APIs:

- Search for a file using the given query

```
    GET     /file-indexer/search?q="the"
```

- Backfill the index for all the files in underlying datasource
```
    POST    /file-indexer/admin/tasks/backfill-file-index
```