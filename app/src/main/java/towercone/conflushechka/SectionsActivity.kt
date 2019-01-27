package towercone.conflushechka

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_sections.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [PagesActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class SectionsActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sections)

        getSectionIdFromDeepLink(intent)?.let {
            showSection(it)
        }

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        myRetrofitService.listSections().call(this) {
            item_list.adapter = SimpleItemRecyclerViewAdapter(this, it, twoPane)
        }
    }

    fun showSection(sectionId: String) {
        if (twoPane) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.item_detail_container, PagesFragment.newInstance(sectionId))
                .commit()
        } else {
            startActivity(PagesActivity.makeIntent(this, sectionId))
        }
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: SectionsActivity,
        private val values: List<Section>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Section
                parentActivity.showSection(item.id)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.contentView.text = item.title

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val contentView: TextView = view.content
        }
    }
}

private fun getSectionIdFromDeepLink(intent: Intent): String? {
    val data = intent.data
    if (data != null && data.isHierarchical) {
        val uri = intent.dataString ?: return null
        val parts = uri.split('/').filter { it.isNotEmpty() }
        if (parts.size == 4 && parts[2] == "section") {
            return parts[3]
        }
    }
    return null
}
