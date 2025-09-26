import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import kotlinx.coroutines.tasks.await

class GoogleAuthUiClient(private val context: Context) {

    private val oneTapClient: SignInClient = Identity.getSignInClient(context)

    // Suspend function to get the IntentSender
    suspend fun signInIntentSender(): IntentSender? {
        return try {
            val request = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId("932853900157-tp24hams577pukh9srca0jl8722fpnkf.apps.googleusercontent.com")
                        .setFilterByAuthorizedAccounts(false)
                        .build()
                )
                .setAutoSelectEnabled(false)
                .build()

            val result = oneTapClient.beginSignIn(request).await()
            result.pendingIntent.intentSender
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getSignInCredential(intent: Intent?): SignInCredential? {
        return try {
            intent?.let { oneTapClient.getSignInCredentialFromIntent(it) }
        } catch (e: Exception) {
            null
        }
    }
}