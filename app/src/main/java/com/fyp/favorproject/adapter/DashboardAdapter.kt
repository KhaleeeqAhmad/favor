package com.fyp.favorproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fyp.favorproject.R
import com.fyp.favorproject.mainFragment.HomeFragment
import com.fyp.favorproject.model.Posts


/**
 * Adapter for the [RecyclerView] in Home Fragment. Displays [Posts] data object.
 */
class DashboardAdapter(
    val context: HomeFragment, private val dataSet: List<Posts>
) : RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder>() {


    class DashboardViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val profileImage: ImageView = view.findViewById(R.id.ivUserProfile)
        val userName: TextView = view.findViewById(R.id.tvUserName)
        val userLevel: TextView = view.findViewById(R.id.tvUserLevel)
        val postDate: TextView =view.findViewById(R.id.tvPostTime)
        val postDescription: TextView = view.findViewById(R.id.tvPostDescription)
        val postImage : ImageView = view.findViewById(R.id.ivPostImage)

        val btnRespond:TextView = view.findViewById(R.id.btnRespond)



    init {
        btnRespond.setOnClickListener{

        }
    }

    }


    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.item_dashboard, parent, false)
        return DashboardViewHolder(adapterLayout)
    }


    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val item = dataSet[position]
        holder.profileImage.setImageResource(item.profileImageResourceID)
        holder.userName.text = context.resources.getString(item.userNameResourceID)
        holder.userLevel.text = context.resources.getString(item.userLevelResourceID)
        holder.postDate.text = context.resources.getString(item.postDateResourceID)
        holder.postDescription.text = context.resources.getString(item.postDescriptionResourceID)
        holder.postImage.setImageResource(item.postImageResourceID)
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = dataSet.size

}