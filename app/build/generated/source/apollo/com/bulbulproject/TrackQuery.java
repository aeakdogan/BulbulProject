package com.bulbulproject;

import com.apollographql.apollo.api.Field;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.ResponseFieldMapper;
import com.apollographql.apollo.api.ResponseReader;
import com.apollographql.apollo.api.internal.UnmodifiableMapBuilder;
import java.io.IOException;
import java.lang.Double;
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
public final class TrackQuery implements Query<TrackQuery.Data, TrackQuery.Data, TrackQuery.Variables> {
  public static final String OPERATION_DEFINITION = "query TrackQuery($id: Int, $limit: Int, $skip: Int, $ids: [Int], $min_acousticness: Float, $max_acousticness: Float, $min_liveness: Float, $max_liveness: Float, $min_speechiness: Float, $max_speechiness: Float, $min_valence: Float, $max_valence: Float, $min_danceability: Float, $max_danceability: Float, $min_instrumentalness: Float, $max_instrumentalness: Float, $min_tempo: Float, $max_tempo: Float, $min_energy: Float, $max_energy: Float, $min_loudness: Float, $max_loudness: Float) {\n"
      + "  tracks(id: $id, limit: $limit, skip: $skip, ids: $ids, min_acousticness: $min_acousticness, max_acousticness: $max_acousticness, min_liveness: $min_liveness, max_liveness: $max_liveness, min_speechiness: $min_speechiness, max_speechiness: $max_speechiness, min_valence: $min_valence, max_valence: $max_valence, min_danceability: $min_danceability, max_danceability: $max_danceability, min_instrumentalness: $min_instrumentalness, max_instrumentalness: $max_instrumentalness, min_tempo: $min_tempo, max_tempo: $max_tempo, min_energy: $min_energy, max_energy: $max_energy, min_loudness: $min_loudness, max_loudness: $max_loudness) {\n"
      + "    __typename\n"
      + "    id\n"
      + "    name\n"
      + "    artists {\n"
      + "      __typename\n"
      + "      id\n"
      + "      name\n"
      + "      tags {\n"
      + "        __typename\n"
      + "        tag_name\n"
      + "      }\n"
      + "      image\n"
      + "      biography_text\n"
      + "      lastfm_url\n"
      + "      mbid\n"
      + "      listener_count\n"
      + "      play_count\n"
      + "      biography_url\n"
      + "    }\n"
      + "    albums {\n"
      + "      __typename\n"
      + "      id\n"
      + "      name\n"
      + "      image\n"
      + "    }\n"
      + "    spotify_album_img\n"
      + "    spotify_track_preview_url\n"
      + "    duration\n"
      + "    spotify_track_id\n"
      + "  }\n"
      + "}";

  public static final String QUERY_DOCUMENT = OPERATION_DEFINITION;

  private final TrackQuery.Variables variables;

  public TrackQuery(@Nullable Integer id, @Nullable Integer limit, @Nullable Integer skip,
      @Nullable List<Integer> ids, @Nullable Double min_acousticness,
      @Nullable Double max_acousticness, @Nullable Double min_liveness,
      @Nullable Double max_liveness, @Nullable Double min_speechiness,
      @Nullable Double max_speechiness, @Nullable Double min_valence, @Nullable Double max_valence,
      @Nullable Double min_danceability, @Nullable Double max_danceability,
      @Nullable Double min_instrumentalness, @Nullable Double max_instrumentalness,
      @Nullable Double min_tempo, @Nullable Double max_tempo, @Nullable Double min_energy,
      @Nullable Double max_energy, @Nullable Double min_loudness, @Nullable Double max_loudness) {
    variables = new TrackQuery.Variables(id, limit, skip, ids, min_acousticness, max_acousticness, min_liveness, max_liveness, min_speechiness, max_speechiness, min_valence, max_valence, min_danceability, max_danceability, min_instrumentalness, max_instrumentalness, min_tempo, max_tempo, min_energy, max_energy, min_loudness, max_loudness);
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public TrackQuery.Data wrapData(TrackQuery.Data data) {
    return data;
  }

  @Override
  public TrackQuery.Variables variables() {
    return variables;
  }

  @Override
  public ResponseFieldMapper<TrackQuery.Data> responseFieldMapper() {
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

    private final @Nullable Double min_acousticness;

    private final @Nullable Double max_acousticness;

    private final @Nullable Double min_liveness;

    private final @Nullable Double max_liveness;

    private final @Nullable Double min_speechiness;

    private final @Nullable Double max_speechiness;

    private final @Nullable Double min_valence;

    private final @Nullable Double max_valence;

    private final @Nullable Double min_danceability;

    private final @Nullable Double max_danceability;

    private final @Nullable Double min_instrumentalness;

    private final @Nullable Double max_instrumentalness;

    private final @Nullable Double min_tempo;

    private final @Nullable Double max_tempo;

    private final @Nullable Double min_energy;

    private final @Nullable Double max_energy;

    private final @Nullable Double min_loudness;

    private final @Nullable Double max_loudness;

    private final transient Map<String, Object> valueMap = new LinkedHashMap<>();

    Variables(@Nullable Integer id, @Nullable Integer limit, @Nullable Integer skip,
        @Nullable List<Integer> ids, @Nullable Double min_acousticness,
        @Nullable Double max_acousticness, @Nullable Double min_liveness,
        @Nullable Double max_liveness, @Nullable Double min_speechiness,
        @Nullable Double max_speechiness, @Nullable Double min_valence,
        @Nullable Double max_valence, @Nullable Double min_danceability,
        @Nullable Double max_danceability, @Nullable Double min_instrumentalness,
        @Nullable Double max_instrumentalness, @Nullable Double min_tempo,
        @Nullable Double max_tempo, @Nullable Double min_energy, @Nullable Double max_energy,
        @Nullable Double min_loudness, @Nullable Double max_loudness) {
      this.id = id;
      this.limit = limit;
      this.skip = skip;
      this.ids = ids;
      this.min_acousticness = min_acousticness;
      this.max_acousticness = max_acousticness;
      this.min_liveness = min_liveness;
      this.max_liveness = max_liveness;
      this.min_speechiness = min_speechiness;
      this.max_speechiness = max_speechiness;
      this.min_valence = min_valence;
      this.max_valence = max_valence;
      this.min_danceability = min_danceability;
      this.max_danceability = max_danceability;
      this.min_instrumentalness = min_instrumentalness;
      this.max_instrumentalness = max_instrumentalness;
      this.min_tempo = min_tempo;
      this.max_tempo = max_tempo;
      this.min_energy = min_energy;
      this.max_energy = max_energy;
      this.min_loudness = min_loudness;
      this.max_loudness = max_loudness;
      this.valueMap.put("id", id);
      this.valueMap.put("limit", limit);
      this.valueMap.put("skip", skip);
      this.valueMap.put("ids", ids);
      this.valueMap.put("min_acousticness", min_acousticness);
      this.valueMap.put("max_acousticness", max_acousticness);
      this.valueMap.put("min_liveness", min_liveness);
      this.valueMap.put("max_liveness", max_liveness);
      this.valueMap.put("min_speechiness", min_speechiness);
      this.valueMap.put("max_speechiness", max_speechiness);
      this.valueMap.put("min_valence", min_valence);
      this.valueMap.put("max_valence", max_valence);
      this.valueMap.put("min_danceability", min_danceability);
      this.valueMap.put("max_danceability", max_danceability);
      this.valueMap.put("min_instrumentalness", min_instrumentalness);
      this.valueMap.put("max_instrumentalness", max_instrumentalness);
      this.valueMap.put("min_tempo", min_tempo);
      this.valueMap.put("max_tempo", max_tempo);
      this.valueMap.put("min_energy", min_energy);
      this.valueMap.put("max_energy", max_energy);
      this.valueMap.put("min_loudness", min_loudness);
      this.valueMap.put("max_loudness", max_loudness);
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

    public @Nullable Double min_acousticness() {
      return min_acousticness;
    }

    public @Nullable Double max_acousticness() {
      return max_acousticness;
    }

    public @Nullable Double min_liveness() {
      return min_liveness;
    }

    public @Nullable Double max_liveness() {
      return max_liveness;
    }

    public @Nullable Double min_speechiness() {
      return min_speechiness;
    }

    public @Nullable Double max_speechiness() {
      return max_speechiness;
    }

    public @Nullable Double min_valence() {
      return min_valence;
    }

    public @Nullable Double max_valence() {
      return max_valence;
    }

    public @Nullable Double min_danceability() {
      return min_danceability;
    }

    public @Nullable Double max_danceability() {
      return max_danceability;
    }

    public @Nullable Double min_instrumentalness() {
      return min_instrumentalness;
    }

    public @Nullable Double max_instrumentalness() {
      return max_instrumentalness;
    }

    public @Nullable Double min_tempo() {
      return min_tempo;
    }

    public @Nullable Double max_tempo() {
      return max_tempo;
    }

    public @Nullable Double min_energy() {
      return min_energy;
    }

    public @Nullable Double max_energy() {
      return max_energy;
    }

    public @Nullable Double min_loudness() {
      return min_loudness;
    }

    public @Nullable Double max_loudness() {
      return max_loudness;
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

    private @Nullable Double min_acousticness;

    private @Nullable Double max_acousticness;

    private @Nullable Double min_liveness;

    private @Nullable Double max_liveness;

    private @Nullable Double min_speechiness;

    private @Nullable Double max_speechiness;

    private @Nullable Double min_valence;

    private @Nullable Double max_valence;

    private @Nullable Double min_danceability;

    private @Nullable Double max_danceability;

    private @Nullable Double min_instrumentalness;

    private @Nullable Double max_instrumentalness;

    private @Nullable Double min_tempo;

    private @Nullable Double max_tempo;

    private @Nullable Double min_energy;

    private @Nullable Double max_energy;

    private @Nullable Double min_loudness;

    private @Nullable Double max_loudness;

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

    public Builder min_acousticness(@Nullable Double min_acousticness) {
      this.min_acousticness = min_acousticness;
      return this;
    }

    public Builder max_acousticness(@Nullable Double max_acousticness) {
      this.max_acousticness = max_acousticness;
      return this;
    }

    public Builder min_liveness(@Nullable Double min_liveness) {
      this.min_liveness = min_liveness;
      return this;
    }

    public Builder max_liveness(@Nullable Double max_liveness) {
      this.max_liveness = max_liveness;
      return this;
    }

    public Builder min_speechiness(@Nullable Double min_speechiness) {
      this.min_speechiness = min_speechiness;
      return this;
    }

    public Builder max_speechiness(@Nullable Double max_speechiness) {
      this.max_speechiness = max_speechiness;
      return this;
    }

    public Builder min_valence(@Nullable Double min_valence) {
      this.min_valence = min_valence;
      return this;
    }

    public Builder max_valence(@Nullable Double max_valence) {
      this.max_valence = max_valence;
      return this;
    }

    public Builder min_danceability(@Nullable Double min_danceability) {
      this.min_danceability = min_danceability;
      return this;
    }

    public Builder max_danceability(@Nullable Double max_danceability) {
      this.max_danceability = max_danceability;
      return this;
    }

    public Builder min_instrumentalness(@Nullable Double min_instrumentalness) {
      this.min_instrumentalness = min_instrumentalness;
      return this;
    }

    public Builder max_instrumentalness(@Nullable Double max_instrumentalness) {
      this.max_instrumentalness = max_instrumentalness;
      return this;
    }

    public Builder min_tempo(@Nullable Double min_tempo) {
      this.min_tempo = min_tempo;
      return this;
    }

    public Builder max_tempo(@Nullable Double max_tempo) {
      this.max_tempo = max_tempo;
      return this;
    }

    public Builder min_energy(@Nullable Double min_energy) {
      this.min_energy = min_energy;
      return this;
    }

    public Builder max_energy(@Nullable Double max_energy) {
      this.max_energy = max_energy;
      return this;
    }

    public Builder min_loudness(@Nullable Double min_loudness) {
      this.min_loudness = min_loudness;
      return this;
    }

    public Builder max_loudness(@Nullable Double max_loudness) {
      this.max_loudness = max_loudness;
      return this;
    }

    public TrackQuery build() {
      return new TrackQuery(id, limit, skip, ids, min_acousticness, max_acousticness, min_liveness, max_liveness, min_speechiness, max_speechiness, min_valence, max_valence, min_danceability, max_danceability, min_instrumentalness, max_instrumentalness, min_tempo, max_tempo, min_energy, max_energy, min_loudness, max_loudness);
    }
  }

  public static class Data implements Operation.Data {
    private final @Nullable List<Track> tracks;

    public Data(@Nullable List<Track> tracks) {
      this.tracks = tracks;
    }

    public @Nullable List<Track> tracks() {
      return this.tracks;
    }

    @Override
    public String toString() {
      return "Data{"
        + "tracks=" + tracks
        + "}";
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o instanceof Data) {
        Data that = (Data) o;
        return ((this.tracks == null) ? (that.tracks == null) : this.tracks.equals(that.tracks));
      }
      return false;
    }

    @Override
    public int hashCode() {
      int h = 1;
      h *= 1000003;
      h ^= (tracks == null) ? 0 : tracks.hashCode();
      return h;
    }

    public static final class Mapper implements ResponseFieldMapper<Data> {
      final Track.Mapper trackFieldMapper = new Track.Mapper();

      final Field[] fields = {
        Field.forList("tracks", "tracks", new UnmodifiableMapBuilder<String, Object>(22)
          .put("min_liveness", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "min_liveness")
          .build())
          .put("max_instrumentalness", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "max_instrumentalness")
          .build())
          .put("min_valence", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "min_valence")
          .build())
          .put("min_danceability", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "min_danceability")
          .build())
          .put("max_energy", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "max_energy")
          .build())
          .put("min_loudness", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "min_loudness")
          .build())
          .put("skip", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "skip")
          .build())
          .put("min_energy", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "min_energy")
          .build())
          .put("max_valence", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "max_valence")
          .build())
          .put("max_tempo", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "max_tempo")
          .build())
          .put("max_loudness", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "max_loudness")
          .build())
          .put("min_instrumentalness", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "min_instrumentalness")
          .build())
          .put("min_acousticness", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "min_acousticness")
          .build())
          .put("max_acousticness", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "max_acousticness")
          .build())
          .put("min_tempo", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "min_tempo")
          .build())
          .put("max_liveness", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "max_liveness")
          .build())
          .put("limit", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "limit")
          .build())
          .put("ids", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "ids")
          .build())
          .put("id", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "id")
          .build())
          .put("min_speechiness", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "min_speechiness")
          .build())
          .put("max_danceability", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "max_danceability")
          .build())
          .put("max_speechiness", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "max_speechiness")
          .build())
        .build(), true, new Field.ObjectReader<Track>() {
          @Override public Track read(final ResponseReader reader) throws IOException {
            return trackFieldMapper.map(reader);
          }
        })
      };

      @Override
      public Data map(ResponseReader reader) throws IOException {
        final List<Track> tracks = reader.read(fields[0]);
        return new Data(tracks);
      }
    }

    public static class Tag {
      private final @Nonnull String tag_name;

      public Tag(@Nonnull String tag_name) {
        this.tag_name = tag_name;
      }

      public @Nonnull String tag_name() {
        return this.tag_name;
      }

      @Override
      public String toString() {
        return "Tag{"
          + "tag_name=" + tag_name
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Tag) {
          Tag that = (Tag) o;
          return ((this.tag_name == null) ? (that.tag_name == null) : this.tag_name.equals(that.tag_name));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (tag_name == null) ? 0 : tag_name.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Tag> {
        final Field[] fields = {
          Field.forString("tag_name", "tag_name", null, false)
        };

        @Override
        public Tag map(ResponseReader reader) throws IOException {
          final String tag_name = reader.read(fields[0]);
          return new Tag(tag_name);
        }
      }
    }

    public static class Artist {
      private final int id;

      private final @Nonnull String name;

      private final @Nullable List<Tag> tags;

      private final @Nullable String image;

      private final @Nullable String biography_text;

      private final @Nullable String lastfm_url;

      private final @Nullable String mbid;

      private final @Nullable Integer listener_count;

      private final @Nullable Integer play_count;

      private final @Nullable String biography_url;

      public Artist(int id, @Nonnull String name, @Nullable List<Tag> tags, @Nullable String image,
          @Nullable String biography_text, @Nullable String lastfm_url, @Nullable String mbid,
          @Nullable Integer listener_count, @Nullable Integer play_count,
          @Nullable String biography_url) {
        this.id = id;
        this.name = name;
        this.tags = tags;
        this.image = image;
        this.biography_text = biography_text;
        this.lastfm_url = lastfm_url;
        this.mbid = mbid;
        this.listener_count = listener_count;
        this.play_count = play_count;
        this.biography_url = biography_url;
      }

      public int id() {
        return this.id;
      }

      public @Nonnull String name() {
        return this.name;
      }

      public @Nullable List<Tag> tags() {
        return this.tags;
      }

      public @Nullable String image() {
        return this.image;
      }

      public @Nullable String biography_text() {
        return this.biography_text;
      }

      public @Nullable String lastfm_url() {
        return this.lastfm_url;
      }

      public @Nullable String mbid() {
        return this.mbid;
      }

      public @Nullable Integer listener_count() {
        return this.listener_count;
      }

      public @Nullable Integer play_count() {
        return this.play_count;
      }

      public @Nullable String biography_url() {
        return this.biography_url;
      }

      @Override
      public String toString() {
        return "Artist{"
          + "id=" + id + ", "
          + "name=" + name + ", "
          + "tags=" + tags + ", "
          + "image=" + image + ", "
          + "biography_text=" + biography_text + ", "
          + "lastfm_url=" + lastfm_url + ", "
          + "mbid=" + mbid + ", "
          + "listener_count=" + listener_count + ", "
          + "play_count=" + play_count + ", "
          + "biography_url=" + biography_url
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Artist) {
          Artist that = (Artist) o;
          return this.id == that.id
           && ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
           && ((this.tags == null) ? (that.tags == null) : this.tags.equals(that.tags))
           && ((this.image == null) ? (that.image == null) : this.image.equals(that.image))
           && ((this.biography_text == null) ? (that.biography_text == null) : this.biography_text.equals(that.biography_text))
           && ((this.lastfm_url == null) ? (that.lastfm_url == null) : this.lastfm_url.equals(that.lastfm_url))
           && ((this.mbid == null) ? (that.mbid == null) : this.mbid.equals(that.mbid))
           && ((this.listener_count == null) ? (that.listener_count == null) : this.listener_count.equals(that.listener_count))
           && ((this.play_count == null) ? (that.play_count == null) : this.play_count.equals(that.play_count))
           && ((this.biography_url == null) ? (that.biography_url == null) : this.biography_url.equals(that.biography_url));
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
        h ^= (tags == null) ? 0 : tags.hashCode();
        h *= 1000003;
        h ^= (image == null) ? 0 : image.hashCode();
        h *= 1000003;
        h ^= (biography_text == null) ? 0 : biography_text.hashCode();
        h *= 1000003;
        h ^= (lastfm_url == null) ? 0 : lastfm_url.hashCode();
        h *= 1000003;
        h ^= (mbid == null) ? 0 : mbid.hashCode();
        h *= 1000003;
        h ^= (listener_count == null) ? 0 : listener_count.hashCode();
        h *= 1000003;
        h ^= (play_count == null) ? 0 : play_count.hashCode();
        h *= 1000003;
        h ^= (biography_url == null) ? 0 : biography_url.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Artist> {
        final Tag.Mapper tagFieldMapper = new Tag.Mapper();

        final Field[] fields = {
          Field.forInt("id", "id", null, false),
          Field.forString("name", "name", null, false),
          Field.forList("tags", "tags", null, true, new Field.ObjectReader<Tag>() {
            @Override public Tag read(final ResponseReader reader) throws IOException {
              return tagFieldMapper.map(reader);
            }
          }),
          Field.forString("image", "image", null, true),
          Field.forString("biography_text", "biography_text", null, true),
          Field.forString("lastfm_url", "lastfm_url", null, true),
          Field.forString("mbid", "mbid", null, true),
          Field.forInt("listener_count", "listener_count", null, true),
          Field.forInt("play_count", "play_count", null, true),
          Field.forString("biography_url", "biography_url", null, true)
        };

        @Override
        public Artist map(ResponseReader reader) throws IOException {
          final int id = reader.read(fields[0]);
          final String name = reader.read(fields[1]);
          final List<Tag> tags = reader.read(fields[2]);
          final String image = reader.read(fields[3]);
          final String biography_text = reader.read(fields[4]);
          final String lastfm_url = reader.read(fields[5]);
          final String mbid = reader.read(fields[6]);
          final Integer listener_count = reader.read(fields[7]);
          final Integer play_count = reader.read(fields[8]);
          final String biography_url = reader.read(fields[9]);
          return new Artist(id, name, tags, image, biography_text, lastfm_url, mbid, listener_count, play_count, biography_url);
        }
      }
    }

    public static class Album {
      private final int id;

      private final @Nonnull String name;

      private final @Nullable String image;

      public Album(int id, @Nonnull String name, @Nullable String image) {
        this.id = id;
        this.name = name;
        this.image = image;
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

      @Override
      public String toString() {
        return "Album{"
          + "id=" + id + ", "
          + "name=" + name + ", "
          + "image=" + image
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Album) {
          Album that = (Album) o;
          return this.id == that.id
           && ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
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
        h ^= (name == null) ? 0 : name.hashCode();
        h *= 1000003;
        h ^= (image == null) ? 0 : image.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Album> {
        final Field[] fields = {
          Field.forInt("id", "id", null, false),
          Field.forString("name", "name", null, false),
          Field.forString("image", "image", null, true)
        };

        @Override
        public Album map(ResponseReader reader) throws IOException {
          final int id = reader.read(fields[0]);
          final String name = reader.read(fields[1]);
          final String image = reader.read(fields[2]);
          return new Album(id, name, image);
        }
      }
    }

    public static class Track {
      private final int id;

      private final @Nonnull String name;

      private final @Nullable List<Artist> artists;

      private final @Nullable List<Album> albums;

      private final @Nullable String spotify_album_img;

      private final @Nullable String spotify_track_preview_url;

      private final @Nullable Integer duration;

      private final @Nullable String spotify_track_id;

      public Track(int id, @Nonnull String name, @Nullable List<Artist> artists,
          @Nullable List<Album> albums, @Nullable String spotify_album_img,
          @Nullable String spotify_track_preview_url, @Nullable Integer duration,
          @Nullable String spotify_track_id) {
        this.id = id;
        this.name = name;
        this.artists = artists;
        this.albums = albums;
        this.spotify_album_img = spotify_album_img;
        this.spotify_track_preview_url = spotify_track_preview_url;
        this.duration = duration;
        this.spotify_track_id = spotify_track_id;
      }

      public int id() {
        return this.id;
      }

      public @Nonnull String name() {
        return this.name;
      }

      public @Nullable List<Artist> artists() {
        return this.artists;
      }

      public @Nullable List<Album> albums() {
        return this.albums;
      }

      public @Nullable String spotify_album_img() {
        return this.spotify_album_img;
      }

      public @Nullable String spotify_track_preview_url() {
        return this.spotify_track_preview_url;
      }

      public @Nullable Integer duration() {
        return this.duration;
      }

      public @Nullable String spotify_track_id() {
        return this.spotify_track_id;
      }

      @Override
      public String toString() {
        return "Track{"
          + "id=" + id + ", "
          + "name=" + name + ", "
          + "artists=" + artists + ", "
          + "albums=" + albums + ", "
          + "spotify_album_img=" + spotify_album_img + ", "
          + "spotify_track_preview_url=" + spotify_track_preview_url + ", "
          + "duration=" + duration + ", "
          + "spotify_track_id=" + spotify_track_id
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
           && ((this.artists == null) ? (that.artists == null) : this.artists.equals(that.artists))
           && ((this.albums == null) ? (that.albums == null) : this.albums.equals(that.albums))
           && ((this.spotify_album_img == null) ? (that.spotify_album_img == null) : this.spotify_album_img.equals(that.spotify_album_img))
           && ((this.spotify_track_preview_url == null) ? (that.spotify_track_preview_url == null) : this.spotify_track_preview_url.equals(that.spotify_track_preview_url))
           && ((this.duration == null) ? (that.duration == null) : this.duration.equals(that.duration))
           && ((this.spotify_track_id == null) ? (that.spotify_track_id == null) : this.spotify_track_id.equals(that.spotify_track_id));
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
        h ^= (artists == null) ? 0 : artists.hashCode();
        h *= 1000003;
        h ^= (albums == null) ? 0 : albums.hashCode();
        h *= 1000003;
        h ^= (spotify_album_img == null) ? 0 : spotify_album_img.hashCode();
        h *= 1000003;
        h ^= (spotify_track_preview_url == null) ? 0 : spotify_track_preview_url.hashCode();
        h *= 1000003;
        h ^= (duration == null) ? 0 : duration.hashCode();
        h *= 1000003;
        h ^= (spotify_track_id == null) ? 0 : spotify_track_id.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Track> {
        final Artist.Mapper artistFieldMapper = new Artist.Mapper();

        final Album.Mapper albumFieldMapper = new Album.Mapper();

        final Field[] fields = {
          Field.forInt("id", "id", null, false),
          Field.forString("name", "name", null, false),
          Field.forList("artists", "artists", null, true, new Field.ObjectReader<Artist>() {
            @Override public Artist read(final ResponseReader reader) throws IOException {
              return artistFieldMapper.map(reader);
            }
          }),
          Field.forList("albums", "albums", null, true, new Field.ObjectReader<Album>() {
            @Override public Album read(final ResponseReader reader) throws IOException {
              return albumFieldMapper.map(reader);
            }
          }),
          Field.forString("spotify_album_img", "spotify_album_img", null, true),
          Field.forString("spotify_track_preview_url", "spotify_track_preview_url", null, true),
          Field.forInt("duration", "duration", null, true),
          Field.forString("spotify_track_id", "spotify_track_id", null, true)
        };

        @Override
        public Track map(ResponseReader reader) throws IOException {
          final int id = reader.read(fields[0]);
          final String name = reader.read(fields[1]);
          final List<Artist> artists = reader.read(fields[2]);
          final List<Album> albums = reader.read(fields[3]);
          final String spotify_album_img = reader.read(fields[4]);
          final String spotify_track_preview_url = reader.read(fields[5]);
          final Integer duration = reader.read(fields[6]);
          final String spotify_track_id = reader.read(fields[7]);
          return new Track(id, name, artists, albums, spotify_album_img, spotify_track_preview_url, duration, spotify_track_id);
        }
      }
    }
  }
}
