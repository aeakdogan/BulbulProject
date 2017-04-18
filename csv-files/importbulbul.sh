export JAVA_OPTS="-Xmx4G"

./neo4j-import --into bulbul.db \
                --id-type string \
               --multiline-fields=true \
                --bad-tolerance=50000 \
                --skip-duplicate-nodes=true \
                 --skip-bad-relationships=true \
                --ignore-empty-strings =true \
         --nodes:Album ~/Bulbul/Data/Data/albums.csv \
         --nodes:Artist ~/Bulbul/Data/Data/artists.csv \
         --nodes:Tag ~/Bulbul/Data/Data/tags.csv \
         --nodes:Track ~/Bulbul/Data/Data/tracks.csv \
         --nodes:BulbulUser ~/Bulbul/Data/Data/user-info-withpw.csv \
         --relationships:BY ~/Bulbul/Data/Data/album-artist.csv \
         --relationships:BY ~/Bulbul/Data/Data/track-artist.csv \
         --relationships:HAS ~/Bulbul/Data/Data/album-track.csv \
         --relationships:FOLLOWS ~/Bulbul/Data/Data/user-to-album.csv \
         --relationships:FOLLOWS ~/Bulbul/Data/Data/user-to-artist.csv \
         --relationships:FOLLOWS ~/Bulbul/Data/Data/user-to-track.csv \
         --relationships:FOLLOWS ~/Bulbul/Data/Data/user-to-user.csv
