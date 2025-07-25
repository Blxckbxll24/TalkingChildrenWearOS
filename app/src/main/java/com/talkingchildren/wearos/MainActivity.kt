package com.talkingchildren.wearos

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.talkingchildren.wearos.adapters.CategoryAdapter
import com.talkingchildren.wearos.models.Category

/**
 * Main activity displaying the three main categories
 */
class MainActivity : ComponentActivity() {

    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView)
        
        val categories = listOf(
            Category(
                id = Category.BASIC,
                name = getString(R.string.category_basic),
                iconResource = R.drawable.ic_category_basic
            ),
            Category(
                id = Category.EMOTIONS,
                name = getString(R.string.category_emotions),
                iconResource = R.drawable.ic_category_emotions
            ),
            Category(
                id = Category.NEEDS,
                name = getString(R.string.category_needs),
                iconResource = R.drawable.ic_category_needs
            )
        )

        categoryAdapter = CategoryAdapter(categories) { category ->
            openMessagesActivity(category)
        }

        categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = categoryAdapter
        }
    }

    private fun openMessagesActivity(category: Category) {
        val intent = Intent(this, MessagesActivity::class.java).apply {
            putExtra(MessagesActivity.EXTRA_CATEGORY_ID, category.id)
            putExtra(MessagesActivity.EXTRA_CATEGORY_NAME, category.name)
        }
        startActivity(intent)
    }
}