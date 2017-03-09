# BulbulProject


USER-[:FOLLOWS]->ALBUM
USER-[:FOLLOWS]->SONG
USER-[:FOLLOWS]->ARTIST
USER-[:FOLLOWS]->USER

USER-[:FOLLOWS]->PLAYLIST // discard
USER-[:LISTENED_LAST]->PLAYLIST // discard
USER-[:LISTENED_LAST]->SONG // discard
PLAYLIST-[:HAS]->SONG // discard
PLAYLIST-[:BY]->USER // discard

ALBUM-[:HAS]->SONG // DONE
ALBUM-[:BY]->ARTIST // DONE
SONG-[:BY]->ARTIST



GRAPHQL CONTRACT

BulbulUser {
    id
    name
    lastActiveList: Playlist
}

Playlist {
    id
    name
}

Song {
    id
    name
    year
    duration
    genre
}

Artist {
    id
    name
}

Album {
    id
    coverPhotoUrl //NEW
    name
    year
}
