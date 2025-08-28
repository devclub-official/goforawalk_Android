package com.yjdev.goforawalk.presentation.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yjdev.goforawalk.R
import com.yjdev.goforawalk.presentation.state.FootstepUiState
import com.yjdev.goforawalk.presentation.ui.component.FootstepCard
import com.yjdev.goforawalk.presentation.ui.theme.Goforawalk_AndroidTheme
import com.yjdev.goforawalk.presentation.ui.theme.Gray64748B
import com.yjdev.goforawalk.presentation.ui.theme.MainColor
import com.yjdev.goforawalk.presentation.viewmodel.MainViewModel

@Composable
fun HomeScreen(viewModel: MainViewModel, onNavigateToCertify: () -> Unit) {
    val context = LocalContext.current
    val uiState by viewModel.uiState

    Goforawalk_AndroidTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "홈", fontSize = 22.sp, fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (val state = uiState) {
                is FootstepUiState.Loading -> {}

                is FootstepUiState.Success -> {
                    if (state.feedList.isEmpty()) {
                        EmptyFeedView(onNavigateToCertify)
                    } else {
                        LazyColumn {
                            items(state.feedList) { feed ->
                                FootstepCard(footstep = feed, onDelete = {
                                    viewModel.deleteFootstep(feed.footstepId, onSuccess = {
                                        Toast.makeText(context, "삭제 성공", Toast.LENGTH_SHORT).show()
                                        viewModel.fetchList()
                                    }, onError = {
                                        Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show()
                                    })
                                })
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }

                is FootstepUiState.Failure -> {}
            }

        }
    }
}

@Composable
fun EmptyFeedView(onNavigateToCertify: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))

        Icon(painterResource(id = R.drawable.ic_check_circle),
            contentDescription = "check circle",
            tint = Color.Unspecified,
            modifier = Modifier.graphicsLayer {
                shadowElevation = 12.dp.toPx()
                shape = CircleShape
                clip = false
            })

        Spacer(modifier = Modifier.height(50.dp))

        Text(text = "아직 발자취가 없어요.", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "첫 발자취를 남겨보세요!", fontSize = 16.sp, color = Gray64748B, fontWeight = FontWeight.Medium)
        Text(text = "당신만의 특별한 순간을 공유해주세요.", fontSize = 16.sp, color = Gray64748B, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(80.dp))

        Button(
            onClick = { onNavigateToCertify() },
            modifier = Modifier
                .size(width = 190.dp, height = 55.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(MainColor),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "첫 게시물 작성하기", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
