package com.ruangtenun.app.view.authentication.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.ruangtenun.app.R
import com.ruangtenun.app.databinding.FragmentLoginBinding
import com.ruangtenun.app.view.authentication.register.RegisterFragment
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("942725723628-lphe17c5agvd83l4jo4fft1u6ribm329.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.apply {
            tvSignUpClick.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        RegisterFragment(),
                        RegisterFragment::class.java.simpleName
                    )
                    .commit()
            }
            googleLogin.setOnClickListener {
                lifecycleScope.launch {
                    loginWithGoogle()
                }
            }
        }

        return view
    }

    private fun loginWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("GoogleSignIn", "requestCode: $requestCode, resultCode: $resultCode, data: $data")

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(data: Task<GoogleSignInAccount?>) {
        try {
            val account = data.getResult(ApiException::class.java)
            val email = account?.email
            val displayName = account?.displayName
            val idToken = account?.idToken

            Toast.makeText(requireContext(), "Login berhasil: $displayName", Toast.LENGTH_SHORT)
                .show()
            Log.d("GoogleSignIn", "Email: $email, ID Token: $idToken")

        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Login gagal: ${e.statusCode}")
            Toast.makeText(requireContext(), "Login gagal: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}