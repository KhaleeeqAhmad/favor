package com.fyp.favorproject.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.fyp.favorproject.R
import com.fyp.favorproject.fragments.NotificationFragment
import com.fyp.favorproject.model.Notification
import com.fyp.favorproject.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso

class NotificationAdapter(
    val context: NotificationFragment,
    private val notificationList: ArrayList<Notification>
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.item_dashboard_notification,parent,false)
        return NotificationViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val currentNotification = notificationList[position]

        FirebaseDatabase.getInstance().reference
            .child("User")
            .child(currentNotification.notificationBy!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue<User>()
                    Picasso.get()
                        .load(user?.userProfilePhoto)
                        .placeholder(R.drawable.place_holder_image)
                        .into(holder.profileImage)

                    if (currentNotification.notificationType.equals("like")) {
                        @Suppress("DEPRECATION")
                        holder.notificationDescription.text = Html.fromHtml("<b>"+user?.name+"</b>"+" liked your FAVOR")
                    } else if (currentNotification.notificationType.equals("favor")) {
                        @Suppress("DEPRECATION")
                        holder.notificationDescription.text = Html.fromHtml("<b>"+user?.name+"</b>"+" add new FAVOR")
                    }
                }

                override fun onCancelled(error: DatabaseError) =Unit
            })
        holder.openNotification.setOnClickListener {
            val activity=it.context as AppCompatActivity
            Navigation.findNavController(activity.findViewById(R.id.fragmentHolder)).navigate(R.id.homeFragment)

        }

    }

    override fun getItemCount() = notificationList.size


    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.ivUserProfileNotification)
        val notificationDescription: TextView = itemView.findViewById(R.id.tvNotificationDescription)
        val notificationTime: TextView = itemView.findViewById(R.id.tvNotificationTime)
        val openNotification : ConstraintLayout = itemView.findViewById(R.id.openNotification)
    }

}