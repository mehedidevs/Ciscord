package com.ciscord.talk.chat.hangout

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
import com.ciscord.talk.chat.hangout.databinding.FragmentProfileEditBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class ProfileEditFragment : Fragment() {


    lateinit var binding: FragmentProfileEditBinding

    lateinit var userDb: DatabaseReference
    lateinit var userStorage: StorageReference


    private var userId = ""

    private var isProfileClicked = false

    private lateinit var userprofileUri: Uri

    private var imageLink: String = "no_data"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileEditBinding.inflate(layoutInflater)


        userDb = FirebaseDatabase.getInstance().reference
        userStorage = FirebaseStorage.getInstance().reference




        requireArguments().getString(USERID)?.let {
            userId = it
            getUserByID(it)
        }



        binding.saveBtn.setOnClickListener {

            if (isProfileClicked && userprofileUri != null) {

                uploadImage(userprofileUri)


            } else {


                val userMap: MutableMap<String, Any> = mutableMapOf()
                userMap["fullName"] = binding.fullName.text.toString().trim()
                userMap["bio"] = binding.bio.text.toString().trim()


                userDb.child(DBNODES.USER).child(userId).updateChildren(userMap)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Profile Updated ! ",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "${it.exception?.message}",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
            }


        }


        binding.profileImage.setOnClickListener {
            isProfileClicked = true

            pickProfileImage()


        }

        return binding.root
    }

    private fun uploadImage(userprofileUri: Uri) {


        val profileStorage: StorageReference =
            userStorage.child("upload").child(userId).child("profile-image")


        profileStorage.putFile(userprofileUri).addOnCompleteListener {

            if (it.isSuccessful) {

                profileStorage.downloadUrl.addOnSuccessListener { data ->


                    profileUpdateWithImage(data.toString())


                    Toast.makeText(requireContext(), "Image Uploaded", Toast.LENGTH_SHORT).show()


                }

            }


        }


    }

    private fun profileUpdateWithImage(imageLink: String) {


        val userMap: MutableMap<String, Any> = mutableMapOf()
        userMap["fullName"] = binding.fullName.text.toString().trim()
        userMap["profileImage"] = imageLink
        userMap["bio"] = binding.bio.text.toString().trim()


        userDb.child(DBNODES.USER).child(userId).updateChildren(userMap).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "Profile Updated ! ", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "${it.exception?.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }

    private fun pickProfileImage() {

        ImagePicker.with(this)
            .crop()                    //Crop image(Optional), Check Customization for more option
            .compress(512)      //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                512,
                512
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }


    }


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    data?.data?.let {
                        userprofileUri = it

                        binding.profileImage.setImageURI(it)

                    }


                }

                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }


    companion object {
        const val USERID = "user_id_key"
    }

    private fun getUserByID(userId: String) {


        userDb.child(DBNODES.USER).child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                snapshot.getValue(User::class.java)?.let {

                    binding.userEmail.text = it.email
                    binding.bio.setText(it.bio)
                    binding.fullName.setText(it.fullName)
                    binding.profileImage.load(it.profileImage)


                }


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }


}