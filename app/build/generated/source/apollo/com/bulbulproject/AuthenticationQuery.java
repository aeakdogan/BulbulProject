package com.bulbulproject;

import com.apollographql.apollo.api.Field;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.ResponseFieldMapper;
import com.apollographql.apollo.api.ResponseReader;
import com.apollographql.apollo.api.internal.UnmodifiableMapBuilder;
import com.apollographql.apollo.api.internal.Utils;
import java.io.IOException;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("Apollo GraphQL")
public final class AuthenticationQuery implements Query<AuthenticationQuery.Data, AuthenticationQuery.Data, AuthenticationQuery.Variables> {
  public static final String OPERATION_DEFINITION = "query authenticationQuery($email: String!, $password: String!) {\n"
      + "  authentication(email: $email, password: $password)\n"
      + "}";

  public static final String QUERY_DOCUMENT = OPERATION_DEFINITION;

  private final AuthenticationQuery.Variables variables;

  public AuthenticationQuery(@Nonnull String email, @Nonnull String password) {
    Utils.checkNotNull(email, "email == null");
    Utils.checkNotNull(password, "password == null");
    variables = new AuthenticationQuery.Variables(email, password);
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public AuthenticationQuery.Data wrapData(AuthenticationQuery.Data data) {
    return data;
  }

  @Override
  public AuthenticationQuery.Variables variables() {
    return variables;
  }

  @Override
  public ResponseFieldMapper<AuthenticationQuery.Data> responseFieldMapper() {
    return new Data.Mapper();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Variables extends Operation.Variables {
    private final @Nonnull String email;

    private final @Nonnull String password;

    private final transient Map<String, Object> valueMap = new LinkedHashMap<>();

    Variables(@Nonnull String email, @Nonnull String password) {
      this.email = email;
      this.password = password;
      this.valueMap.put("email", email);
      this.valueMap.put("password", password);
    }

    public @Nonnull String email() {
      return email;
    }

    public @Nonnull String password() {
      return password;
    }

    @Override
    public Map<String, Object> valueMap() {
      return Collections.unmodifiableMap(valueMap);
    }
  }

  public static final class Builder {
    private @Nonnull String email;

    private @Nonnull String password;

    Builder() {
    }

    public Builder email(@Nonnull String email) {
      this.email = email;
      return this;
    }

    public Builder password(@Nonnull String password) {
      this.password = password;
      return this;
    }

    public AuthenticationQuery build() {
      if (email == null) throw new IllegalStateException("email can't be null");
      if (password == null) throw new IllegalStateException("password can't be null");
      return new AuthenticationQuery(email, password);
    }
  }

  public static class Data implements Operation.Data {
    private final @Nullable String authentication;

    public Data(@Nullable String authentication) {
      this.authentication = authentication;
    }

    public @Nullable String authentication() {
      return this.authentication;
    }

    @Override
    public String toString() {
      return "Data{"
        + "authentication=" + authentication
        + "}";
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o instanceof Data) {
        Data that = (Data) o;
        return ((this.authentication == null) ? (that.authentication == null) : this.authentication.equals(that.authentication));
      }
      return false;
    }

    @Override
    public int hashCode() {
      int h = 1;
      h *= 1000003;
      h ^= (authentication == null) ? 0 : authentication.hashCode();
      return h;
    }

    public static final class Mapper implements ResponseFieldMapper<Data> {
      final Field[] fields = {
        Field.forString("authentication", "authentication", new UnmodifiableMapBuilder<String, Object>(2)
          .put("password", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "password")
          .build())
          .put("email", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "email")
          .build())
        .build(), true)
      };

      @Override
      public Data map(ResponseReader reader) throws IOException {
        final String authentication = reader.read(fields[0]);
        return new Data(authentication);
      }
    }
  }
}
