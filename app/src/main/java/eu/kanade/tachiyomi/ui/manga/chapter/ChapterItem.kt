package eu.kanade.tachiyomi.ui.manga.chapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.data.database.models.Chapter
import eu.kanade.tachiyomi.data.database.models.Manga
import eu.kanade.tachiyomi.data.download.model.Download
import eu.kanade.tachiyomi.source.model.Page

class ChapterItem(val chapter: Chapter, val manga: Manga) :
    AbstractFlexibleItem<ChapterHolder>(),
    Chapter by chapter {

    private var _status: Download.State = Download.State.NOT_DOWNLOADED

    var status: Download.State
        get() = download?.status ?: _status
        set(value) {
            _status = value
        }

    val progress: Int
        get() {
            val pages = download?.pages ?: return 0
            return pages.map(Page::progress).average().toInt()
        }

    @Transient
    var download: Download? = null

    val isDownloaded: Boolean
        get() = status == Download.State.DOWNLOADED

    override fun getLayoutRes(): Int {
        return R.layout.chapters_item
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>): ChapterHolder {
        return ChapterHolder(view, adapter as ChaptersAdapter)
    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>,
        holder: ChapterHolder,
        position: Int,
        payloads: List<Any?>?
    ) {
        holder.bind(this, manga)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is ChapterItem) {
            return chapter.id!! == other.chapter.id!!
        }
        return false
    }

    override fun hashCode(): Int {
        return chapter.id!!.hashCode()
    }
}
