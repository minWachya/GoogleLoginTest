package com.example.googlelogingtest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.googlelogingtest.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

private const val RC_RIGN_IN = 300

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    companion object {
        private val TAG = "mmm"
    }

    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleAuth: FirebaseAuth
    private lateinit var GoogleSingResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //구글
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //.requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        googleAuth = FirebaseAuth.getInstance()

        initOnClickListener()

        GoogleSingResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result:ActivityResult ->
            if(result.resultCode == RESULT_OK){
                result.data?.let{data->
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        //Google Sign In was successful, authenticate with Firebase
                        val account = task.getResult(ApiException::class.java)
                        Log.d("mmm result:", account.email.toString())
                    }catch (e: ApiException){
                        Log.w(TAG, "Google sign in failed", e)
                    }

                }
            }else{
                println(result)
            }
        }

    }

    private fun initOnClickListener() {
        binding.btnGoogleSignIn.setOnClickListener {
            Log.d("mmm", "로그인 버튼 클릭")
            googleSignIn()
        }
    }

    private fun googleSignIn(){
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        GoogleSingResultLauncher.launch(signInIntent)
    }
}