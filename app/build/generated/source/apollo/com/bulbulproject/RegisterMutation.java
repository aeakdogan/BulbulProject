package com.bulbulproject;

import com.apollographql.apollo.api.Field;
import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Operation;
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
public final class RegisterMutation implements Mutation<RegisterMutation.Data, RegisterMutation.Data, RegisterMutation.Variables> {
  public static final String OPERATION_DEFINITION = "mutation registerMutation($username: String!, $email: String!, $password: String!) {\n"
      + "  register(username: $username, password: $password, email: $email)\n"
      + "}";

  public static final String QUERY_DOCUMENT = OPERATION_DEFINITION;

  private final RegisterMutation.Variables variables;

  public RegisterMutation(@Nonnull String username, @Nonnull String email,
      @Nonnull String password) {
    Utils.checkNotNull(username, "username == null");
    Utils.checkNotNull(email, "email == null");
    Utils.checkNotNull(password, "password == null");
    variables = new RegisterMutation.Variables(username, email, password);
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public RegisterMutation.Data wrapData(RegisterMutation.Data data) {
    return data;
  }

  @Override
  public RegisterMutation.Variables variables() {
    return variables;
  }

  @Override
  public ResponseFieldMapper<RegisterMutation.Data> responseFieldMapper() {
    return new Data.Mapper();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Variables extends Operation.Variables {
    private final @Nonnull String username;

    private final @Nonnull String email;

    private final @Nonnull String password;

    private final transient Map<String, Object> valueMap = new LinkedHashMap<>();

    Variables(@Nonnull String username, @Nonnull String email, @Nonnull String password) {
      this.username = username;
      this.email = email;
      this.password = password;
      this.valueMap.put("username", username);
      this.valueMap.put("email", email);
      this.valueMap.put("password", password);
    }

    public @Nonnull String username() {
      return username;
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
    private @Nonnull String username;

    private @Nonnull String email;

    private @Nonnull String password;

    Builder() {
    }

    public Builder username(@Nonnull String username) {
      this.username = username;
      return this;
    }

    public Builder email(@Nonnull String email) {
      this.email = email;
      return this;
    }

    public Builder password(@Nonnull String password) {
      this.password = password;
      return this;
    }

    public RegisterMutation build() {
      if (username == null) throw new IllegalStateException("username can't be null");
      if (email == null) throw new IllegalStateException("email can't be null");
      if (password == null) throw new IllegalStateException("password can't be null");
      return new RegisterMutation(username, email, password);
    }
  }

  public static class Data implements Operation.Data {
    private final @Nullable String register;

    public Data(@Nullable String register) {
      this.register = register;
    }

    public @Nullable String register() {
      return this.register;
    }

    @Override
    public String toString() {
      return "Data{"
        + "register=" + register
        + "}";
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o instanceof Data) {
        Data that = (Data) o;
        return ((this.register == null) ? (that.register == null) : this.register.equals(that.register));
      }
      return false;
    }

    @Override
    public int hashCode() {
      int h = 1;
      h *= 1000003;
      h ^= (register == null) ? 0 : register.hashCode();
      return h;
    }

    public static final class Mapper implements ResponseFieldMapper<Data> {
      final Field[] fields = {
        Field.forString("register", "register", new UnmodifiableMapBuilder<String, Object>(3)
          .put("password", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "password")
          .build())
          .put("email", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "email")
          .build())
          .put("username", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "username")
          .build())
        .build(), true)
      };

      @Override
      public Data map(ResponseReader reader) throws IOException {
        final String register = reader.read(fields[0]);
        return new Data(register);
      }
    }
  }
}
