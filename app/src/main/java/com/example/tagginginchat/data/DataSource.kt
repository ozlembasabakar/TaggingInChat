package com.example.tagginginchat.data

import com.example.tagginginchat.R
import com.example.tagginginchat.data.model.Message
import com.example.tagginginchat.data.model.User
import com.example.tagginginchat.ui.theme.Gary
import com.example.tagginginchat.ui.theme.KarenPlankton
import com.example.tagginginchat.ui.theme.MrsKrabs
import com.example.tagginginchat.ui.theme.MrsPuff
import com.example.tagginginchat.ui.theme.Patrick
import com.example.tagginginchat.ui.theme.PearlKrabs
import com.example.tagginginchat.ui.theme.Plankton
import com.example.tagginginchat.ui.theme.Sandy
import com.example.tagginginchat.ui.theme.SpongeBob
import com.example.tagginginchat.ui.theme.Squidward
import javax.inject.Inject

class DataSource @Inject constructor() {

    val messages = mutableListOf(
        Message(
            isSent = true,
            userId = 1,
            content = "I'm Ready, I'm Ready, I'm Ready!"
        ), Message(
            isSent = false,
            userId = 2,
            content = "I Wumbo, You Wumbo, He, She, We Wumbo. Wumboing, Wumbology, the Study of Wumbo!"
        ), Message(
            isSent = false,
            userId = 3,
            content = "The Krusty Krab Pizza is the Pizza for You and Me!"
        )
    )

    val users = mutableListOf<User>(
        User(
            id = 1,
            name = "SpongeBob",
            surname = "SquarePants",
            profileImage = R.drawable.spongebob,
            color = SpongeBob
        ),
        User(
            id = 2,
            name = "Patrick",
            surname = "Star",
            profileImage = R.drawable.patrick,
            color = Patrick
        ),
        User(
            id = 3,
            name = "Squidward",
            surname = "Tentacles",
            profileImage = R.drawable.squidward,
            color = Squidward
        ),
        User(
            id = 4,
            name = "Eugene H.",
            surname = "Krabs",
            profileImage = R.drawable.mr_krabs,
            color = MrsKrabs
        ),
        User(
            id = 5,
            name = "Sheldon J.",
            surname = "Plankton",
            profileImage = R.drawable.plankton,
            color = Plankton
        ),
        User(
            id = 6,
            name = "Karen",
            surname = "Plankton",
            profileImage = R.drawable.karen_plankton,
            color = KarenPlankton
        ),
        User(
            id = 7,
            name = "Sandy",
            surname = "Cheeks",
            profileImage = R.drawable.sandy,
            color = Sandy
        ),
        User(
            id = 8,
            name = "Mrs. Penelope",
            surname = "Puff",
            profileImage = R.drawable.mrs_puff,
            color = MrsPuff
        ),
        User(
            id = 9,
            name = "Pearl",
            surname = "Krabs",
            profileImage = R.drawable.pearl_krabs,
            color = PearlKrabs
        ),
        User(
            id = 10,
            name = "Gary",
            surname = "Wilson",
            profileImage = R.drawable.gary,
            color = Gary
        ),
    )
}