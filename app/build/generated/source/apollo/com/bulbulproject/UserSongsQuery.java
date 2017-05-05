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
public final class UserSongsQuery implements Query<UserSongsQuery.Data, UserSongsQuery.Data, UserSongsQuery.Variables> {
  public static final String OPERATION_DEFINITION = "query UserSongsQuery($token: String, $id: Int, $limit: Int, $skip: Int, $ids: [Int]) {\n"
      + "  users(token: $token, id: $id, limit: $limit, skip: $skip, ids: $ids) {\n"
      + "    __typename\n"
      + "    listenedTracks {\n"
      + "      __typename\n"
      + "      id\n"
      + "      spotify_track_id\n"
      + "      name\n"
      + "      artists {\n"
      + "        __typename\n"
      + "        name\n"
      + "      }\n"
      + "    }\n"
      + "  }\n"
      + "}";

  public static final String QUERY_DOCUMENT = OPERATION_DEFINITION;

  private final UserSongsQuery.Variables variables;

  public UserSongsQuery(@Nullable String token, @Nullable Integer id, @Nullable Integer limit,
      @Nullable Integer skip, @Nullable List<Integer> ids) {
    variables = new UserSongsQuery.Variables(token, id, limit, skip, ids);
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public UserSongsQuery.Data wrapData(UserSongsQuery.Data data) {
    return data;
  }

  @Override
  public UserSongsQuery.Variables variables() {
    return variables;
  }

  @Override
  public ResponseFieldMapper<UserSongsQuery.Data> responseFieldMapper() {
    return new Data.Mapper();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Variables extends Operation.Variables {
    private final @Nullable String token;

    private final @Nullable Integer id;

    private final @Nullable Integer limit;

    private final @Nullable Integer skip;

    private final @Nullable List<Integer> ids;

    private final transient Map<String, Object> valueMap = new LinkedHashMap<>();

    Variables(@Nullable String token, @Nullable Integer id, @Nullable Integer limit,
        @Nullable Integer skip, @Nullable List<Integer> ids) {
      this.token = token;
      this.id = id;
      this.limit = limit;
      this.skip = skip;
      this.ids = ids;
      this.valueMap.put("token", token);
      this.valueMap.put("id", id);
      this.valueMap.put("limit", limit);
      this.valueMap.put("skip", skip);
      this.valueMap.put("ids", ids);
    }

    public @Nullable String token() {
      return token;
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
    private @Nullable String token;

    private @Nullable Integer id;

    private @Nullable Integer limit;

    private @Nullable Integer skip;

    private @Nullable List<Integer> ids;

    Builder() {
    }

    public Builder token(@Nullable String token) {
      this.token = token;
      return this;
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

    public UserSongsQuery build() {
      return new UserSongsQuery(token, id, limit, skip, ids);
    }
  }

  public static class Data implements Operation.Data {
    private final @Nullable List<User> users;

    public Data(@Nullable List<User> users) {
      this.users = users;
    }

    public @Nullable List<User> users() {
      return this.users;
    }

    @Override
    public String toString() {
      return "Data{"
        + "users=" + users
        + "}";
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o instanceof Data) {
        Data that = (Data) o;
        return ((this.users == null) ? (that.users == null) : this.users.equals(that.users));
      }
      return false;
    }

    @Override
    public int hashCode() {
      int h = 1;
      h *= 1000003;
      h ^= (users == null) ? 0 : users.hashCode();
      return h;
    }

    public static final class Mapper implements ResponseFieldMapper<Data> {
      final User.Mapper userFieldMapper = new User.Mapper();

      final Field[] fields = {
        Field.forList("users", "users", new UnmodifiableMapBuilder<String, Object>(5)
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
          .put("token", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "token")
          .build())
        .build(), true, new Field.ObjectReader<User>() {
          @Override public User read(final ResponseReader reader) throws IOException {
            return userFieldMapper.map(reader);
          }
        })
      };

      @Override
      public Data map(ResponseReader reader) throws IOException {
        final List<User> users = reader.read(fields[0]);
        return new Data(users);
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

    public static class ListenedTrack {
      private final int id;

      private final @Nullable String spotify_track_id;

      private final @Nonnull String name;

      private final @Nullable List<Artist> artists;

      public ListenedTrack(int id, @Nullable String spotify_track_id, @Nonnull String name,
          @Nullable List<Artist> artists) {
        this.id = id;
        this.spotify_track_id = spotify_track_id;
        this.name = name;
        this.artists = artists;
      }

      public int id() {
        return this.id;
      }

      public @Nullable String spotify_track_id() {
        return this.spotify_track_id;
      }

      public @Nonnull String name() {
        return this.name;
      }

      public @Nullable List<Artist> artists() {
        return this.artists;
      }

      @Override
      public String toString() {
        return "ListenedTrack{"
          + "id=" + id + ", "
          + "spotify_track_id=" + spotify_track_id + ", "
          + "name=" + name + ", "
          + "artists=" + artists
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof ListenedTrack) {
          ListenedTrack that = (ListenedTrack) o;
          return this.id == that.id
           && ((this.spotify_track_id == null) ? (that.spotify_track_id == null) : this.spotify_track_id.equals(that.spotify_track_id))
           && ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
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
        h ^= (spotify_track_id == null) ? 0 : spotify_track_id.hashCode();
        h *= 1000003;
        h ^= (name == null) ? 0 : name.hashCode();
        h *= 1000003;
        h ^= (artists == null) ? 0 : artists.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<ListenedTrack> {
        final Artist.Mapper artistFieldMapper = new Artist.Mapper();

        final Field[] fields = {
          Field.forInt("id", "id", null, false),
          Field.forString("spotify_track_id", "spotify_track_id", null, true),
          Field.forString("name", "name", null, false),
          Field.forList("artists", "artists", null, true, new Field.ObjectReader<Artist>() {
            @Override public Artist read(final ResponseReader reader) throws IOException {
              return artistFieldMapper.map(reader);
            }
          })
        };

        @Override
        public ListenedTrack map(ResponseReader reader) throws IOException {
          final int id = reader.read(fields[0]);
          final String spotify_track_id = reader.read(fields[1]);
          final String name = reader.read(fields[2]);
          final List<Artist> artists = reader.read(fields[3]);
          return new ListenedTrack(id, spotify_track_id, name, artists);
        }
      }
    }

    public static class User {
      private final @Nullable List<ListenedTrack> listenedTracks;

      public User(@Nullable List<ListenedTrack> listenedTracks) {
        this.listenedTracks = listenedTracks;
      }

      public @Nullable List<ListenedTrack> listenedTracks() {
        return this.listenedTracks;
      }

      @Override
      public String toString() {
        return "User{"
          + "listenedTracks=" + listenedTracks
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof User) {
          User that = (User) o;
          return ((this.listenedTracks == null) ? (that.listenedTracks == null) : this.listenedTracks.equals(that.listenedTracks));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (listenedTracks == null) ? 0 : listenedTracks.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<User> {
        final ListenedTrack.Mapper listenedTrackFieldMapper = new ListenedTrack.Mapper();

        final Field[] fields = {
          Field.forList("listenedTracks", "listenedTracks", null, true, new Field.ObjectReader<ListenedTrack>() {
            @Override public ListenedTrack read(final ResponseReader reader) throws IOException {
              return listenedTrackFieldMapper.map(reader);
            }
          })
        };

        @Override
        public User map(ResponseReader reader) throws IOException {
          final List<ListenedTrack> listenedTracks = reader.read(fields[0]);
          return new User(listenedTracks);
        }
      }
    }
  }
}
