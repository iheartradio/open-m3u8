# Open M3U8 (working name)

## Description

This is an open source M3U8 playlist parser java library that attempts to conform to this specification:

http://tools.ietf.org/html/draft-pantos-http-live-streaming-14

Currently the functionality is more than sufficient for our needs. However, there is still a lot of work to be done before we have full compliance. Pull requests are welcome!

## Rationale

We would like to give back to the open source community surrounding Android that has helped make iHeartRadio a success. By using the MIT license we hope to make this code as usable as possible.

## Artifacts

We now have artifacts in Maven Central!

### Gradle

```
dependencies {
  compile 'com.iheartradio.m3u8:open-m3u8:0.0.3.1'
}
```

### Maven

```
<dependency>
  <groupId>com.iheartradio.m3u8</groupId>
  <artifactId>open-m3u8</artifactId>
  <version>0.0.3.1</version>
</dependency>
```

## Getting started

Getting started with parsing is quite easy: Get a `PlaylistParser` and specify the format.

```java
InputStream inputStream = ...
PlaylistParser parser = new PlaylistParser();
// The inputStream is automatically closed after this operation
Playlist playlist = parser.parse(inputStream, Format.EXT_M3U, Encoding.UTF_8);
```

Creating a new `Playlist` works via `Builder`s and their fluent `with*()` methods. On each `build()` method the provided parameters are validated:

```java
TrackData trackData = new TrackData.Builder()
    .withTrackInfo(new TrackInfo(3.0f, "Example Song"))
    .withPath("example.mp3")
    .build();

List<TrackData> tracks = new ArrayList<TrackData>();
tracks.add(trackData);

MediaPlaylist mediaPlaylist = new MediaPlaylist.Builder()
    .withMediaSequenceNumber(1)
    .withTargetDuration(3)
    .withTracks(tracks)
    .build();

Playlist playlist = new Playlist.Builder()
    .withCompatibilityVersion(5)
    .withMediaPlaylist(mediaPlaylist)
    .build();
```

The Playlist is similar to a C style union of a `MasterPlaylist` and `MediaPlaylist` in that it has one or the other but not both. You can check with `Playlist.hasMasterPlaylist()` or `Playlist.hasMediaPlaylist()` which type you got.

Modifying an existing `Playlist` work similar to creating via the `Builder`s: Also each data class provides an `buildUpon()` method to generate a new `Builder` with all the data from the object itself:

```java
TrackData additionalTrack = new TrackData.Builder()
    .withTrackInfo(new TrackInfo(3.0f, "Additional Song"))
    .withPath("additional.mp3")
    .build();

List<TrackData> updatedTracks = new ArrayList<TrackData>(playlist.getMediaPlaylist().getTracks());
updatedTracks.add(additionalTrack);

MediaPlaylist updatedMediaPlaylist = playlist.getMediaPlaylist()
    .buildUpon()
    .withTracks(updatedTracks)
    .build();

Playlist updatedPlaylist = playlist.buildUpon()
    .withMediaPlaylist(updatedMediaPlaylist)
    .build();
```

Writing out a `Playlist` again works the same way as parsing via `PlaylistParser`:

```java
OutputStream outputStream = ...
PlaylistWriter writer = new PlaylistWriter();
writer.write(outputStream, updatedPlaylist, Format.EXT_M3U, Encoding.UTF_8);
```

causing this playlist to be written:

```
#EXTM3U
#EXT-X-VERSION:5
#EXT-X-TARGETDURATION:3
#EXT-X-MEDIA-SEQUENCE:1
#EXTINF:3.0,Example Song
example.mp3
#EXTINF:3.0,Additional Song
additional.mp3
#EXT-X-ENDLIST
```

## Advanced usage

### Lenient mode

The parser supports a `lenient` mode - by default it operats in a `strict` mode and parsing unknown tags in the `InputStream` cause an `ParseException`.

Activating the `lenient` mode via `com.iheartradio.m3u8.PlaylistParser.parse(InputStream, Format, Encoding, ParsingMode)`:

```java
InputStream inputStream = ...
PlaylistParser parser = new PlaylistParser();
Playlist playlist = parser.parse(inputStream, Format.EXT_M3U, Encoding.UTF_8, ParsingMode.LENIENT);
if (playlist.hasMasterPlaylist() && playlist.getMasterPlaylist().hasUnknownTags()) {
    System.err.println(
        playlist.getMasterPlaylist().getUnknownTags());
} else if (playlist.hasMediaPlaylist() && playlist.getMediaPlaylist().hasUnknownTags()) {
    System.err.println(
        playlist.getMediaPlaylist().getUnknownTags());
} else {
    System.out.println("Parsing without unknown tags successful");
}
```

=======
## Build

This is a Gradle 2.1 project. Build via
```
gradle build
```
which also executes the unit tests.

Cobertura is configured to report the line coverage:
```
gradle cobertura
```
producing the coverage report at `build/reports/cobertura/index.html`
