package com.exercisechat.presentation.feature.messages.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.exercisechat.R
import com.exercisechat.data.UserEntity
import com.exercisechat.domain.models.Message
import com.exercisechat.domain.models.MessageSpacing
import com.exercisechat.domain.models.MessageStatus
import com.exercisechat.presentation.feature.messages.MessagesContract
import com.exercisechat.presentation.theme.Colors
import com.exercisechat.presentation.theme.ExerciseChatTheme
import java.time.Instant

private const val SMALL_MESSAGE_SPACING = 4
private const val LARGE_MESSAGE_SPACING = 12

@Composable
fun MessagesScreen(
    navController: NavHostController,
    senderUser: UserEntity?,
    receiverUser: UserEntity?,
    messages: List<Message>,
    onEvent: (action: MessagesContract.Event) -> Unit
) {
    Scaffold(
        topBar = {
            MessagesTopBar(receiverUser, navController, onEvent)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (messages.isEmpty()) {
                EmptyChat(Modifier.weight(1f))
            } else {
                if (senderUser != null) {
                    MessagesColumn(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxSize()
                            .weight(1f),
                        messages = messages,
                        senderUser = senderUser,
                    )
                } else {
                    ChatLoadingProgress(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    )
                }
            }
            // add top shadow to the input field
            Spacer(
                modifier = Modifier
                    .height(4.dp)
                    .fillMaxWidth()
                    .rotate(180f)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                DefaultShadowColor.copy(alpha = 0.1f),
                                Color.Transparent,
                            )
                        )
                    )
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                MessagesInputField(
                    modifier = Modifier.padding(12.dp),
                    onSendMessage = { message ->
                        onEvent(MessagesContract.Event.SendMessage(message))
                    }
                )
            }
        }
    }
}

@Composable
private fun ChatLoadingProgress(modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
        )
    }
}

@Composable
private fun EmptyChat(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier
            .wrapContentSize(),
        color = Colors.DarkGrey,
        text = stringResource(id = R.string.text_empty_chat),
        fontSize = 24.sp
    )
}

@Composable
private fun MessagesColumn(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    senderUser: UserEntity
) {
    LazyColumn(
        modifier = modifier,
        reverseLayout = true
    ) {
        items(messages) { message ->
            val isCurrentUser = message.senderUserId == senderUser.id
            // prevent the bubble to take all width of the screen to make UI more readable
            val bubblePadding = if (isCurrentUser) {
                Modifier.padding(start = 48.dp)
            } else {
                Modifier.padding(end = 48.dp)
            }

            val messageSpacing = if (message.messageSpacing == MessageSpacing.LARGE) {
                LARGE_MESSAGE_SPACING
            } else {
                SMALL_MESSAGE_SPACING
            }.dp
            MessagesBubble(
                modifier = Modifier
                    .padding(top = messageSpacing)
                    .then(bubblePadding),
                text = message.text,
                status = message.status,
                isCurrentUser = isCurrentUser
            )

            // display timestamp if a previous message was sent more than an hour ago, or there is no previous messages
            if (message.needsHeader) {
                MessagesHeader(message.timestamp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MessagesScreenPreview() {
    ExerciseChatTheme {
        MessagesScreen(
            navController = rememberNavController(),
            receiverUser = UserEntity(
                id = 1,
                firstName = "First",
                lastName = "Last",
                avatarId = 2
            ),
            senderUser = UserEntity(
                id = 2,
                firstName = "Second",
                lastName = "Last",
                avatarId = 3
            ),
            messages = listOf(
                Message(
                    text = "Hello World!",
                    senderUserId = 2,
                    receiverUserId = 1,
                    status = MessageStatus.SENT,
                    Instant.ofEpochSecond(123456789)
                )
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MessagesEmptyChatPreview() {
    ExerciseChatTheme {
        MessagesScreen(
            navController = rememberNavController(),
            receiverUser = UserEntity(
                id = 1,
                firstName = "First",
                lastName = "Last",
                avatarId = 2
            ),
            senderUser = UserEntity(
                id = 2,
                firstName = "Second",
                lastName = "Last",
                avatarId = 3
            ),
            messages = emptyList(),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MessagesLoadingChatPreview() {
    ExerciseChatTheme {
        MessagesScreen(
            navController = rememberNavController(),
            receiverUser = null,
            senderUser = null,
            messages = listOf(
                Message(
                    text = "Hello World!",
                    senderUserId = 2,
                    receiverUserId = 1,
                    status = MessageStatus.SENT,
                    Instant.ofEpochSecond(123456789)
                )
            ),
            onEvent = {}
        )
    }
}


