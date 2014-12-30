# Open M3U8 (working name)

## Description

This is an open source M3U8 playlist parser java library that attempts to conform to this specification:

http://tools.ietf.org/html/draft-pantos-http-live-streaming-14

Currently the functionality is more than sufficient for our needs. However, there is still a lot of work to be done before we have full compliance. Pull requests are welcome!

## Rationale

We would like to give back to the open source community surrounding Android that has helped make iHeartRadio a success. By using the MIT license we hope to make this code as usable as possible.

## Artifacts

We now have artifacts in Maven Central!

### Gradel

```
dependencies {
  compile 'com.iheartradio.m3u8:open-m3u8:0.0.1'
}
```

### Maven

```
<dependency>
  <groupId>com.iheartradio.m3u8</groupId>
  <artifactId>open-m3u8</artifactId>
  <version>0.0.1</version>
</dependency>
```
