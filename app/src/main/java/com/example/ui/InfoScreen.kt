package com.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.GlassmorphicCard
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.TranslucentSurface
import com.example.ui.theme.WhiteTranslucent

@Composable
fun InfoScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // ── 1. HERO BANNER CARD ──────────────────────────────────────────────
        item {
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                borderColor = ElectricCyan.copy(alpha = 0.35f),
                backgroundColor = DeepNavy.copy(alpha = 0.0f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF0D1B2A),
                                    Color(0xFF0A2A3D),
                                    Color(0xFF0D3350)
                                )
                            ),
                            RoundedCornerShape(24.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Logo with glowing ring
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            ElectricCyan.copy(alpha = 0.25f),
                                            Color.Transparent
                                        )
                                    ),
                                    CircleShape
                                )
                                .border(1.5.dp, ElectricCyan.copy(alpha = 0.5f), CircleShape)
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "HEXplore Logo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "HIMALAYA EXHIBITION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ElectricCyan,
                            letterSpacing = 3.sp
                        )

                        Text(
                            text = "HEX 2083",
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 2.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Motto badge
                        Box(
                            modifier = Modifier
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            ElectricCyan.copy(alpha = 0.15f),
                                            NeonBlue.copy(alpha = 0.2f)
                                        )
                                    ),
                                    RoundedCornerShape(12.dp)
                                )
                                .border(
                                    1.dp,
                                    ElectricCyan.copy(alpha = 0.3f),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "BUILD SAFE.  BUILD SMART.",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = ElectricCyan,
                                letterSpacing = 1.sp,
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Himalaya College of Engineering",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = WhiteTranslucent,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // ── 2. EVENT DETAILS CARD ────────────────────────────────────────────
        item {
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                borderColor = Color.White.copy(alpha = 0.08f),
                backgroundColor = TranslucentSurface.copy(alpha = 0.38f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    SectionLabel(text = "EVENT DETAILS")
                    Spacer(modifier = Modifier.height(14.dp))

                    EventDetailRow(
                        icon = Icons.Default.CalendarMonth,
                        iconTint = ElectricCyan,
                        label = "Date",
                        value = "26th & 27th Ashar, 2083",
                        subValue = "Friday & Saturday"
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = Color.White.copy(alpha = 0.05f)
                    )

                    EventDetailRow(
                        icon = Icons.Default.LocationOn,
                        iconTint = Color(0xFFFF6B6B),
                        label = "Venue",
                        value = "Himalaya College of Engineering",
                        subValue = "Chyasal-9, Lalitpur, Nepal"
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = Color.White.copy(alpha = 0.05f)
                    )

                    // Admission badge row
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    Color(0xFF2DCE89).copy(alpha = 0.15f),
                                    RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = Color(0xFF2DCE89),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Admission",
                                fontSize = 11.sp,
                                color = WhiteTranslucent,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "FREE ENTRY FOR +2 GRADUATES",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF2DCE89)
                            )
                        }
                    }
                }
            }
        }

        // ── 3. CORE PILLARS CARD ─────────────────────────────────────────────
        item {
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                borderColor = Color.White.copy(alpha = 0.08f),
                backgroundColor = TranslucentSurface.copy(alpha = 0.38f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    SectionLabel(text = "EXHIBITION PILLARS")
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PillarChip(
                            icon = Icons.Default.PrecisionManufacturing,
                            label = "Engineering\nProjects",
                            accentColor = ElectricCyan,
                            modifier = Modifier.weight(1f)
                        )
                        PillarChip(
                            icon = Icons.Default.School,
                            label = "Student\nInnovations",
                            accentColor = Color(0xFFFFD700),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PillarChip(
                            icon = Icons.Default.SmartToy,
                            label = "AI &\nRobotics",
                            accentColor = Color(0xFFB06EF5),
                            modifier = Modifier.weight(1f)
                        )
                        PillarChip(
                            icon = Icons.Default.RocketLaunch,
                            label = "Future\nTechnologies",
                            accentColor = Color(0xFF2DCE89),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // ── 4. DEVELOPER ATTRIBUTION CARD ───────────────────────────────────
        item {
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                borderColor = ElectricCyan.copy(alpha = 0.2f),
                backgroundColor = DeepNavy.copy(alpha = 0.7f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF0D1B2A),
                                    Color(0xFF091525)
                                )
                            ),
                            RoundedCornerShape(24.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SectionLabel(text = "DEVELOPED BY")
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            DeveloperChip(name = "Kushal Neupane", modifier = Modifier.weight(1f))
                            DeveloperChip(name = "Bardan Bhatta", modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        HorizontalDivider(color = ElectricCyan.copy(alpha = 0.1f))
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Code,
                                contentDescription = null,
                                tint = ElectricCyan.copy(alpha = 0.6f),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Computer / IT Department, HCOE",
                                fontSize = 12.sp,
                                color = WhiteTranslucent,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(48.dp)) }
    }
}

// ── HELPER COMPOSABLES ────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(14.dp)
                .background(ElectricCyan, RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = ElectricCyan,
            letterSpacing = 1.5.sp
        )
    }
}

@Composable
private fun EventDetailRow(
    icon: ImageVector,
    iconTint: Color,
    label: String,
    value: String,
    subValue: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconTint.copy(alpha = 0.12f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = WhiteTranslucent,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = subValue,
                fontSize = 12.sp,
                color = WhiteTranslucent
            )
        }
    }
}

@Composable
private fun PillarChip(
    icon: ImageVector,
    label: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(accentColor.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
            .border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
            .padding(vertical = 14.dp, horizontal = 12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun DeveloperChip(name: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        ElectricCyan.copy(alpha = 0.1f),
                        NeonBlue.copy(alpha = 0.08f)
                    )
                ),
                RoundedCornerShape(16.dp)
            )
            .border(1.dp, ElectricCyan.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(ElectricCyan.copy(alpha = 0.15f), CircleShape)
                    .border(1.dp, ElectricCyan.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = ElectricCyan,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}
