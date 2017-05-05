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
public final class FollowersQuery implements Query<FollowersQuery.Data, FollowersQuery.Data, FollowersQuery.Variables> {
  public static final String OPERATION_DEFINITION = "query FollowersQuery($token: String, $id: Int, $limit: Int, $skip: Int, $ids: [Int]) {\n"
      + "  users(token: $token, id: $id, limit: $limit, skip: $skip, ids: $ids) {\n"
      + "    __typename\n"
      + "    followers {\n"
      + "      __typename\n"
      + "      id\n"
      + "      username\n"
      + "      image\n"
      + "    }\n"
      + "  }\n"
      + "}";

  public static final String QUERY_DOCUMENT = OPERATION_DEFINITION;

  private final FollowersQuery.Variables variables;

  public FollowersQuery(@Nullable String token, @Nullable Integer id, @Nullable Integer limit,
      @Nullable Integer skip, @Nullable List<Integer> ids) {
    variables = new FollowersQuery.Variables(token, id, limit, skip, ids);
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public FollowersQuery.Data wrapData(FollowersQuery.Data data) {
    return data;
  }

  @Override
  public FollowersQuery.Variables variables() {
    return variables;
  }

  @Override
  public ResponseFieldMapper<FollowersQuery.Data> responseFieldMapper() {
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

    public FollowersQuery build() {
      return new FollowersQuery(token, id, limit, skip, ids);
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

    public static class Follower {
      private final int id;

      private final @Nonnull String username;

      private final @Nullable String image;

      public Follower(int id, @Nonnull String username, @Nullable String image) {
        this.id = id;
        this.username = username;
        this.image = image;
      }

      public int id() {
        return this.id;
      }

      public @Nonnull String username() {
        return this.username;
      }

      public @Nullable String image() {
        return this.image;
      }

      @Override
      public String toString() {
        return "Follower{"
          + "id=" + id + ", "
          + "username=" + username + ", "
          + "image=" + image
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Follower) {
          Follower that = (Follower) o;
          return this.id == that.id
           && ((this.username == null) ? (that.username == null) : this.username.equals(that.username))
           && ((this.image == null) ? (that.image == null) : this.image.equals(that.image));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= id;
        h *= 1000003;
        h ^= (username == null) ? 0 : username.hashCode();
        h *= 1000003;
        h ^= (image == null) ? 0 : image.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Follower> {
        final Field[] fields = {
          Field.forInt("id", "id", null, false),
          Field.forString("username", "username", null, false),
          Field.forString("image", "image", null, true)
        };

        @Override
        public Follower map(ResponseReader reader) throws IOException {
          final int id = reader.read(fields[0]);
          final String username = reader.read(fields[1]);
          final String image = reader.read(fields[2]);
          return new Follower(id, username, image);
        }
      }
    }

    public static class User {
      private final @Nullable List<Follower> followers;

      public User(@Nullable List<Follower> followers) {
        this.followers = followers;
      }

      public @Nullable List<Follower> followers() {
        return this.followers;
      }

      @Override
      public String toString() {
        return "User{"
          + "followers=" + followers
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof User) {
          User that = (User) o;
          return ((this.followers == null) ? (that.followers == null) : this.followers.equals(that.followers));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (followers == null) ? 0 : followers.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<User> {
        final Follower.Mapper followerFieldMapper = new Follower.Mapper();

        final Field[] fields = {
          Field.forList("followers", "followers", null, true, new Field.ObjectReader<Follower>() {
            @Override public Follower read(final ResponseReader reader) throws IOException {
              return followerFieldMapper.map(reader);
            }
          })
        };

        @Override
        public User map(ResponseReader reader) throws IOException {
          final List<Follower> followers = reader.read(fields[0]);
          return new User(followers);
        }
      }
    }
  }
}
