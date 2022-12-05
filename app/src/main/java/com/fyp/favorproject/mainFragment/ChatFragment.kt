package com.fyp.favorproject.mainFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fyp.favorproject.adapter.UsersAdapter
import com.fyp.favorproject.databinding.FragmentChatBinding
import com.fyp.favorproject.model.Chats
import com.fyp.favorproject.model.Users
import com.fyp.favorproject.utill.ChatsCallBack
import com.fyp.favorproject.utill.UserHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var manager: LinearLayoutManager
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var usersAdapter: UsersAdapter
    private lateinit var usersArrayList:ArrayList<Users>
    var user: Users? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.d("CCAADD", "onChatsFetched: created ")
        Toast.makeText(requireContext(), "Created", Toast.LENGTH_SHORT).show()

        binding = FragmentChatBinding.inflate(inflater)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        usersArrayList = ArrayList<Users>()

        binding.progressBarChatFragment.visibility=View.VISIBLE
        Log.d("CCAADD", "onChatsFetched: call back called ")
        UserHelper.getCurrentUserChats(object : ChatsCallBack{

            override fun onChatsFetched(chats: ArrayList<Chats>) {

                Log.d("CC", "onChatsFetched: call back called ")
                if (chats.size<=0){
                    binding.progressBarChatFragment.visibility=View.GONE
                    Log.d("CC", "onChatsFetched: No chat found ")

                }else{

                    Log.d("CC", "onChatsFetched: Chats found ")

                    usersAdapter = UsersAdapter(context?.applicationContext as Context, chats,this@ChatFragment)

                    binding.rvUserslistChat.adapter = usersAdapter
                    binding.progressBarChatFragment.visibility=View.GONE

                }
            }

        })

     //   usersAdapter = UsersAdapter(context?.applicationContext as Context, usersArrayList)
//
//
//        database.reference.child("User").child(auth.uid.toString()).addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                user = snapshot.getValue(User::class.java)
//            }
//
//            override fun onCancelled(error: DatabaseError) {}
//        })

//        database.reference.child("User").addValueEventListener(object: ValueEventListener{
//           // @SuppressLint("NotifyDataSetChanged")
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//               usersArrayList.clear()
//                for (snapshot1 in snapshot.children){
//                    val user:User? = snapshot1.getValue(User::class.java)
//                    if (!user!!.uid.equals(FirebaseAuth.getInstance().uid)) usersArrayList.add(user)
//                }
//                usersAdapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {}
//        })

        return binding.root
    }

}
