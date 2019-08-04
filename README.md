# Usumu, an API-first, cloud-native subscription and newsletter microservice

Usumu is a handy little tool store and manage your newsletter subscriptions and send out newsletters using an API.
It stores data encrypted on an S3-compatible object storage bucket.

## Installing it

The easiest way to install and run Usumu is using a container. As such you can run it like this:

```bash
docker run usumu/usumu
```

However, to make it useful, you will need to configure it.

> **Warning!** NEVER open Usumu to the Internet! It does not contain ANY authentication!

## Configuration

Usumu can be configured using environment variables:

- `USUMU_SECRET`: The secret for encrypting data.
- `USUMU_INIT_VECTOR`: The initialization vector for the AES encryption.
- `USUMU_S3_ACCESS_KEY_ID`: Access key to the S3-compatible bucket. If empty, the default AWS method will be used.
- `USUMU_S3_SECRET_ACCESS_KEY`: Secret to the S3-compatible bucket. If empty, the default AWS method will be used.
- `USUMU_S3_REGION`: The region to create the bucket in if it does not exist. If empty, the default AWS region will be used.
- `USUMU_S3_BUCKET`: The name of the S3 bucket to be used or created.
- `USUMU_S3_BUCKET_HOST`: A string for finding the S3 endpoint. `%s` can be used to insert the region. If empty, AWS will be used.

## Using it

In order to use Usumu, go to http://your-usumu-host:8080 to access the API. The API documentation will be accessible
at `/swagger-ui.html`.
