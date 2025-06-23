package com.yjdev.goforawalk.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.yjdev.goforawalk.R
import com.yjdev.goforawalk.data.model.Footstep
import com.yjdev.goforawalk.presentation.ui.theme.Gray757990
import com.yjdev.goforawalk.presentation.ui.theme.GrayD0D0D0
import com.yjdev.goforawalk.presentation.ui.theme.GrayE0E0E0

@Composable
fun FootstepCard(
    footstep: Footstep,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        var expanded by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .background(Color.White)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = footstep.userNickname, fontSize = 16.sp, fontWeight = FontWeight.Bold)

                    Box {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_more_menu),
                            contentDescription = "more menu",
                            modifier = Modifier
                                .padding(12.dp)
                                .clickable { expanded = true }
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "삭제 아이콘"
                                    )
                                },
                                text = { Text("삭제하기") },
                                colors = MenuItemColors(Color.Red, Color.Red, Color.Red, Color.Gray, Color.Gray, Color.Gray),
                                onClick = {
                                    expanded = false
                                    onDelete()
                                },
                                modifier = Modifier.size(127.dp, 36.dp)
                            )
                        }
                    }
                }
                Text(text = footstep.date, fontSize = 12.sp, color = Gray757990)
            }

            Spacer(modifier = Modifier.height(4.dp))

            AsyncImage(
                model = footstep.imageUrl,
                contentDescription = "피드 이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(200.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = footstep.content ?: "",
                color = Gray757990,
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
