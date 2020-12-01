package edu.uoc.pac3.data.streams


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.uoc.pac3.R

class StreamsListAdapter(private var streams: MutableList<Stream>) :RecyclerView.Adapter<StreamsListAdapter.ViewHolder>() {

    // Creates View Holder for re-use
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamsListAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_list_stream, parent, false)
        return ViewHolder(view)
    }

    // Binds re-usable View for a given position
    override fun onBindViewHolder(holder: StreamsListAdapter.ViewHolder, position: Int) {
        holder.vhTitle.text = streams[position].title
        holder.vhUserName.text = streams[position].userName


        // Load imageThumbnail
        val thumbnailUrl = streams[position].thumbnail_url
            ?.replace("{width}", "1024")
            ?.replace("{height}", "840")

        Glide.with(holder.vhThumbnailUrl.context)
            .load(thumbnailUrl)
            .into(holder.vhThumbnailUrl)

    }



    // Return number of Streams
    override fun getItemCount(): Int {
        return streams.size
    }

    // Holds an instance to the view for re-use
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val vhUserName = view.findViewById<TextView>(R.id.st_username)
        val vhTitle = view.findViewById<TextView>(R.id.st_title)
        val vhThumbnailUrl = view.findViewById<ImageView>(R.id.st_thumbnail_url)
    }

}