package com.ruangtenun.app.view.authentication.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.tasks.Task
import com.ruangtenun.app.R
import com.ruangtenun.app.databinding.FragmentLoginBinding
import com.ruangtenun.app.view.authentication.register.RegisterFragment
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleSignInClient: GoogleSignInClient

    // Menyiapkan Activity Result API untuk Google Sign-In
    private val googleSignInResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        } else {
            Log.e("GoogleSignIn", "Login gagal atau dibatalkan. resultCode: ${result.resultCode}")
            Toast.makeText(requireContext(), "Login gagal atau dibatalkan.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        // Setup Google Sign-In options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("942725723628-9hmggo0p6esoksqm1l6ma5h2tlghu1fl.apps.googleusercontent.com") // Ganti dengan Android Client ID yang benar
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
                googleSignInResultLauncher.launch(googleSignInClient.signInIntent)
            }
        }

        return view
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount?>) {
        try {
            val account = task.getResult(ApiException::class.java)
            val email = account?.email
            val displayName = account?.displayName
            val idToken = account?.idToken

            Log.d("GoogleSignIn", "Login berhasil: $displayName, Email: $email")
            Toast.makeText(requireContext(), "Login berhasil: $displayName", Toast.LENGTH_SHORT).show()

            // Kirim token ke server untuk verifikasi jika diperlukan

        } catch (e: ApiException) {
            // Menampilkan status code dan pesan kesalahan lebih detail
            Log.e("GoogleSignIn", "Login gagal. Status Code: ${e.statusCode}, Message: ${e.message}")
            Toast.makeText(requireContext(), "Login gagal: ${e.message}, Status Code: ${e.statusCode}", Toast.LENGTH_SHORT).show()

            // Status codes dapat membantu memecahkan masalah
            when (e.statusCode) {
                CommonStatusCodes.SIGN_IN_REQUIRED -> {
                    // Sign-in diperlukan, coba lagi atau tampilkan UI
                }
                CommonStatusCodes.NETWORK_ERROR -> {
                    // Cek koneksi internet
                }
                else -> {
                    // Lain-lain: Masalah umum yang perlu penanganan lebih lanjut
                }
            }
        }
    }
}


