package com.bulbulproject;

import com.apollographql.apollo.api.Field;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.ResponseFieldMapper;
import com.apollographql.apollo.api.ResponseReader;
import com.apollographql.apollo.api.internal.UnmodifiableMapBuilder;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("Apollo GraphQL")
public final class PlaylistsQuery implements Query<PlaylistsQuery.Data, PlaylistsQuery.Data, PlaylistsQuery.Variables> {
  public static final String OPERATION_DEFINITION = "query PlaylistsQuery($id: Int, $limit: Int, $skip: Int, $ids: [Int]) {\n"
      + "  playlists(id: $id, limit: $limit, skip: $skip, ids: $ids) {\n"
      + "    __typename\n"
      + "    id\n"
      + "    name\n"
      + "    tracks {\n"
      + "      __typename\n"
      + "      id\n"
      + "      name\n"
      + "      spotify_track_id\n"
      + "      artists {\n"
      + "        __typename\n"
      + "        name\n"
      + "      }\n"
      + "    }\n"
      + "  }\n"
      + "}";

  public static final String QUERY_DOCUMENT = OPERATION_DEFINITION;

  private final PlaylistsQuery.Variables variables;

  public PlaylistsQuery(@Nullable Integer id, @Nullable Integer limit, @Nullable Integer skip,
      @Nullable List<Integer> ids) {
    variables = new PlaylistsQuery.Variables(id, limit, skip, ids);
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public PlaylistsQuery.Data wrapData(PlaylistsQuery.Data data) {
    return data;
  }

  @Override
  public PlaylistsQuery.Variables variables() {
    return variables;
  }

  @Override
  public ResponseFieldMapper<PlaylistsQuery.Data> responseFieldMapper() {
    return new Data.Mapper();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Variables extends Operation.Variables {
    private final @Nullable Integer id;

    private final @Nullable Integer limit;

    private final @Nullable Integer skip;

    private final @Nullable List<Integer> ids;

    private final transient Map<String, Object> valueMap = new LinkedHashMap<>();

    Variables(@Nullable Integer id, @Nullable Integer limit, @Nullable Integer skip,
        @Nullable List<Integer> ids) {
      this.id = id;
      this.limit = limit;
      this.skip = skip;
      this.ids = ids;
      this.valueMap.put("id", id);
      this.valueMap.put("limit", limit);
      this.valueMap.put("skip", skip);
      this.valueMap.put("ids", ids);
    }

    public @Nullable Integer id() {
      return id;
    }

    public @Nullable Integer limit() {
      return limit;
    }

    public @Nullable Integer skip() {
      return skip;
    }

    public @Nullable List<Integer> ids() {
      return ids;
    }

    @Override
    public Map<String, Object> valueMap() {
      return Collections.unmodifiableMap(valueMap);
    }
  }

  public static final class Builder {
    private @Nullable Integer id;

    private @Nullable Integer limit;

    private @Nullable Integer skip;

    private @Nullable List<Integer> ids;

    Builder() {
    }

    public Builder id(@Nullable Integer id) {
      this.id = id;
      return this;
    }

    public Builder limit(@Nullable Integer limit) {
      this.limit = limit;
      return this;
    }

    public Builder skip(@Nullable Integer skip) {
      this.skip = skip;
      return this;
    }

    public Builder ids(@Nullable List<Integer> ids) {
      this.ids = ids;
      return this;
    }

    public PlaylistsQuery build() {
      return new PlaylistsQuery(id, limit, skip, ids);
    }
  }

  public static class Data implements Operation.Data {
    private final @Nullable List<Playlist> playlists;

    public Data(@Nullable List<Playlist> playlists) {
      this.playlists = playlists;
    }

    public @Nullable List<Playlist> playlists() {
      return this.playlists;
    }

    @Override
    public String toString() {
      return "Data{"
        + "playlists=" + playlists
        + "}";
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o instanceof Data) {
        Data that = (Data) o;
        return ((this.playlists == null) ? (that.playlists == null) : this.playlists.equals(that.playlists));
      }
      return false;
    }

    @Override
    public int hashCode() {
      int h = 1;
      h *= 1000003;
      h ^= (playlists == null) ? 0 : playlists.hashCode();
      return h;
    }

    public static final class Mapper implements ResponseFieldMapper<Data> {
      final Playlist.Mapper playlistFieldMapper = new Playlist.Mapper();

      final Field[] fields = {
        Field.forList("playlists", "playlists", new UnmodifiableMapBuilder<String, Object>(4)
          .put("limit", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "limit")
          .build())
          .put("ids", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "ids")
          .build())
          .put("skip", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "skip")
          .build())
          .put("id", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "id")
          .build())
        .build(), true, new Field.ObjectReader<Playlist>() {
          @Override public Playlist read(final ResponseReader reader) throws IOException {
            return playlistFieldMapper.map(reader);
          }
        })
      };

      @Override
      public Data map(ResponseReader reader) throws IOException {
        final List<Playlist> playlists = reader.read(fields[0]);
        return new Data(playlists);
      }
    }

    public static class Artist {
      private final @Nonnull String name;

      public Artist(@Nonnull String name) {
        this.name = name;
      }

      public @Nonnull String name() {
        return this.name;
      }

      @Override
      public String toString() {
        return "Artist{"
          + "name=" + name
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Artist) {
          Artist that = (Artist) o;
          return ((this.name == null) ? (that.name == null) : this.name.equals(that.name));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (name == null) ? 0 : name.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Artist> {
        final Field[] fields = {
          Field.forString("name", "name", null, false)
        };

        @Override
        public Artist map(ResponseReader reader) throws IOException {
          final String name = reader.read(fields[0]);
          return new Artist(name);
        }
      }
    }

    public static class Track {
      private final int id;

      private final @Nonnull String name;

      private final @Nullable String spotify_track_id;

      private final @Nullable List<Artist> artists;

      public Track(int id, @Nonnull String name, @Nullable String spotify_track_id,
          @Nullable List<Artist> artists) {
        this.id = id;
        this.name = name;
        this.spotify_track_id = spotify_track_id;
        this.artists = artists;
      }

      public int id() {
        return this.id;
      }

      public @Nonnull String name() {
        return this.name;
      }

      public @Nullable String spotify_track_id() {
        return this.spotify_track_id;
      }

      public @Nullable List<Artist> artists() {
        return this.artists;
      }

      @Override
      public String toString() {
        return "Track{"
          + "id=" + id + ", "
          + "name=" + name + ", "
          + "spotify_track_id=" + spotify_track_id + ", "
          + "artists=" + artists
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Track) {
          Track that = (Track) o;
          return this.id == that.id
           && ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
           && ((this.spotify_track_id == null) ? (that.spotify_track_id == null) : this.spotify_track_id.equals(that.spotify_track_id))
           && ((this.artists == null) ? (that.artists == null) : this.artists.equals(that.artists));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= id;
        h *= 1000003;
        h ^= (name == null) ? 0 : name.hashCode();
        h *= 1000003;
        h ^= (spotify_track_id == null) ? 0 : spotify_track_id.hashCode();
        h *= 1000003;
        h ^= (artists == null) ? 0 : artists.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Track> {
        final Artist.Mapper artistFieldMapper = new Artist.Mapper();

        final Field[] fields = {
          Field.forInt("id", "id", null, false),
          Field.forString("name", "name", null, false),
          Field.forString("spotify_track_id", "spotify_track_id", null, true),
          Field.forList("artists", "artists", null, true, new Field.ObjectReader<Artist>() {
            @Override public Artist read(final ResponseReader reader) throws IOException {
              return artistFieldMapper.map(reader);
            }
          })
        };

        @Override
        public Track map(ResponseReader reader) throws IOException {
          final int id = reader.read(fields[0]);
          final String name = reader.read(fields[1]);
          final String spotify_track_id = reader.read(fields[2]);
          final List<Artist> artists = reader.read(fields[3]);
          return new Track(id, name, spotify_track_id, artists);
        }
      }
    }

    public static class Playlist {
      private final int id;

      private final @Nonnull String name;

      private final @Nullable List<Track> tracks;

      public Playlist(int id, @Nonnull String name, @Nullable List<Track> tracks) {
        this.id = id;
        this.name = name;
        this.tracks = tracks;
      }

      public int id() {
        return this.id;
      }

      public @Nonnull String name() {
        return this.name;
      }

      public @Nullable List<Track> tracks() {
        return this.tracks;
      }

      @Override
      public String toString() {
        return "Playlist{"
          + "id=" + id + ", "
          + "name=" + name + ", "
          + "tracks=" + tracks
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Playlist) {
          Playlist that = (Playlist) o;
          return this.id == that.id
           && ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
           && ((this.tracks == null) ? (that.tracks == null) : this.tracks.equals(that.tracks));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= id;
        h *= 1000003;
        h ^= (name == null) ? 0 : name.hashCode();
        h *= 1000003;
        h ^= (tracks == null) ? 0 : tracks.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Playlist> {
        final Track.Mapper trackFieldMapper = new Track.Mapper();

        final Field[] fields = {
          Field.forInt("id", "id", null, false),
          Field.forString("name", "name", null, false),
          Field.forList("tracks", "tracks", null, true, new Field.ObjectReader<Track>() {
            @Override public Track read(final ResponseReader reader) throws IOException {
              return trackFieldMapper.map(reader);
            }
          })
        };

        @Override
        public Playlist map(ResponseReader reader) throws IOException {
          final int id = reader.read(fields[0]);
          final String name = reader.read(fields[1]);
          final List<Track> tracks = reader.read(fields[2]);
          return new Playlist(id, name, tracks);
        }
      }
    }
  }
}
