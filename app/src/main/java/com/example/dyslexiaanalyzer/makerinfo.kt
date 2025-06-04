package com.example.dyslexiaanalyzer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
//import com.example.teaminfo.ui.theme.TeaminfoTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
///import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
//import androidx.compose.material.icons.filled.MusicNote
//import androidx.compose.material.icons.filled.Pets
//import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
//mport androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter


data class TeamMember(
    val name: String,
    val role: String,
    val funFact: String,
    val imageRes: Int,
    val favoriteThings: List<FavoriteThing>,
    val quirkySaying: String
)

data class FavoriteThing(
    val icon: @Composable () -> Unit,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun makerinfo(navController: NavHostController) {
    // List of fun background colors for each team member card
    val cardColors = listOf(
        Color(0xFFE6F7FF), // Light blue
        Color(0xFFFFF3E0), // Light orange
        Color(0xFFE8F5E9), // Light green
        Color(0xFFF3E5F5), // Light purple
        Color(0xFFFFEBEE)  // Light pink
    )

    // Create team members with fun facts and interests
    val teamMembers = listOf(
        TeamMember(
            name = "Thrilochan Reddy Vemula",
            role = "Lead Developer",
            funFact = "There are no winners in a war and debugging",
            imageRes = R.drawable.thrilochan,
            favoriteThings = listOf(
                FavoriteThing(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                    description = "Machines"
                ),
                FavoriteThing(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                    description = "Anime"
                )
            ),
            quirkySaying = "˥ooℲ ǝɥʇ ǝsıɐɹԀ "
        ),
        TeamMember(
            name = "Chetan Aditya",
            role = "Ml Developer",
            funFact = "Breaking things just to fix them is my kind of fun",
            imageRes = R.drawable.ca,
            favoriteThings = listOf(
                FavoriteThing(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                    description = "Football"
                ),
                FavoriteThing(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                    description = "Editing"
                ),
                FavoriteThing(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                    description = "Writing"
                )
            ),
            quirkySaying = "The best way to predict the future is to create it"
        ),
        TeamMember(
            name = "Bharath Chandra",
            role = "",
            funFact = "I speak fluent code-- But my Bugs speak louder",
            imageRes = R.drawable.bharath,
            favoriteThings = listOf(
                FavoriteThing(
                    icon = { Icon(Icons.Default.Star, contentDescription = null) },
                    description = "Technology"
                ),
                FavoriteThing(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                    description = "Music"
                )
            ),
            quirkySaying = "It's not that we use technology, we live technology"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Our Development Team",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate("home") },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Navigate back to home",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }/*,
                actions = {
                    // Optional: Add a share button to allow sharing info about the team
                    IconButton(onClick = { /* Share functionality */ }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share team information",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }*/,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1D6D86),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                modifier = Modifier.shadow(elevation = 4.dp)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Fun intro text

                Box(modifier = Modifier.fillMaxSize()) {

                    // Background Image from URL
                    Image(
                        painter = rememberAsyncImagePainter("https://e0.pxfuel.com/wallpapers/98/684/desktop-wallpaper-blue-green-soft-gradation-blur.jpg"),
                        contentDescription = "Background Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "Meet the creative minds who spend way too much time staring at screens so you don't have to!",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 24.dp)
                    )
                // Team members cards
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(teamMembers) { member ->
                        FunTeamMemberCard(
                            teamMember = member,
                            backgroundColor = cardColors[teamMembers.indexOf(member) % cardColors.size]
                        )
                    }
                }
            }
            }
        }
    }
}

@Composable
fun FunTeamMemberCard(teamMember: TeamMember, backgroundColor: Color) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile image with fun border
            Box(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                    .padding(4.dp)
            ) {
                Image(
                    painter = painterResource(id = teamMember.imageRes),
                    contentDescription = "${teamMember.name} profile",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            // Name with fun styling
            Text(
                text = teamMember.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            // Role
            Text(
                text = teamMember.role,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )

            // Quirky saying in a speech bubble
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = "\"${teamMember.quirkySaying}\"",
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(12.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            // Expandable content
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    // Fun fact section
                    Text(
                        text = "Fun Fact:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = teamMember.funFact,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Favorite things
                    Text(
                        text = "Favorite Things:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    teamMember.favoriteThings.forEach { thing ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                thing.icon()
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = thing.description,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Tap to expand/collapse hint
            Text(
                text = if (expanded) "Tap to collapse" else "More Info",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }

}