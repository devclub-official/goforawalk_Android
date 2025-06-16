package com.yjdev.goforawalk.presentation.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yjdev.goforawalk.presentation.viewmodel.MainViewModel
import com.yjdev.goforawalk.data.model.Footstep
import com.yjdev.goforawalk.presentation.ui.component.FootstepCard
import com.yjdev.goforawalk.presentation.ui.theme.Goforawalk_AndroidTheme

@Composable
fun HomeScreen(viewModel: MainViewModel, feedList: List<Footstep>) {
    val context = LocalContext.current
    Goforawalk_AndroidTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "홈",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (feedList.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "아직 발자취가 없어요", style = MaterialTheme.typography.titleLarge)
                    Text(text = "첫 발자취를 남겨볼까요?", style = MaterialTheme.typography.titleLarge)
                }
            } else {
                LazyColumn {
                    items(feedList) { feed ->
                        FootstepCard(footstep = feed, onDelete = {
                            viewModel.deleteFootstep(
                                feed.footstepId,
                                onSuccess = {
                                    Toast.makeText(context, "삭제 성공", Toast.LENGTH_SHORT).show()
                                    viewModel.fetchList()
                                },
                                onError = {
                                    Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}