package towercone.conflushechka

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_pages.view.*

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [SectionsActivity]
 * in two-pane mode (on tablets) or a [PagesActivity]
 * on handsets.
 */
class PagesFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_SECTION_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.

                // item = DummyContent.ITEM_MAP[it.getString(ARG_SECTION_ID)]
                // activity?.toolbar_layout?.title = item?.content
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_pages, container, false)

        rootView.apply {
            view_pager.adapter = object: FragmentPagerAdapter(childFragmentManager) {
                override fun getItem(position: Int) = PageFragment()
                override fun getCount() = 9
                override fun getPageTitle(position: Int) = "Page $position"
            }
            tab_layout.setupWithViewPager(view_pager)

            // Show the dummy content as text in a TextView.
            //item?.let {
                //item_detail.text = it.details
            //}
        }

        return rootView
    }

    companion object {
        const val ARG_SECTION_ID = "page_id"

        fun newInstance(sectionId: Long) = PagesFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_SECTION_ID, sectionId)
            }
        }
    }
}
