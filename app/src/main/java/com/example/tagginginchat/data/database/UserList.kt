package com.example.tagginginchat.data.database

import com.example.tagginginchat.R
import com.example.tagginginchat.data.model.User

val users = mutableListOf<User>(
    User(
        id = 1,
        name = "SpongeBob",
        surname = "SquarePants",
        profileImage = R.drawable.spongebob
    ),User(
        id = 2,
        name = "Patrick",
        surname = "Star",
        profileImage = R.drawable.patrick
    ),User(
        id = 3,
        name = "Squidward",
        surname = "Tentacles",
        profileImage = R.drawable.squidward
    ),User(
        id = 4,
        name = "Eugene H.",
        surname = "Krabs",
        profileImage = R.drawable.mr_krabs
    ),User(
        id = 5,
        name = "Sheldon J.",
        surname = "Plankton",
        profileImage = R.drawable.plankton
    ),User(
        id = 6,
        name = "Karen",
        surname = "Plankton",
        profileImage = R.drawable.karen_plankton
    ),User(
        id = 7,
        name = "Sandy",
        surname = "Cheeks",
        profileImage = R.drawable.sandy
    ),User(
        id = 8,
        name = "Mrs. Penelope",
        surname = "Puff",
        profileImage = R.drawable.mrs_puff
    ),User(
        id = 9,
        name = "Pearl",
        surname = "Krabs",
        profileImage = R.drawable.pearl_krabs
    ),User(
        id = 10,
        name = "Gary",
        surname = "Wilson",
        profileImage = R.drawable.gary
    ),
)