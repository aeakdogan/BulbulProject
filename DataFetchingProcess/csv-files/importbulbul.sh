export JAVA_OPTS="-Xmx4G"

./neo4j-import --into bulbul.db \
                --id-type string \
               --multiline-fields=true \
                --bad-tolerance=1000000 \
                --skip-duplicate-nodes=true \
                 --skip-bad-relationships=true \
                --ignore-empty-strings =true \
         --nodes:Tag generated_csv/tags_merged.csv \
         --nodes:Artist generated_csv/artists.csv \
         --nodes:Album generated_csv/albums.csv \
         --nodes:Track generated_csv/tracks.csv \
         --nodes:BulbulUser generated_csv/user-info.csv \
         --relationships:BY generated_csv/album-artist.csv \
         --relationships:BY generated_csv/track-artist.csv \
         --relationships:HAS generated_csv/album-track.csv \
         --relationships:LISTENS generated_csv/user-to-album.csv \
         --relationships:LISTENS generated_csv/user-to-artist.csv \
         --relationships:LISTENS generated_csv/user-to-track.csv \
         --relationships:FOLLOWS generated_csv/user-to-user.csv \
         --relationships:TAGGED generated_csv/artist-tags.csv \
         --relationships:TAGGED generated_csv/track-tags.csv

