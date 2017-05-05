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
public final class UserArtistsQuery implements Query<UserArtistsQuery.Data, UserArtistsQuery.Data, UserArtistsQuery.Variables> {
  public static final String OPERATION_DEFINITION = "query UserArtistsQuery($token: String, $id: Int, $limit: Int, $skip: Int, $ids: [Int]) {\n"
      + "  users(token: $token, id: $id, limit: $limit, skip: $skip, ids: $ids) {\n"
      + "    __typename\n"
      + "    listenedArtists {\n"
      + "      __typename\n"
      + "      id\n"
      + "      name\n"
      + "      image\n"
      + "      albumsCount\n"
      + "    }\n"
      + "  }\n"
      + "}";

  public static final String QUERY_DOCUMENT = OPERATION_DEFINITION;

  private final UserArtistsQuery.Variables variables;

  public UserArtistsQuery(@Nullable String token, @Nullable Integer id, @Nullable Integer limit,
      @Nullable Integer skip, @Nullable List<Integer> ids) {
    variables = new UserArtistsQuery.Variables(token, id, limit, skip, ids);
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public UserArtistsQuery.Data wrapData(UserArtistsQuery.Data data) {
    return data;
  }

  @Override
  public UserArtistsQuery.Variables variables() {
    return variables;
  }

  @Override
  public ResponseFieldMapper<UserArtistsQuery.Data> responseFieldMapper() {
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

    public UserArtistsQuery build() {
      return new UserArtistsQuery(token, id, limit, skip, ids);
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

    public static class ListenedArtist {
      private final int id;

      private final @Nonnull String name;

      private final @Nullable String image;

      private final @Nullable Integer albumsCount;

      public ListenedArtist(int id, @Nonnull String name, @Nullable String image,
          @Nullable Integer albumsCount) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.albumsCount = albumsCount;
      }

      public int id() {
        return this.id;
      }

      public @Nonnull String name() {
        return this.name;
      }

      public @Nullable String image() {
        return this.image;
      }

      public @Nullable Integer albumsCount() {
        return this.albumsCount;
      }

      @Override
      public String toString() {
        return "ListenedArtist{"
          + "id=" + id + ", "
          + "name=" + name + ", "
          + "image=" + image + ", "
          + "albumsCount=" + albumsCount
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof ListenedArtist) {
          ListenedArtist that = (ListenedArtist) o;
          return this.id == that.id
           && ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
           && ((this.image == null) ? (that.image == null) : this.image.equals(that.image))
           && ((this.albumsCount == null) ? (that.albumsCount == null) : this.albumsCount.equals(that.albumsCount));
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
        h ^= (image == null) ? 0 : image.hashCode();
        h *= 1000003;
        h ^= (albumsCount == null) ? 0 : albumsCount.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<ListenedArtist> {
        final Field[] fields = {
          Field.forInt("id", "id", null, false),
          Field.forString("name", "name", null, false),
          Field.forString("image", "image", null, true),
          Field.forInt("albumsCount", "albumsCount", null, true)
        };

        @Override
        public ListenedArtist map(ResponseReader reader) throws IOException {
          final int id = reader.read(fields[0]);
          final String name = reader.read(fields[1]);
          final String image = reader.read(fields[2]);
          final Integer albumsCount = reader.read(fields[3]);
          return new ListenedArtist(id, name, image, albumsCount);
        }
      }
    }

    public static class User {
      private final @Nullable List<ListenedArtist> listenedArtists;

      public User(@Nullable List<ListenedArtist> listenedArtists) {
        this.listenedArtists = listenedArtists;
      }

      public @Nullable List<ListenedArtist> listenedArtists() {
        return this.listenedArtists;
      }

      @Override
      public String toString() {
        return "User{"
          + "listenedArtists=" + listenedArtists
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof User) {
          User that = (User) o;
          return ((this.listenedArtists == null) ? (that.listenedArtists == null) : this.listenedArtists.equals(that.listenedArtists));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (listenedArtists == null) ? 0 : listenedArtists.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<User> {
        final ListenedArtist.Mapper listenedArtistFieldMapper = new ListenedArtist.Mapper();

        final Field[] fields = {
          Field.forList("listenedArtists", "listenedArtists", null, true, new Field.ObjectReader<ListenedArtist>() {
            @Override public ListenedArtist read(final ResponseReader reader) throws IOException {
              return listenedArtistFieldMapper.map(reader);
            }
          })
        };

        @Override
        public User map(ResponseReader reader) throws IOException {
          final List<ListenedArtist> listenedArtists = reader.read(fields[0]);
          return new User(listenedArtists);
        }
      }
    }
  }
}
